package com.mark.captchademo.captcha;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.BackgroundProducer;
import nl.captcha.gimpy.GimpyRenderer;
import nl.captcha.noise.NoiseProducer;
import nl.captcha.text.producer.TextProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;
import nl.captcha.text.renderer.WordRenderer;

import java.util.List;

public class CaptchaProducer {

    public Captcha crateCaptcha(CaptchaConfig config) {
        final Captcha.Builder captchaBuilder = new Captcha.Builder(config.getWidth(), config.getHeight());

        final List<Object> producers = config.getProducers();
        if (producers != null && !producers.isEmpty()) {
            for (Object producer : producers) {
                if (producer instanceof BackgroundProducer) {
                    captchaBuilder.addBackground((BackgroundProducer) producer);
                } else if (producer instanceof GimpyRenderer) {
                    captchaBuilder.gimp((GimpyRenderer) producer);
                } else if (producer instanceof NoiseProducer) {
                    captchaBuilder.addNoise((NoiseProducer) producer);
                } else if (producer instanceof TextProducer) {
                    final WordRenderer wordRenderer = config.getWordRenderer();
                    captchaBuilder.addText((TextProducer) producer, wordRenderer != null ? wordRenderer : new DefaultWordRenderer());
                } else {
                    throw new IllegalArgumentException("UnSupport producer:" + producer.getClass().getName());
                }
            }
        }

        if (config.isAddBorder()) {
            captchaBuilder.addBorder();
        }
        return captchaBuilder.build();
    }

}
