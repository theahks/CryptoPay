package me.theahks.cryptopay.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Утилитный класс для работы с JSON.
 */
public final class JsonUtils {
    
    private JsonUtils() {}
    
    /**
     * Настраивает ObjectMapper для корректной работы с CryptoBot API.
     * 
     * @param objectMapper ObjectMapper для настройки
     */
    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
} 