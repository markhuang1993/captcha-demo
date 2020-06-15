package com.mark.captchademo.handler;

import com.mark.captchademo.service.CaptchaService;
import com.mark.captchademo.service.CaptchaService.CreateCaptchaResult;
import com.mark.captchademo.service.CaptchaService.ValidateCaptchaParam;
import com.mark.captchademo.service.CaptchaService.ValidateCaptchaResult;
import com.mark.captchademo.util.ImageUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/captcha")
public class CaptchaHandler {

    private final CaptchaService captchaService;

    public CaptchaHandler(final CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @RequestMapping("/create")
    public ResponseEntity<?> createCaptcha() throws IOException, NoSuchAlgorithmException {
        CreateCaptchaResult result = captchaService.createCaptcha();
        Map<String, String> respMap = new HashMap<>();

        byte[] captchaImgBytes = ImageUtil.bufferedImageToBytes(result.getImage(), "jpg");
        respMap.put("b64Image", Base64.getEncoder().encodeToString(captchaImgBytes));
        respMap.put("encAns", result.getEncAns());

        return ResponseEntity.ok(respMap);
    }

    @RequestMapping("/validate")
    public ResponseEntity<?> validateCaptcha(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String ans = request.getParameter("ans");
        String encAns = request.getParameter("encAns");

        ValidateCaptchaResult result = captchaService.validateCaptcha(new ValidateCaptchaParam(ans, encAns));

        return ResponseEntity.ok(Collections.singletonMap("isPass", result.isPass()));
    }

}
