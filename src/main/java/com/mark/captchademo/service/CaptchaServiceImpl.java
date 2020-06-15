package com.mark.captchademo.service;

import com.mark.captchademo.captcha.CaptchaConfig;
import com.mark.captchademo.captcha.CaptchaProducer;
import com.mark.captchademo.util.Md5CryptoUtil;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.RippleGimpyRenderer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.noise.StraightLineNoiseProducer;
import nl.captcha.text.producer.DefaultTextProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    private static final CaptchaConfig DEFAULT_CAPTCHA_CONFIG;

    static {
        List<Color> colors = Arrays.asList(
                new Color(0, 244, 255),
                new Color(5, 31, 240),
                new Color(16, 64, 14),
                new Color(10, 80, 23)
        );
        List<Font> fonts = Arrays.asList(
                new Font("Times New Roman", Font.PLAIN, 50),
                new Font("Times New Roman", Font.PLAIN, 50),
                new Font("Times New Roman", Font.PLAIN, 50),
                new Font("Times New Roman", Font.PLAIN, 50)
        );
        DEFAULT_CAPTCHA_CONFIG = CaptchaConfig.newBuilder()
                .setWidth(230)
                .setHeight(65)
                .setAddBorder(true)
                .setWordRenderer(new DefaultWordRenderer(colors, fonts))
                .addProducer(new DefaultTextProducer(4, new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}))
                .addProducer(new GradiatedBackgroundProducer())
                .addProducer(new RippleGimpyRenderer())
                .addProducer(new CurvedLineNoiseProducer())
                .addProducer(new StraightLineNoiseProducer())
                .build();
    }


    @Override
    public CreateCaptchaResult createCaptcha() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final Captcha captcha = new CaptchaProducer().crateCaptcha(DEFAULT_CAPTCHA_CONFIG);

        String ans = captcha.getAnswer();
        String encAns = Md5CryptoUtil.encrypt(ans);

        return new CreateCaptchaResult(
                captcha.getImage(),
                ans,
                encAns,
                captcha.getTimeStamp()
        );
    }

    @Override
    public ValidateCaptchaResult validateCaptcha(ValidateCaptchaParam validateCaptchaParam) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String ans = validateCaptchaParam.getAns();
        String encAns = validateCaptchaParam.getEncAns();

        ValidateCaptchaResult result = new ValidateCaptchaResult(false);

        if (ans == null || encAns == null) return result;
        if (Md5CryptoUtil.encrypt(ans).equals(encAns)) {
            result.setPass(true);
        }
        return result;
    }

}
