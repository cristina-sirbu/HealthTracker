package com.healthtracker.healthtracker.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public final class FreshnessToken {

    private static final String HMAC_ALG = "HmacSHA256";
    // TODO: read from env/property in real app
    private static final String SECRET = "change-this-in-env";

    private FreshnessToken() {}

    public static String create(long validSeconds) {
        long exp = Instant.now().getEpochSecond() + validSeconds;
        String payload = Long.toString(exp);
        String sig = sign(payload);
        return payload + "." + sig;
    }

    public static boolean isValid(String headerValue) {
        if (headerValue == null || !headerValue.contains(".")) return false;
        String[] parts = headerValue.split("\\.", 2);
        String expStr = parts[0];
        String sig = parts[1];
        if (!constantTimeEquals(sig, sign(expStr))) return false;

        long exp;
        try {
            exp = Long.parseLong(expStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return Instant.now().getEpochSecond() <= exp;
    }

    private static String sign(String payload) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALG);
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), HMAC_ALG));
            byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        } catch (Exception e) {
            throw new IllegalStateException("HMAC error", e);
        }
    }

    // simple constant-time compare
    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int res = 0;
        for (int i = 0; i < a.length(); i++) res |= a.charAt(i) ^ b.charAt(i);
        return res == 0;
    }
}
