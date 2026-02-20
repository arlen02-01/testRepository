package com.ott.api_admin.upload.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "콘텐츠 업로드 초기화 응답")
public record ContentsUploadInitResponse(

        @Schema(type = "Long", description = "생성된 콘텐츠 ID", example = "1")
        Long contentsId,

        @Schema(type = "String", description = "포스터 object key", example = "contents/1/poster/poster.jpg")
        String posterObjectKey,

        @Schema(type = "String", description = "썸네일 object key", example = "contents/1/thumbnail/thumbnail.jpg")
        String thumbnailObjectKey,

        @Schema(type = "String", description = "원본 영상 object key", example = "contents/1/origin/origin.mp4")
        String originObjectKey,

        @Schema(type = "String", description = "트랜스코딩 마스터 object key", example = "contents/1/transcoded/master.m3u8")
        String transcodedMasterObjectKey,

        @Schema(type = "String", description = "포스터 업로드용 presigned URL")
        String posterUploadUrl,

        @Schema(type = "String", description = "썸네일 업로드용 presigned URL")
        String thumbnailUploadUrl,

        @Schema(type = "String", description = "원본 영상 업로드용 presigned URL")
        String originUploadUrl,

        @Schema(type = "String", description = "트랜스코딩 마스터 업로드용 presigned URL")
        String transcodedMasterUploadUrl
) {
}
