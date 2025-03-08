package me.theahks.cryptopay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Представляет информацию о счете (инвойсе) криптовалюты.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    /**
     * Уникальный идентификатор счета.
     */
    private Long invoiceId;
    
    /**
     * Хеш счета.
     */
    private String hash;
    
    /**
     * Код криптовалюты.
     */
    private String asset;
    
    /**
     * Сумма счета в виде числа с плавающей точкой.
     */
    private BigDecimal amount;
    
    /**
     * Валюта фиатных денег.
     */
    private String fiatCurrency;
    
    /**
     * Сумма фиатных денег в виде числа с плавающей точкой.
     */
    private BigDecimal fiatAmount;
    
    /**
     * Описание счета.
     */
    private String description;
    
    /**
     * Уникальный идентификатор Telegram-бота для инвойса.
     */
    private String botInvoiceUrl;
    
    /**
     * Оплатить URL для пользователя, если не предоставлено приложению.
     */
    private String payUrl;
    
    /**
     * Статус счета: "active", "paid" или "expired".
     */
    private String status;
    
    /**
     * Дата создания счета.
     */
    private LocalDateTime createdAt;
    
    /**
     * Дата окончания срока действия счета.
     */
    private LocalDateTime expirationDate;
    
    /**
     * Дата оплаты счета (может быть null).
     */
    private LocalDateTime paidAt;
    
    /**
     * Информация о том, кто оплатил счет (может быть null).
     */
    private PaidBtnUser paidBtnUser;
    
    /**
     * Список доступных методов оплаты.
     */
    private List<String> allowedPaymentMethods;
    
    /**
     * Представляет информацию о пользователе, который оплатил счет.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaidBtnUser {
        /**
         * Идентификатор пользователя в Telegram.
         */
        private Long userId;
        
        /**
         * Имя пользователя.
         */
        private String firstName;
        
        /**
         * Фамилия пользователя (может быть null).
         */
        private String lastName;
        
        /**
         * Имя пользователя в Telegram (может быть null).
         */
        private String username;
        
        /**
         * Фото профиля (может быть null).
         */
        private String photoUrl;
    }
} 