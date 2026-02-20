package com.ott.api_admin.upload.dto.request;

import com.ott.domain.common.PublicStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "콘텐츠 업로드 초기화 요청")
public record ContentsUploadInitRequest(

        @Schema(type = "Long", description = "시리즈 ID (선택)", example = "1")
        Long seriesId,

        @Schema(type = "String", description = "콘텐츠 제목", example = "에피소드 1")
        @NotBlank
        String title,

        @Schema(type = "String", description = "콘텐츠 설명", example = "첫 번째 에피소드")
        @NotBlank
        String description,

        @Schema(type = "String", description = "출연진", example = "Actor A, Actor B")
        @NotBlank
        String actors,

        @Schema(type = "String", description = "공개 여부", example = "PUBLIC")
        @NotNull
        PublicStatus publicStatus,

        @Schema(type = "Integer", description = "러닝타임(초)", example = "1200")
        Integer duration,

        @Schema(type = "Integer", description = "영상 크기(MB 등)", example = "2048")
        Integer videoSize,

        @Schema(type = "String", description = "포스터 파일명(확장자 포함)", example = "poster.jpg")
        @NotBlank
        String posterFileName,

        @Schema(type = "String", description = "썸네일 파일명(확장자 포함)", example = "thumbnail.jpg")
        @NotBlank
        String thumbnailFileName,

        @Schema(type = "String", description = "원본 영상 파일명(.mp4)", example = "origin.mp4")
        @NotBlank
        String originFileName
) {
}
