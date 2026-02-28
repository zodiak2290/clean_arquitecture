package com.alberto.enterprise.infrastructure.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


import com.alberto.enterprise.application.port.out.FileStorage;


public class LocalFileStorage implements FileStorage {
    @Override
    public String upload(byte[] bytes, String fileName, String contentType) {
        try {
            Path dir = Path.of("uploads");
            Files.createDirectories(dir);
            Path target = dir.resolve(fileName);
            Files.write(target, bytes);
            return target.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }
    }
}
