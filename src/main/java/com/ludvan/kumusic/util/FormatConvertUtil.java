package com.ludvan.kumusic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class FormatConvertUtil {
    private static final String server_from_url = "D:\\带学生\\软件项目-酷U音乐\\实体\\微信小程序端+uniapp\\ku_music\\unpackage\\dist\\dev\\mp-weixin\\static\\test\\trans_sta_correct_name\\";
    private static final String server_to_url = "D:\\带学生\\软件项目-酷U音乐\\实体\\微信小程序端+uniapp\\ku_music\\unpackage\\dist\\dev\\mp-weixin\\static\\test\\song_which_converted\\";

    //mp3转aac
    public static void convertMp3ToAac(String songName) throws IOException, InterruptedException {
        // 构建 FFmpeg 命令
        String name = removeExtension(songName);
        String[] command = {
                "ffmpeg",
                "-i", server_from_url+songName, // 输入文件
                "-c:a", "aac",       // 音频编解码器
                "-b:a", "192k",      // 音频比特率
                server_to_url+name+".aac"       // 输出文件
        };

        // 使用 ProcessBuilder 执行命令
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // 读取并打印 FFmpeg 的输出
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 等待进程完成
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg 转换失败，退出代码：" + exitCode);
        }
    }

    //mp3转wav
    public static void convertMp3ToWav(String songName) throws IOException, InterruptedException {
        String url = server_from_url+songName;
        String name = removeExtension(songName);
        String outputFilePath = server_to_url+name+".wav";

        System.out.println(url);
        System.out.println(outputFilePath);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", url, outputFilePath);
            Process process = processBuilder.start();
            process.waitFor();

            System.out.println("MP3 to WAV conversion completed.");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }

    //aac转wav
    public static void convertAacToWav(String songName) throws IOException, InterruptedException {
        String inputFilePath = server_from_url+songName;
        String name = removeExtension(songName);
        String outputFilePath = server_to_url+name+".wav";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", inputFilePath, outputFilePath
        );

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("AAC to WAV conversion successful");
            } else {
                System.out.println("Conversion failed with error code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }

    //aac转mp3
    public static void convertAacToMp3(String songName) throws IOException, InterruptedException {
        String inputFilePath = server_from_url+songName;
        String name = removeExtension(songName);
        String outputFilePath = server_to_url+name+".mp3";

        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", inputFilePath, outputFilePath
        );

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("AAC to MP3 conversion successful");
            } else {
                System.out.println("Conversion failed with error code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }

    //wav转aac
    public static void convertWavToAac(String songName) throws IOException, InterruptedException {
        // 构建 FFmpeg 命令
        String inputFilePath = server_from_url+songName;
        String name = removeExtension(songName);
        String outputFilePath = server_to_url+name+".aac";

        String[] command = {
                "ffmpeg",
                "-i", inputFilePath, // 输入文件
                "-c:a", "aac",       // 音频编解码器
                "-b:a", "192k",      // 音频比特率
                outputFilePath       // 输出文件
        };

        // 使用 ProcessBuilder 执行命令
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // 读取并打印 FFmpeg 的输出
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        // 等待进程完成
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg 转换失败，退出代码：" + exitCode);
        }
    }

    //wav转mp3
    public static void convertWavToMp3(String songName) throws IOException, InterruptedException {
        String inputFilePath = server_from_url+songName;
        String name = removeExtension(songName);
        String outputFilePath = server_to_url+name+".mp3";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", inputFilePath, outputFilePath);
            Process process = processBuilder.start();
            process.waitFor();

            System.out.println("WAV to MP3 conversion completed.");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }

    public static String removeExtension(String fileName) {
        String[] parts = fileName.split("\\.");
        if (parts.length > 1) {
            return String.join(".", Arrays.copyOf(parts, parts.length - 1));
        }
        return fileName;  // 如果没有找到点，返回原始文件名
    }

}
