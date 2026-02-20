package com.ott.api_admin.upload.service;

import com.ott.api_admin.upload.dto.request.SeriesUploadInitRequest;
import com.ott.api_admin.upload.dto.response.SeriesUploadInitResponse;
import com.ott.common.web.exception.BusinessException;
import com.ott.common.web.exception.ErrorCode;
import com.ott.domain.member.domain.Member;
import com.ott.domain.member.domain.Provider;
import com.ott.domain.member.domain.Role;
import com.ott.domain.series.domain.Series;
import com.ott.domain.series.repository.SeriesRepository;
import com.ott.infra.s3.service.S3PresignService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeriesUploadService {

    private final SeriesRepository seriesRepository;
    private final EntityManager entityManager;
    private final S3PresignService s3PresignService;

    @Transactional
    public SeriesUploadInitResponse createSeriesUpload(SeriesUploadInitRequest request) {
        Member uploader = resolveUploader();

        Series series = Series.builder()
                .uploader(uploader)
                .title(request.title())
                .description(request.description())
                .actors(request.actors())
                .posterUrl("PENDING")
                .thumbnailUrl("PENDING")
                .bookmarkCount(0L)
                .likesCount(0L)
                .publicStatus(request.publicStatus())
                .build();

        Series savedSeries = seriesRepository.save(series);
        Long seriesId = savedSeries.getId();

        String posterObjectKey = buildObjectKey(seriesId, "poster", request.posterFileName());
        String thumbnailObjectKey = buildObjectKey(seriesId, "thumbnail", request.thumbnailFileName());
        savedSeries.updateMediaKeys(posterObjectKey, thumbnailObjectKey);

        return new SeriesUploadInitResponse(
                seriesId,
                posterObjectKey,
                thumbnailObjectKey,
                s3PresignService.createPutPresignedUrl(posterObjectKey, resolveContentType(request.posterFileName())),
                s3PresignService.createPutPresignedUrl(thumbnailObjectKey, resolveContentType(request.thumbnailFileName()))
        );
    }

    private String buildObjectKey(Long seriesId, String mediaType, String fileName) {
        return "series/" + seriesId + "/" + mediaType + "/" + fileName;
    }

    private String resolveContentType(String fileName) {
        String lowerFileName = fileName.toLowerCase();
        if (lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lowerFileName.endsWith(".png")) {
            return "image/png";
        }
        if (lowerFileName.endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream";
    }

    private Member resolveUploader() {
        return entityManager.createQuery("select m from Member m order by m.id asc", Member.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst()
                .orElseGet(this::createDefaultUploader);
    }

    private Member createDefaultUploader() {
        Member uploader = Member.builder()
                .email("admin@oplust.local")
                .password("dummy-password")
                .nickname("admin")
                .role(Role.ADMIN)
                .provider(Provider.LOCAL)
                .providerId(null)
                .refreshToken(null)
                .build();

        try {
            entityManager.persist(uploader);
            entityManager.flush();
            return uploader;
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, ex);
        }
    }
}
