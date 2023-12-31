package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.util.ImageManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageManager imageManager;

    public byte[] getImage(String subdir, String filename) {
        log.trace(
                "-getUserImage(subdir={}, filename={})",
                subdir,
                filename);
        return imageManager.getImage(subdir, filename);
    }

}
