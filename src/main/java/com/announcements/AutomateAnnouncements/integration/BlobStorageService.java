package com.announcements.AutomateAnnouncements.integration;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class BlobStorageService {

    @Value("${azure.blob.connection-string}")
    private String connectionString;

    @Value("${azure.blob.container-name}")
    private String containerName;

    public String uploadFile(MultipartFile file) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName)
                    .getBlobClient(file.getOriginalFilename());

            blobClient.upload(file.getInputStream(), file.getSize(), true);

            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Blob Storage", e);
        }
    }
}