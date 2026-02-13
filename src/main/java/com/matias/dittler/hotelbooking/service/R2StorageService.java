package com.matias.dittler.hotelbooking.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.matias.dittler.hotelbooking.exception.OurException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class R2StorageService {

    @Value("${storage.r2.access-key}")
    private String accessKey;

    @Value("${storage.r2.secret-key}")
    private String secretKey;

    @Value("${storage.r2.bucket}")
    private String bucketName;

    @Value("${storage.r2.endpoint}")
    private String endpoint;

    @Value("${storage.r2.public-url}")
    private String publicUrl;

    public String uploadImage(MultipartFile file) {
        try {
            String fileName =
                    "images/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();

            BasicAWSCredentials credentials =
                    new BasicAWSCredentials(accessKey, secretKey);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(endpoint, "auto")
                    )
                    .withPathStyleAccessEnabled(true)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            s3Client.putObject(
                    new PutObjectRequest(
                            bucketName,
                            fileName,
                            file.getInputStream(),
                            metadata
                    )
            );

            // URL que usa el frontend
            return publicUrl + "/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            throw new OurException("Error al cargar la imagen a Cloudflare R2");
        }
    }
}
