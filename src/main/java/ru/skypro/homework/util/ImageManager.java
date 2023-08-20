package ru.skypro.homework.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.HomeworkEntity;
import ru.skypro.homework.exception.BadImageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageManager {

    @Value("${img.path}")
    private String IMG_DIR;

    private final String AD_IMG_DIR = IMG_DIR + "/ads";
    private final String USER_IMG_DIR = IMG_DIR + "/users";

    public Path getImgPath(Class entityClass) throws IOException {
        log.trace("getImgPath(class={})", entityClass.getSimpleName());

        if(entityClass.equals(Ad.class)) {
            return validatePath(Path.of(AD_IMG_DIR));
        }

        String error = "Try to get image path for not listed class" +
            entityClass.getSimpleName();
        log.error(error);
        throw new BadImageException(error);
    }

    private Path validatePath(Path path) throws IOException {
        log.trace("validatePath(path={})", path);

        if (Files.isDirectory(path)) {
            return path;
        }
        return Files.createDirectories(path);
    }

    /** If there are any faults during the attempt of writing image file then BadImageException is thrown.
     * @return  img.name()
     */
    public String uploadImg(HomeworkEntity entity, MultipartFile img) {
        log.trace("uploadImg(ad, img");

        try {
            Files.write(
                    Paths.get(getImgPath(entity.getClass()).toString(), getLocalFilename(entity, img)),
                    img.getBytes()
            );
        }
        catch (IOException e)  {
            log.error("uploadImg({}, image.name={}): IOException was thrown",
                    entity, img.getOriginalFilename(), e
            );
            throw new BadImageException(img.getOriginalFilename());
        }
        return img.getName();
    }

    private static String getLocalFilename(HomeworkEntity entity, MultipartFile img) {
        String filename = String.format(
                "%s-%d.%s",
                img.getName(),
                entity.getPk(),
                StringUtils.getFilenameExtension(img.getOriginalFilename())
        );
        log.trace("getLocalFilename({}.pk={}, img.name={})={}",
                entity.getClass().getSimpleName(),
                entity.getPk(), img.getName(), filename);
        return filename;
    }
}
