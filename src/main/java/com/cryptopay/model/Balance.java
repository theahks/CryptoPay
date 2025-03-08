package com.cryptopay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Представляет информацию о балансе криптовалюты.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    /**
     * Код криптовалюты.
     */
    private String currencyCode;
    
    /**
     * Доступное количество.
     */
    private BigDecimal available;
    
    /**
     * Количество, находящееся на удержании.
     */
    private BigDecimal onhold;
} 