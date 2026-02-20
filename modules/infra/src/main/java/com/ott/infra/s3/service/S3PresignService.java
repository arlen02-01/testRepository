package com.ott.infra.s3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class S3PresignService {

    private final S3Presigner s3Presigner;
    private final String bucket;
    private final long presignExpireSeconds;

    public S3PresignService(
            S3Presigner s3Presigner,
            @Value("${aws.s3.bucket}") String bucket,
            @Value("${aws.s3.presign-expire-seconds:900}") long presignExpireSeconds
    ) {
        this.s3Presigner = s3Presigner;
        this.bucket = bucket;
        this.presignExpireSeconds = presignExpireSeconds;
    }

    public String createPutPresignedUrl(String objectKey, String contentType) {
        if (!StringUtils.hasText(bucket)) {
            throw new IllegalStateException("aws.s3.bucket must be configured.");
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(presignExpireSeconds))
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest)
                .url()
                .toString();
    }
}
