package com.foodmart.food.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    private final Path uploads = Paths.get("uploads").toAbsolutePath();

    public FileStorageService() throws IOException {
        Files.createDirectories(uploads);
    }

    public Path store(byte[] data, String filename) throws IOException {
        Path target = uploads.resolve(filename);
        Files.write(target, data);
        return target;
    }
}
