package com.cargo.booking.claim.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
public class S3Client implements ApplicationRunner {
    private static final String SIGNATURE_ALGORITHM = "AWSS3V4SignerType";
    private final AmazonS3 s3Client;
    private final String bucket;

    public S3Client(
            @Value("${dme.cargo.s3.server_endpoint}") String serverEndpoint,
            @Value("${dme.cargo.s3.login}") String login,
            @Value("${dme.cargo.s3.password}") String password,
            @Value("${dme.cargo.s3.bucket}") String bucket
    ) {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride(SIGNATURE_ALGORITHM);

        s3Client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                serverEndpoint,
                                Regions.DEFAULT_REGION.name()))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(login, password)
                ))
                .build();

        this.bucket = bucket;
    }

    private void createBucketIfNeeded() {
        if (!s3Client.doesBucketExistV2(bucket)) {
            s3Client.createBucket(bucket);
            log.info("Bucket {} created", bucket);
        } else {
            log.info("Bucket {} found", bucket);
        }
    }

    public void uploadFile(UUID storedFileId, InputStream inputStream, long size) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        s3Client.putObject(bucket, storedFileId.toString(), inputStream, objectMetadata);
    }

    public InputStream downloadFile(UUID storedFileId) {
        return s3Client.getObject(bucket, storedFileId.toString()).getObjectContent();
    }

    public void deleteFile(UUID storedFileId) {
        s3Client.deleteObject(bucket, storedFileId.toString());
    }

    @Override
    public void run(ApplicationArguments args) {
        createBucketIfNeeded();
    }
}
