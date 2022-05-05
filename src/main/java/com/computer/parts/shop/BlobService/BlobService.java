package com.computer.parts.shop.BlobService;

import com.azure.core.implementation.util.BinaryDataContent;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class BlobService {

  private final BlobServiceClient blobClient;

  public String uploadBlob(List<String> data) {
    StringBuilder stringBuilder = new StringBuilder();

    UUID uuid = UUID.randomUUID();

    String filename = uuid + ".txt";

    BlobClient images = blobClient
      .getBlobContainerClient("images")
      .getBlobClient(filename);

    for (String buff : data) {
      stringBuilder.append(buff);
      stringBuilder.append("\n");
    }

    BinaryData binaryData = BinaryData.fromBytes(
      stringBuilder.toString().getBytes()
    );

    images.upload(binaryData);

    return filename;
  }

  public String uploadBlob(List<String> data, String filename) {
    StringBuilder stringBuilder = new StringBuilder();

    BlobClient images = blobClient
      .getBlobContainerClient("images")
      .getBlobClient(filename);

    for (String buff : data) {
      stringBuilder.append(buff);
      stringBuilder.append("\n");
    }

    BinaryData binaryData = BinaryData.fromBytes(
      stringBuilder.toString().getBytes()
    );

    images.upload(binaryData, true);

    return filename;
  }

  public List<String> getBlobAttachments(String fileName) {
    BlobClient images = blobClient
      .getBlobContainerClient("images")
      .getBlobClient(fileName);

    BinaryData binaryData = images.downloadContent();
    byte[] bytes = binaryData.toBytes();

    String s = new String(bytes);
    return Arrays.stream(s.split("\\n")).toList();
  }

  public void removeBlob(String fileName) {
    BlobClient images = blobClient
      .getBlobContainerClient("images")
      .getBlobClient(fileName);

    images.delete();
  }
}
