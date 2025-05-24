package com.ludvan.kumusic.controller;


import com.ludvan.kumusic.service.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/convert")
public class ConvertController {

    @Autowired
    private ConvertService convertService;

    @PostMapping("/downloadAndMoveAudioFile")
    public ResponseEntity<String> downloadAndMoveAudioFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("sourceFileUrl") String sourceFileUrl) {
        return convertService.downloadAndMoveAudioFile(file, sourceFileUrl);
    }

    @PostMapping("/mp3-to-aac")
    public ResponseEntity<String> convertMp3ToAac(@RequestParam String songName) {
        return convertService.convertMp3ToAac(songName);
    }

    @PostMapping("/mp3-to-wav")
    public ResponseEntity<String> convertMp3ToWav(@RequestParam String songName) {
        return convertService.convertMp3ToWav(songName);
    }

    @PostMapping("/aac-to-wav")
    public ResponseEntity<String> convertAacToWav(@RequestParam String songName) {
        return convertService.convertAacToWav(songName);
    }

    @PostMapping("/aac-to-mp3")
    public ResponseEntity<String> convertAacToMp3(@RequestParam String songName) {
        return convertService.convertAacToMp3(songName);
    }

    @PostMapping("/wav-to-aac")
    public ResponseEntity<String> convertWavToAac(@RequestParam String songName) {
        return convertService.convertWavToAac(songName);
    }

    @PostMapping("/wav-to-mp3")
    public ResponseEntity<String> convertWavToMp3(@RequestParam String songName) {
        return convertService.convertWavToMp3(songName);
    }


}
