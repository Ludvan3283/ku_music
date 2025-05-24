package com.ludvan.kumusic.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ConvertService {
    ResponseEntity<String> convertMp3ToAac(String songName);
    ResponseEntity<String> convertMp3ToWav(String songName);
    ResponseEntity<String> convertAacToWav(String songName);
    ResponseEntity<String> convertAacToMp3(String songName);
    ResponseEntity<String> convertWavToAac(String songName);
    ResponseEntity<String> convertWavToMp3(String songName);
    ResponseEntity<String> downloadAndMoveAudioFile(MultipartFile file, String sourceFileUrl);
}
