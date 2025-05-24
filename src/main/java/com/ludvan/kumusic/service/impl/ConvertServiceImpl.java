package com.ludvan.kumusic.service.impl;


import com.ludvan.kumusic.config.Constants;
import com.ludvan.kumusic.service.ConvertService;
import com.ludvan.kumusic.util.FormatConvertUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ConvertServiceImpl implements ConvertService {



    @Override
    public ResponseEntity<String> convertMp3ToAac(String songName) {
        try {
            FormatConvertUtil.convertMp3ToAac(songName);
            return ResponseEntity.ok("MP3 to AAC conversion successful");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Conversion failed: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> convertMp3ToWav(String songName) {
        try {
            FormatConvertUtil.convertMp3ToWav(songName);
            return ResponseEntity.ok("MP3 to WAV conversion successful");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Conversion failed: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> convertAacToWav(String songName) {
        try {
            FormatConvertUtil.convertAacToWav(songName);
            return ResponseEntity.ok("AAC to WAV conversion successful");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Conversion failed: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> convertAacToMp3(String songName) {
        try {
            FormatConvertUtil.convertAacToMp3(songName);
            return ResponseEntity.ok("AAC to MP3 conversion successful");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Conversion failed: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> convertWavToAac(String songName) {
        try {
            FormatConvertUtil.convertWavToAac(songName);
            return ResponseEntity.ok("WAV to AAC conversion successful");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Conversion failed: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> convertWavToMp3(String songName) {
        try {
            FormatConvertUtil.convertWavToMp3(songName);
            return ResponseEntity.ok("WAV to MP3 conversion successful");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Conversion failed: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> downloadAndMoveAudioFile(MultipartFile file, String sourceFileUrl) {
        try {
            // 保存上传的文件
            String uploadedFileName = file.getOriginalFilename();
            if (uploadedFileName == null || uploadedFileName.isEmpty()) {
                throw new IOException("Uploaded file name is invalid.");
            }
            Path uploadedFilePath = Paths.get(Constants.DEST_DIR_FROM_CLIENT, uploadedFileName);
            Files.copy(file.getInputStream(), uploadedFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(uploadedFilePath);

            // 处理源文件路径
            Path sourcePath;
            if (sourceFileUrl.startsWith("http://") || sourceFileUrl.startsWith("https://")) {
                // 处理网络URL
                URL url = new URL(sourceFileUrl);
                sourcePath = downloadFileFromUrl(url);
            } else {
                // 处理本地文件路径
                sourcePath = Paths.get(sourceFileUrl);
                if (!Files.exists(sourcePath)) {
                    throw new IOException("Source file does not exist.");
                }
            }

            // 验证文件类型
            if (!isAudioFile(sourcePath.toFile())) {
                throw new IOException("The specified file is not an audio file.");
            }

            // 移动文件到目标目录
            Path destPath = Paths.get(Constants.DEST_DIR_MOVED, sourcePath.getFileName().toString());
            Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);

            // 打印原始文件名
            System.out.println("Uploaded file name: " + uploadedFileName);
            System.out.println("Downloaded file name: " + sourcePath.getFileName().toString());



            return ResponseEntity.ok("Audio file uploaded, downloaded, and moved successfully.");
        } catch (IOException e) {


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing files: " + e.getMessage());
        }
    }

    private Path downloadFileFromUrl(URL url) throws IOException {
        Path tempFile = Files.createTempFile(null, null);
        try (InputStream in = url.openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile;
    }

    private boolean isAudioFile(File file) {
        if (file.isFile()) {
            String fileName = file.getName().toLowerCase();
            return fileName.endsWith(".mp3") || fileName.endsWith(".wav") || fileName.endsWith(".aac");
        }
        return false;
    }
}