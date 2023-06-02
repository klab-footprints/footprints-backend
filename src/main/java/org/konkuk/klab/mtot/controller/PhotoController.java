package org.konkuk.klab.mtot.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Pin;
import org.konkuk.klab.mtot.dto.response.CalenderThumbnailResponse;
import org.konkuk.klab.mtot.dto.response.PhotoLinksResponse;
import org.konkuk.klab.mtot.dto.response.PhotoUploadResponse;
import org.konkuk.klab.mtot.s3.AwsS3FileSupporter;
import org.konkuk.klab.mtot.service.JourneyService;
import org.konkuk.klab.mtot.service.PhotoService;
import org.konkuk.klab.mtot.service.PinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;
    private final PinService pinService;
    private final JourneyService journeyService;
    @GetMapping("/pin/{id}")
    private ResponseEntity<PhotoLinksResponse> getLinkByPin(@PathVariable("id") Long pinId){
        PhotoLinksResponse response = photoService.findPhotoByPin(pinService.findPinById(pinId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Journey/{id}")
    private ResponseEntity<PhotoLinksResponse> getLinkByJourney(@PathVariable("id") Long journeyId){
        PhotoLinksResponse response = photoService.findPhotoByJourney(journeyService.findJourneyById(journeyId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/calenderThumbnail")
    private ResponseEntity<CalenderThumbnailResponse> getCalenderThumbnailLink(
            @RequestBody @Valid @RequestParam("year") int year, @RequestParam("month") int month){
        CalenderThumbnailResponse response = photoService.findThumbnail(year,month);;
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PhotoUploadResponse> uploadPhotos2aws(@RequestBody @Valid  String loginEmail, Long pinId, List<MultipartFile> multipartFiles) throws IOException {
        PhotoUploadResponse response = photoService.uploadPhotos(loginEmail,pinId,multipartFiles);
        return ResponseEntity.ok(response);
    }
}
