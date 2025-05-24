package com.ludvan.kumusic.service;

import com.aliyuncs.exceptions.ClientException;

public interface SmsService {
    String sendSms(String phoneNumber, String code) throws ClientException;
}