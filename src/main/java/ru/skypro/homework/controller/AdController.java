package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.service.AdService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@CrossOrigin(value = "${cross-origin.value}")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ads")
public class AdController {

    private final AdService adService;

    @GetMapping("/{id}")
    public AdDto get(@PathVariable Integer id) {
        log.trace("get(id={}", id);

        return adService.getById(id);
    }

    @GetMapping("/me")
    public Ads getMyAds() {
        log.trace("getMyAds");

        return adService.getAllByCurrentUser();
    }

    @GetMapping
    public Ads getAll() {
        log.trace("getAll");
        return adService.getAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     public ResponseEntity<AdDto> create(
             @Valid @RequestPart CreateOrUpdateAd properties,
             @RequestPart MultipartFile image) {
        log.trace("create(properties={}, image.filename={}))",
                properties,
                image.getOriginalFilename());

        return new ResponseEntity<>(adService.create(properties, image), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public AdDto patchProperties(
            @NotNull @PathVariable Integer id,
            @Valid @RequestBody CreateOrUpdateAd updateAd) {
        return adService.patchProperties(id, updateAd);
    }

    @PatchMapping(path="/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdDto patchImg(
            @NotNull @PathVariable Integer id,
            @RequestPart MultipartFile image) {
        return adService.patchImage(id, image);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable Integer id) {
        adService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
