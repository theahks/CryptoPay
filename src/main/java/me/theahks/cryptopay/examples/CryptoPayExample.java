package me.theahks.cryptopay.examples;

import me.theahks.cryptopay.api.CryptoPay;
import me.theahks.cryptopay.model.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Пример использования библиотеки CryptoPay.
 * Класс демонстрирует основные функции API CryptoBot.
 * 
 * Примечание: для запуска этого примера требуется действительный API-токен CryptoBot.
 */
@Slf4j
public class CryptoPayExample {
    // Замените на свой API-токен
    private static final String API_TOKEN = "YOUR_API_TOKEN";
    
    public static void main(String[] args) {
        // Инициализация клиента
        CryptoPay cryptoPay = CryptoPay.builder()
                .apiToken(API_TOKEN)
                .build();
        
        log.info("CryptoPay SDK инициализирован");
        
        try {
            // Получение информации о боте
            log.info("Получение информации о боте...");
            var botInfo = cryptoPay.getMe();
            log.info("Информация о боте: {}", botInfo);
            
            // Получение списка доступных активов
            log.info("Получение списка доступных активов...");
            List<Asset> assets = cryptoPay.getAssets();
            log.info("Доступные активы:");
            assets.forEach(asset -> log.info(" - {}: {}", asset.getCode(), asset.getName()));
            
            // Получение баланса
            log.info("Получение баланса...");
            List<Balance> balances = cryptoPay.getBalance();
            log.info("Баланс:");
            balances.forEach(balance -> log.info(" - {}: {} (доступно), {} (заблокировано)",
                    balance.getCurrencyCode(), balance.getAvailable(), balance.getOnhold()));
            
            // Получение обменных курсов
            log.info("Получение обменных курсов...");
            List<ExchangeRate> rates = cryptoPay.getAllExchangeRates();
            log.info("Обменные курсы (первые 5):");
            rates.stream().limit(5).forEach(rate -> 
                    log.info(" - {} -> {}: {}", rate.getSource(), rate.getTarget(), rate.getRate()));
            
            // Создание счета
            log.info("Создание счета...");
            Invoice invoice = cryptoPay.createInvoice(
                    CryptoPay.CreateInvoiceParams.builder()
                            .asset("USDT")
                            .amount(new BigDecimal("10.0"))
                            .description("Тестовый счет")
                            .expiresIn(Duration.ofHours(1))
                            .build()
            );
            log.info("Счет создан: ID={}, URL={}", invoice.getInvoiceId(), invoice.getPayUrl());
            
            // Получение информации о счете
            log.info("Получение информации о счете...");
            Invoice retrievedInvoice = cryptoPay.getInvoice(invoice.getInvoiceId());
            log.info("Счет: ID={}, Статус={}", retrievedInvoice.getInvoiceId(), retrievedInvoice.getStatus());
            
            // Создание чека
            log.info("Создание чека...");
            Check check = cryptoPay.createCheck(
                    CryptoPay.CreateCheckParams.builder()
                            .asset("USDT")
                            .amount(new BigDecimal("5.0"))
                            .comment("Тестовый чек")
                            .build()
            );
            log.info("Чек создан: ID={}, URL={}", check.getCheckId(), check.getBotCheckUrl());
            
            // Получение информации о чеке
            log.info("Получение информации о чеке...");
            Check retrievedCheck = cryptoPay.getCheck(check.getCheckId());
            log.info("Чек: ID={}, Статус={}", retrievedCheck.getCheckId(), retrievedCheck.getStatus());
            
            // Пример фильтрации счетов
            log.info("Получение списка активных счетов...");
            List<Invoice> activeInvoices = cryptoPay.getInvoices(
                    CryptoPay.GetInvoicesParams.builder()
                            .status(Arrays.asList("active"))
                            .count(10)
                            .build()
            );
            log.info("Активных счетов: {}", activeInvoices.size());
            
            // Пример фильтрации чеков
            log.info("Получение списка активных чеков...");
            List<Check> activeChecks = cryptoPay.getChecks(
                    CryptoPay.GetChecksParams.builder()
                            .status(Arrays.asList("active"))
                            .count(10)
                            .build()
            );
            log.info("Активных чеков: {}", activeChecks.size());
            
            // Пример получения курса обмена для конкретной пары
            log.info("Получение курса обмена USDT -> USD...");
            Optional<ExchangeRate> usdtToUsd = cryptoPay.getExchangeRate("USDT", "USD");
            usdtToUsd.ifPresentOrElse(
                    rate -> log.info("Курс USDT -> USD: {}", rate.getRate()),
                    () -> log.info("Курс USDT -> USD не найден")
            );
            
            // Пример перевода средств (в реальном приложении лучше не выполнять)
            // log.info("Перевод средств пользователю...");
            // Transfer transfer = cryptoPay.transfer(
            //         123456789L, // ID пользователя Telegram
            //         "USDT", 
            //         new BigDecimal("1.0")
            // );
            // log.info("Перевод выполнен: {}", transfer.getTransferId());
            
        } catch (Exception e) {
            log.error("Ошибка при выполнении примера: {}", e.getMessage(), e);
        }
    }
} 