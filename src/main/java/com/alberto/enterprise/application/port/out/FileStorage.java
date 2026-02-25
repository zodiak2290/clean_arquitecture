package com.alberto.enterprise.application.port.out;

public interface FileStorage {
    String upload(byte[] bytes, String fileName, String contentType);
}
