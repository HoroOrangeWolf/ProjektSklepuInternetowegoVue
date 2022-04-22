package com.computer.parts.shop.BlobService;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlobConfig {

    @Bean
    public BlobServiceClient getBlobServiceClient(){
        BlobServiceClientBuilder blobServiceClientBuilder = new BlobServiceClientBuilder();
        blobServiceClientBuilder.connectionString("DefaultEndpointsProtocol=https;AccountName=projektsklepustorage;AccountKey=EcgSfjf1ThYGYLaQ0B2rXzD5ww57pGv7vMdPO2hDeekkIHnlHjHyNO8+zuUmeg23/HC7HN9VBC3/gD9sOiMvHg==;EndpointSuffix=core.windows.net");
        return blobServiceClientBuilder.buildClient();
    }

}
