package com.ott.api_admin.upload.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시리즈 업로드 초기화 응답")
public record SeriesUploadInitResponse(

        @Schema(type = "Long", description = "생성된 시리즈 ID", example = "1")
        Long seriesId,

        @Schema(type = "String", description = "포스터 object key", example = "series/1/poster/poster.jpg")
        String posterObjectKey,

        @Schema(type = "String", description = "썸네일 object key", example = "series/1/thumbnail/thumbnail.jpg")
        String thumbnailObjectKey,

        @Schema(type = "String", description = "포스터 업로드용 presigned URL")
        String posterUploadUrl,

        @Schema(type = "String", description = "썸네일 업로드용 presigned URL")
        String thumbnailUploadUrl
) {
}
