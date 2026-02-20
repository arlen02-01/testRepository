package com.ott.api_admin.upload.dto.request;

import com.ott.domain.common.PublicStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "시리즈 업로드 초기화 요청")
public record SeriesUploadInitRequest(

        @Schema(type = "String", description = "시리즈 제목", example = "비밀의 숲")
        @NotBlank
        String title,

        @Schema(type = "String", description = "시리즈 설명", example = "검사와 형사의 추적극")
        @NotBlank
        String description,

        @Schema(type = "String", description = "출연진", example = "조승우, 배두나")
        @NotBlank
        String actors,

        @Schema(type = "String", description = "공개 여부", example = "PUBLIC")
        @NotNull
        PublicStatus publicStatus,

        @Schema(type = "String", description = "포스터 파일명(확장자 포함)", example = "poster.jpg")
        @NotBlank
        String posterFileName,

        @Schema(type = "String", description = "썸네일 파일명(확장자 포함)", example = "thumbnail.jpg")
        @NotBlank
        String thumbnailFileName
) {
}
