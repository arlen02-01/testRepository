package com.ott.api_admin.upload.controller;

import com.ott.api_admin.upload.dto.request.SeriesUploadInitRequest;
import com.ott.api_admin.upload.dto.response.SeriesUploadInitResponse;
import com.ott.api_admin.upload.service.SeriesUploadService;
import com.ott.common.web.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-office")
@RequiredArgsConstructor
public class SeriesUploadController implements SeriesUploadApi {

    private final SeriesUploadService seriesUploadService;

    @Override
    @PostMapping("/admin/series")
    public ResponseEntity<SuccessResponse<SeriesUploadInitResponse>> createSeriesUpload(
            @Valid @RequestBody SeriesUploadInitRequest request
    ) {
        return ResponseEntity.ok(
                SuccessResponse.of(seriesUploadService.createSeriesUpload(request))
        );
    }
}
