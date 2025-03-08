package com.cryptopay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Представляет информацию о курсе обмена валюты.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    /**
     * True, если полученный курс актуален.
     */
    private boolean isValid;
    
    /**
     * True, если исходная валюта - криптовалюта.
     */
    private boolean isCrypto;
    
    /**
     * True, если исходная валюта - фиатная.
     */
    private boolean isFiat;
    
    /**
     * Код исходной валюты (криптовалюта или фиатная).
     */
    private String source;
    
    /**
     * Код целевой валюты (фиатная).
     */
    private String target;
    
    /**
     * Текущий курс исходной валюты, выраженный в целевой валюте.
     */
    private BigDecimal rate;
} 