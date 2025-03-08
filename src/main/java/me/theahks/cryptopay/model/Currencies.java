package me.theahks.cryptopay.model;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collections;

/**
 * Содержит константы для поддерживаемых валют в CryptoBot API.
 */
public final class Currencies {
    /**
     * Список поддерживаемых криптовалют.
     */
    public static final Set<String> CRYPTOCURRENCIES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "USDT", "TON", "NOT", "GRAM", "DOGS", "HMSTR", "CATI", "TRX", "SOL", "MY",
                    "USDC", "BNB", "BTC", "LTC", "ETH", "TRUMP", "DOGE", "MAJOR", "PEPE", "WIF", 
                    "BONK", "MELANIA"
            ))
    );

    /**
     * Список поддерживаемых фиатных валют.
     */
    public static final Set<String> FIATS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "USD", "EUR", "RUB"
            ))
    );

    /**
     * Проверяет, является ли код криптовалютой.
     * 
     * @param currencyCode код валюты для проверки
     * @return true, если код представляет криптовалюту
     */
    public static boolean isCryptocurrency(String currencyCode) {
        return CRYPTOCURRENCIES.contains(currencyCode);
    }

    /**
     * Проверяет, является ли код фиатной валютой.
     * 
     * @param currencyCode код валюты для проверки
     * @return true, если код представляет фиатную валюту
     */
    public static boolean isFiat(String currencyCode) {
        return FIATS.contains(currencyCode);
    }

    // Приватный конструктор для предотвращения инстанцирования
    private Currencies() {
        throw new AssertionError("No instances for you!");
    }
} 