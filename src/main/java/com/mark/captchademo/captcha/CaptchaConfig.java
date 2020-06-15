package com.mark.captchademo.captcha;

import nl.captcha.text.renderer.WordRenderer;

import java.util.ArrayList;
import java.util.List;

public class CaptchaConfig {

    private int width;
    private int height;
    private boolean isAddBorder;
    private WordRenderer wordRenderer;
    private List<Object> producers;

    public CaptchaConfig(final int width, final int height, final boolean isAddBorder,
                         final WordRenderer wordRenderer, final List<Object> producers) {
        this.width = width;
        this.height = height;
        this.isAddBorder = isAddBorder;
        this.wordRenderer = wordRenderer;
        this.producers = producers;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private int width;
        private int height;
        private boolean isAddBorder;
        private WordRenderer wordRenderer;
        private List<Object> producers = new ArrayList<>();

        private Builder() {
        }

        public Builder setWidth(final int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(final int height) {
            this.height = height;
            return this;
        }

        public Builder setAddBorder(final boolean addBorder) {
            this.isAddBorder = addBorder;
            return this;
        }

        public Builder setWordRenderer(final WordRenderer wordRenderer) {
            this.wordRenderer = wordRenderer;
            return this;
        }

        public Builder addProducer(final Object producer) {
            this.producers.add(producer);
            return this;
        }

        public CaptchaConfig build() {
            return new CaptchaConfig(this.width, this.height, this.isAddBorder, this.wordRenderer, this.producers);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isAddBorder() {
        return isAddBorder;
    }

    public WordRenderer getWordRenderer() {
        return wordRenderer;
    }

    public List<Object> getProducers() {
        return producers;
    }
}
