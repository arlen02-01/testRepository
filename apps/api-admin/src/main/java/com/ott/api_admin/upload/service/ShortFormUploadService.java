package com.ott.api_admin.upload.service;

import com.ott.api_admin.upload.dto.request.ShortFormUploadInitRequest;
import com.ott.api_admin.upload.dto.response.ShortFormUploadInitResponse;
import com.ott.common.web.exception.BusinessException;
import com.ott.common.web.exception.ErrorCode;
import com.ott.domain.contents.domain.Contents;
import com.ott.domain.contents.repository.ContentsRepository;
import com.ott.domain.member.domain.Member;
import com.ott.domain.member.domain.Provider;
import com.ott.domain.member.domain.Role;
import com.ott.domain.series.domain.Series;
import com.ott.domain.series.repository.SeriesRepository;
import com.ott.domain.short_form.domain.ShortForm;
import com.ott.domain.short_form.repository.ShortFormRepository;
import com.ott.infra.s3.service.S3PresignService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShortFormUploadService {

    private final ShortFormRepository shortFormRepository;
    private final SeriesRepository seriesRepository;
    private final ContentsRepository contentsRepository;
    private final EntityManager entityManager;
    private final S3PresignService s3PresignService;

    @Transactional
    public ShortFormUploadInitResponse createShortFormUpload(ShortFormUploadInitRequest request) {
        Member uploader = resolveUploader();
        Series series = resolveSeries(request.seriesId());
        Contents contents = resolveContents(request.contentsId());

        ShortForm shortForm = ShortForm.builder()
                .uploader(uploader)
                .series(series)
                .contents(contents)
                .title(request.title())
                .description(request.description())
                .posterUrl("PENDING")
                .duration(request.duration())
                .videoSize(request.videoSize())
                .bookmarkCount(0L)
                .publicStatus(request.publicStatus())
                .originUrl("PENDING")
                .masterPlaylistUrl("PENDING")
                .build();

        ShortForm savedShortForm = shortFormRepository.save(shortForm);
        Long shortFormId = savedShortForm.getId();

        String posterObjectKey = buildObjectKey(shortFormId, "poster", request.posterFileName());
        String thumbnailObjectKey = buildObjectKey(shortFormId, "thumbnail", request.thumbnailFileName());
        String originObjectKey = buildObjectKey(shortFormId, "origin", request.originFileName());
        String transcodedMasterObjectKey = "short-forms/" + shortFormId + "/transcoded/master.m3u8";
        savedShortForm.updateMediaKeys(
                posterObjectKey,
                originObjectKey,
                transcodedMasterObjectKey
        );

        return new ShortFormUploadInitResponse(
                shortFormId,
                posterObjectKey,
                thumbnailObjectKey,
                originObjectKey,
                transcodedMasterObjectKey,
                s3PresignService.createPutPresignedUrl(posterObjectKey, resolveContentType(request.posterFileName())),
                s3PresignService.createPutPresignedUrl(thumbnailObjectKey, resolveContentType(request.thumbnailFileName())),
                s3PresignService.createPutPresignedUrl(originObjectKey, "video/mp4"),
                s3PresignService.createPutPresignedUrl(transcodedMasterObjectKey, "application/vnd.apple.mpegurl")
        );
    }

    private String buildObjectKey(Long shortFormId, String mediaType, String fileName) {
        return "short-forms/" + shortFormId + "/" + mediaType + "/" + fileName;
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

    private Series resolveSeries(Long seriesId) {
        if (seriesId == null) {
            return null;
        }
        return seriesRepository.findById(seriesId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SERIES_NOT_FOUND));
    }

    private Contents resolveContents(Long contentsId) {
        if (contentsId == null) {
            return null;
        }
        return contentsRepository.findById(contentsId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONTENT_NOT_FOUND));
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
