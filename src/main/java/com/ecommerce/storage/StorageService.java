package com.ecommerce.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

public interface StorageService {

    void init ();

    void store (MultipartFile file);

    Stream<Path> loadAll();

    Path load (String filename);

    Resource loadAsResource (String filename);

    void deleteAll ();

    
}
