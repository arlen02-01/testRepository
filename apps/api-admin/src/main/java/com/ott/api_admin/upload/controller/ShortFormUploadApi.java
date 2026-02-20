package com.ott.api_admin.upload.controller;

import com.ott.api_admin.upload.dto.request.ShortFormUploadInitRequest;
import com.ott.api_admin.upload.dto.response.ShortFormUploadInitResponse;
import com.ott.common.web.exception.ErrorResponse;
import com.ott.common.web.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "ShortForm Upload API", description = "[백오피스] 숏폼 업로드 준비 API")
public interface ShortFormUploadApi {

    @Operation(
            summary = "숏폼 업로드 요청",
            description = "숏폼 메타데이터를 저장하고 poster/thumbnail/origin/transcoded 업로드용 presigned URL 을 발급합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "숏폼 생성 및 presigned URL 발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ShortFormUploadInitResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "접근 권한 없음 (ADMIN만 접근 가능)",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<SuccessResponse<ShortFormUploadInitResponse>> createShortFormUpload(
            @Valid @RequestBody ShortFormUploadInitRequest request
    );
}
