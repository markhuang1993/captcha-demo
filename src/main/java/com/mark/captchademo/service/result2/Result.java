package com.mark.captchademo.service.result2;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Result extends LinkedHashMap<String, Object> implements IResult {

    public Result() {

    }

    public Result(String key, Object val) {
        this.put(key, val);
    }

    public Result(Map<String, Object> m) {
        this.putAll(m);
    }

    @SuppressWarnings("unchecked")
    public <T> T genericGet(final String key) {
        return (T) super.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T genericGetOrDefault(final String key, final T defaultValue) {
        return (T) super.getOrDefault(key, defaultValue);
    }

    public static class Ok extends Result {
        public Ok() {
        }

        public Ok(final String key, final Object val) {
            super(key, val);
        }

        public Ok(final Map<String, Object> m) {
            super(m);
        }

        @Override
        public ResultFlag flag() {
            return ResultFlag.OK;
        }

        @Override
        public boolean isOk() {
            return true;
        }
    }

    public static class Err extends Result {
        public Err() {
        }

        public Err(final String key, final Object val) {
            super(key, val);
        }

        public Err(final Map<String, Object> m) {
            super(m);
        }

        @Override
        public ResultFlag flag() {
            return ResultFlag.ERR;
        }

        @Override
        public boolean isOk() {
            return false;
        }
    }
}
