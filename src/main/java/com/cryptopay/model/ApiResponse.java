package com.cryptopay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет ответ API CryptoBot.
 * @param <T> тип данных в ответе
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    /**
     * Признак успешности запроса.
     */
    private boolean ok;
    
    /**
     * Результат запроса. Может быть null, если запрос не удался.
     */
    private T result;
    
    /**
     * Код ошибки. Присутствует только если ok = false.
     */
    private Integer errorCode;
    
    /**
     * Описание ошибки. Присутствует только если ok = false.
     */
    private String description;
    
    /**
     * Проверяет, является ли ответ успешным.
     * @return true, если ответ успешен, иначе false
     */
    public boolean isSuccess() {
        return ok;
    }
    
    /**
     * Проверяет, содержит ли ответ ошибку.
     * @return true, если ответ содержит ошибку, иначе false
     */
    public boolean hasError() {
        return !ok;
    }
} 