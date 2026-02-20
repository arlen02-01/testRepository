package com.ott.api_admin.upload.controller;

import com.ott.api_admin.upload.dto.request.ContentsUploadInitRequest;
import com.ott.api_admin.upload.dto.response.ContentsUploadInitResponse;
import com.ott.api_admin.upload.service.ContentsUploadService;
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
public class ContentsUploadController implements ContentsUploadApi {

    private final ContentsUploadService contentsUploadService;

    @Override
    @PostMapping("/admin/contents")
    public ResponseEntity<SuccessResponse<ContentsUploadInitResponse>> createContentsUpload(
            @Valid @RequestBody ContentsUploadInitRequest request
    ) {
        return ResponseEntity.ok(
                SuccessResponse.of(contentsUploadService.createContentsUpload(request))
        );
    }
}
