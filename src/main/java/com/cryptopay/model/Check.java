package com.cryptopay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Представляет информацию о чеке криптовалюты.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Check {
    /**
     * Уникальный идентификатор этого чека.
     */
    private Long checkId;
    
    /**
     * Хеш чека.
     */
    private String hash;
    
    /**
     * Код криптовалюты.
     */
    private String asset;
    
    /**
     * Сумма чека в виде числа с плавающей точкой.
     */
    private BigDecimal amount;
    
    /**
     * URL, который должен быть предоставлен пользователю для активации чека.
     */
    private String botCheckUrl;
    
    /**
     * Статус чека: "active" или "activated".
     */
    private String status;
    
    /**
     * Дата создания чека.
     */
    private LocalDateTime createdAt;
    
    /**
     * Дата активации чека (может быть null).
     */
    private LocalDateTime activatedAt;
} 