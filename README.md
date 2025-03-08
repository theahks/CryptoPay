[![](https://jitpack.io/v/theahks/CryptoPay.svg)](https://jitpack.io/#theahks/CryptoPay)

# CryptoPay Java SDK

Java SDK для работы с API CryptoBot ([@CryptoBot](https://t.me/CryptoBot)).

## Описание

Эта библиотека предоставляет удобный интерфейс для взаимодействия с API CryptoBot в Java-проектах. Она позволяет:

- Создавать счета и чеки для оплаты криптовалютой
- Переводить средства между пользователями
- Проверять статус платежей
- Получать информацию о балансах и обменных курсах
- И многое другое


## Начало работы

Для начала использования библиотеки вам необходимо получить API-токен у [@CryptoBot](https://t.me/CryptoBot).

```java
import com.cryptopay.api.CryptoPay;
import com.cryptopay.model.Invoice;
import java.math.BigDecimal;

public class Example {
    public static void main(String[] args) {
        // Инициализация клиента
        CryptoPay cryptoPay = CryptoPay.builder()
                .apiToken("YOUR_API_TOKEN")
                .build();
        
        // Создание счета для оплаты
        Invoice invoice = cryptoPay.createInvoice(
                "USDT", 
                new BigDecimal("10.0"), 
                "Оплата услуг"
        );
        
        System.out.println("Создан счет: " + invoice.getInvoiceId());
        System.out.println("URL для оплаты: " + invoice.getPayUrl());
    }
}
```

## Примеры использования

### Создание счета с дополнительными параметрами

```java
import com.cryptopay.api.CryptoPay;
import com.cryptopay.model.Invoice;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;

public class InvoiceExample {
    public static void main(String[] args) {
        CryptoPay cryptoPay = CryptoPay.builder()
                .apiToken("YOUR_API_TOKEN")
                .build();
        
        // Создание счета с дополнительными параметрами
        Invoice invoice = cryptoPay.createInvoice(
                CryptoPay.CreateInvoiceParams.builder()
                        .asset("BTC")
                        .amount(new BigDecimal("0.001"))
                        .description("Премиум подписка")
                        .expiresIn(Duration.ofHours(1))  // Срок действия 1 час
                        .allowedPaymentMethods(Arrays.asList("BTC"))
                        .build()
        );
        
        System.out.println("Создан счет: " + invoice.getInvoiceId());
    }
}
```

### Создание чека

```java
import com.cryptopay.api.CryptoPay;
import com.cryptopay.model.Check;
import java.math.BigDecimal;

public class CheckExample {
    public static void main(String[] args) {
        CryptoPay cryptoPay = CryptoPay.builder()
                .apiToken("YOUR_API_TOKEN")
                .build();
        
        // Создание чека
        Check check = cryptoPay.createCheck(
                "TON", 
                new BigDecimal("1.0")
        );
        
        System.out.println("Создан чек: " + check.getCheckId());
        System.out.println("URL для активации: " + check.getBotCheckUrl());
    }
}
```

### Перевод средств

```java
import com.cryptopay.api.CryptoPay;
import com.cryptopay.model.Transfer;
import java.math.BigDecimal;

public class TransferExample {
    public static void main(String[] args) {
        CryptoPay cryptoPay = CryptoPay.builder()
                .apiToken("YOUR_API_TOKEN")
                .build();
        
        // Перевод средств пользователю Telegram
        Transfer transfer = cryptoPay.transfer(
                123456789L,  // ID пользователя Telegram
                "USDT", 
                new BigDecimal("5.0")
        );
        
        System.out.println("Перевод выполнен: " + transfer.getTransferId());
    }
}
```

### Получение баланса

```java
import com.cryptopay.api.CryptoPay;
import com.cryptopay.model.Balance;
import java.util.List;

public class BalanceExample {
    public static void main(String[] args) {
        CryptoPay cryptoPay = CryptoPay.builder()
                .apiToken("YOUR_API_TOKEN")
                .build();
        
        // Получение баланса
        List<Balance> balances = cryptoPay.getBalance();
        
        for (Balance balance : balances) {
            System.out.println(balance.getCurrencyCode() + ": " + 
                    balance.getAvailable() + " (доступно), " + 
                    balance.getOnhold() + " (заблокировано)");
        }
    }
}
```

### Получение обменных курсов

```java
import com.cryptopay.api.CryptoPay;
import com.cryptopay.model.ExchangeRate;
import java.util.List;

public class ExchangeRateExample {
    public static void main(String[] args) {
        CryptoPay cryptoPay = CryptoPay.builder()
                .apiToken("YOUR_API_TOKEN")
                .build();
        
        // Получение обменных курсов для BTC
        List<ExchangeRate> rates = cryptoPay.getExchangeRates("BTC");
        
        for (ExchangeRate rate : rates) {
            System.out.println("Курс " + rate.getSource() + " к " + 
                    rate.getTarget() + ": " + rate.getRate());
        }
    }
}
```

## Документация

Подробная документация API CryptoBot доступна по адресу: [https://help.crypt.bot/crypto-pay-api](https://help.crypt.bot/crypto-pay-api)

## Лицензия

Этот проект распространяется под лицензией MIT. Подробнее см. файл LICENSE. 
