package com.ott.api_admin.upload.controller;

import com.ott.api_admin.upload.dto.request.ShortFormUploadInitRequest;
import com.ott.api_admin.upload.dto.response.ShortFormUploadInitResponse;
import com.ott.api_admin.upload.service.ShortFormUploadService;
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
public class ShortFormUploadController implements ShortFormUploadApi {

    private final ShortFormUploadService shortFormUploadService;

    @Override
    @PostMapping("/admin/short-forms")
    public ResponseEntity<SuccessResponse<ShortFormUploadInitResponse>> createShortFormUpload(
            @Valid @RequestBody ShortFormUploadInitRequest request
    ) {
        return ResponseEntity.ok(
                SuccessResponse.of(shortFormUploadService.createShortFormUpload(request))
        );
    }
}
