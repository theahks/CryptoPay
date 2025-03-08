package com.cryptopay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Представляет информацию о криптовалютном активе.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    /**
     * Код криптовалюты.
     */
    private String code;
    
    /**
     * Название криптовалюты.
     */
    private String name;
    
    /**
     * Является ли валюта фиатной.
     */
    private boolean isFiat;
    
    /**
     * Минимальная сумма для счетов.
     */
    private BigDecimal minInvoiceAmount;
    
    /**
     * Минимальная сумма для переводов.
     */
    private BigDecimal minTransferAmount;
} 