package com.matias.dittler.hotelbooking.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.matias.dittler.hotelbooking.exception.OurException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio para manejar la carga de imágenes al almacenamiento en la nube
 * usando Cloudflare R2 (compatible con S3 API).
 */
@Service
public class R2StorageService {

    // Credenciales y configuración del bucket en application.properties
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

    /**
     * Método que sube un archivo al bucket y devuelve la URL pública.
     * @param file Archivo a subir (MultipartFile)
     * @return URL pública para acceder al archivo subido
     * @throws OurException Si ocurre un error al subir la imagen
     */
    public String uploadImage(MultipartFile file) {
        try {
            // Genera un nombre único para la imagen con timestamp
            String fileName = "images/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();

            // Configura las credenciales para R2
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

            // Construye el cliente S3 compatible con R2
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(endpoint, "auto")
                    )
                    .withPathStyleAccessEnabled(true) // necesario para R2
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();

            // Metadata del archivo (tipo de contenido y tamaño)
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // Subida del archivo al bucket
            s3Client.putObject(
                    new PutObjectRequest(
                            bucketName,
                            fileName,
                            file.getInputStream(),
                            metadata
                    )
            );

            // Devuelve la URL pública que el frontend puede usar
            return publicUrl + "/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            // Lanza excepción personalizada si algo falla
            throw new OurException("Error al cargar la imagen a Cloudflare R2");
        }
    }
}
