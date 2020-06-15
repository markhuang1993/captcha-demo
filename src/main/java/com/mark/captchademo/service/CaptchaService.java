package com.mark.captchademo.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public interface CaptchaService {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CreateCaptchaResult implements ServiceResult {
        BufferedImage image;
        String ans;
        String encAns;
        Date timestamp;
    }

    CreateCaptchaResult createCaptcha() throws UnsupportedEncodingException, NoSuchAlgorithmException;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class ValidateCaptchaParam implements ServiceParam {
        String ans;
        String encAns;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class ValidateCaptchaResult implements ServiceResult {
        boolean isPass;
    }

    ValidateCaptchaResult validateCaptcha(ValidateCaptchaParam validateCaptchaParam) throws UnsupportedEncodingException, NoSuchAlgorithmException;

}

