package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AdMapper {

    default int map(User user) { return (user!=null)? user.getId() : -1 ; }

    @Mapping(source="id", target = "pk")
    AdDto adToDto(Ad ad);

    @Mapping(source="author.firstName", target = "authorFirstName")
    @Mapping(source="author.lastName", target = "authorLastName")
    @Mapping(source="author.email", target = "email")
    @Mapping(source="author.phone", target = "phone")
    @Mapping(source="id", target = "pk")
    ExtendedAd adToExtendedAd(Ad ad);

    @Mapping(source="image.originalFilename", target = "image")
    @Mapping(source="createOrUpdateAd.pk", target = "id")
    Ad createOrUpdateAdToAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image);

    default Ads adsToDto(Collection<Ad> ads) {
        Ads result = new Ads();
        result.setResults(
                ads.stream()
                        .map(this::adToDto)
                        .collect(Collectors.toList())
        );
        result.setCount(ads.size());
        return result;
    }

    AdDto createOrUpdateAdToDto(CreateOrUpdateAd createOrUpdateAd, String image);
}