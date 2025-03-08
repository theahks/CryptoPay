package com.cryptopay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Представляет информацию о переводе средств.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    /**
     * Уникальный идентификатор этого перевода.
     */
    private Long transferId;
    
    /**
     * Идентификатор пользователя Telegram, которому был отправлен перевод.
     */
    private Long userId;
    
    /**
     * Код криптовалюты.
     */
    private String asset;
    
    /**
     * Сумма перевода в виде числа с плавающей точкой.
     */
    private BigDecimal amount;
    
    /**
     * Статус перевода, может быть только "completed".
     */
    private String status;
    
    /**
     * Дата завершения перевода.
     */
    private LocalDateTime completedAt;
    
    /**
     * Комментарий к этому переводу.
     */
    private String comment;
} 