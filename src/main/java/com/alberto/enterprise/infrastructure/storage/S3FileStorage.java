package com.alberto.enterprise.infrastructure.storage;

import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alberto.enterprise.application.port.out.FileStorage;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3FileStorage implements FileStorage {

    private final S3Client s3;
    private final String bucket;

    public S3FileStorage(S3Client s3, @Value("${app.storage.bucket}") String bucket) {
        this.s3 = s3;
        this.bucket = bucket;
    }

    @Override
    public String upload(byte[] bytes, String fileName, String contentType) {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalStateException("app.storage.bucket is required for S3 uploads");
        }

        // key dentro del bucket
        String key = "avatars/" + fileName;

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        s3.putObject(req, RequestBody.fromBytes(bytes));

        // URL (si el objeto es público o si usas CloudFront / presigned URL más adelante)
        S3Utilities utils = s3.utilities();
        URL url = utils.getUrl(b -> b.bucket(bucket).key(key));
        return url.toString();
    }
}
