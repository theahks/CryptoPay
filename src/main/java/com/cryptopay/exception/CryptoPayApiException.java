package com.cryptopay.exception;

/**
 * Исключение, которое выбрасывается при ошибке в API CryptoBot.
 */
public class CryptoPayApiException extends RuntimeException {
    /**
     * Код ошибки API.
     */
    private final Integer errorCode;

    /**
     * Создает исключение с сообщением и кодом ошибки.
     * 
     * @param message сообщение об ошибке
     * @param errorCode код ошибки API
     */
    public CryptoPayApiException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Создает исключение с сообщением, кодом ошибки и причиной.
     * 
     * @param message сообщение об ошибке
     * @param errorCode код ошибки API
     * @param cause причина исключения
     */
    public CryptoPayApiException(String message, Integer errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Создает исключение с сообщением и причиной.
     * 
     * @param message сообщение об ошибке
     * @param cause причина исключения
     */
    public CryptoPayApiException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    /**
     * Получает код ошибки API.
     * 
     * @return код ошибки API или null, если код не указан
     */
    public Integer getErrorCode() {
        return errorCode;
    }
} 