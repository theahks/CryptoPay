package com.cryptopay.api;

import com.cryptopay.exception.CryptoPayApiException;
import com.cryptopay.model.*;
import com.cryptopay.util.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Основной класс для работы с API CryptoBot.
 */
@Slf4j
public class CryptoPay {
    private static final String DEFAULT_BASE_URL = "https://pay.crypt.bot/api/";
    
    private final HttpClient httpClient;
    
    @Getter
    private final String apiToken;
    
    @Getter
    private final String hostUrl;
    
    private final Cache<String, List<Asset>> assetsCache;
    
    /**
     * Создает экземпляр CryptoPay с указанным токеном API и базовым URL.
     * 
     * @param apiToken токен API
     * @param hostUrl базовый URL API (по умолчанию "https://pay.crypt.bot/api/")
     */
    @Builder
    public CryptoPay(String apiToken, String hostUrl) {
        Preconditions.checkNotNull(apiToken, "API токен не может быть null");
        
        this.apiToken = apiToken;
        this.hostUrl = hostUrl != null ? hostUrl : DEFAULT_BASE_URL;
        this.httpClient = new HttpClient(apiToken, this.hostUrl);
        
        // Настраиваем кэш активов
        this.assetsCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }
    
    /**
     * Получает информацию о боте.
     * 
     * @return информация о боте
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Map<String, Object> getMe() {
        TypeReference<ApiResponse<Map<String, Object>>> typeRef = new TypeReference<ApiResponse<Map<String, Object>>>() {};
        ApiResponse<Map<String, Object>> response = httpClient.get("getMe", null, typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Создает новый счет для оплаты.
     * 
     * @param asset код криптовалюты
     * @param amount сумма счета
     * @param description описание счета
     * @return созданный счет
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Invoice createInvoice(String asset, BigDecimal amount, String description) {
        return createInvoice(CreateInvoiceParams.builder()
                .asset(asset)
                .amount(amount)
                .description(description)
                .build());
    }
    
    /**
     * Создает новый счет для оплаты.
     * 
     * @param params параметры создания счета
     * @return созданный счет
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Invoice createInvoice(CreateInvoiceParams params) {
        TypeReference<ApiResponse<Invoice>> typeRef = new TypeReference<ApiResponse<Invoice>>() {};
        ApiResponse<Invoice> response = httpClient.post("createInvoice", params, typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Получает информацию о счете по его идентификатору.
     * 
     * @param invoiceId идентификатор счета
     * @return информация о счете
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Invoice getInvoice(long invoiceId) {
        Map<String, String> params = ImmutableMap.of("invoice_id", String.valueOf(invoiceId));
        
        TypeReference<ApiResponse<Invoice>> typeRef = new TypeReference<ApiResponse<Invoice>>() {};
        ApiResponse<Invoice> response = httpClient.get("getInvoice", params, typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Получает список счетов с учетом фильтров.
     * 
     * @param params параметры фильтрации счетов
     * @return список счетов
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public List<Invoice> getInvoices(GetInvoicesParams params) {
        TypeReference<ApiResponse<List<Invoice>>> typeRef = new TypeReference<ApiResponse<List<Invoice>>>() {};
        ApiResponse<List<Invoice>> response = httpClient.get("getInvoices", params.toQueryParams(), typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Создает ссылку на чек.
     * 
     * @param asset код криптовалюты
     * @param amount сумма чека
     * @return созданный чек
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Check createCheck(String asset, BigDecimal amount) {
        return createCheck(CreateCheckParams.builder()
                .asset(asset)
                .amount(amount)
                .build());
    }
    
    /**
     * Создает ссылку на чек.
     * 
     * @param params параметры создания чека
     * @return созданный чек
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Check createCheck(CreateCheckParams params) {
        TypeReference<ApiResponse<Check>> typeRef = new TypeReference<ApiResponse<Check>>() {};
        ApiResponse<Check> response = httpClient.post("createCheck", params, typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Получает информацию о чеке по его идентификатору.
     * 
     * @param checkId идентификатор чека
     * @return информация о чеке
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Check getCheck(long checkId) {
        Map<String, String> params = ImmutableMap.of("check_id", String.valueOf(checkId));
        
        TypeReference<ApiResponse<Check>> typeRef = new TypeReference<ApiResponse<Check>>() {};
        ApiResponse<Check> response = httpClient.get("getCheck", params, typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Получает список чеков с учетом фильтров.
     * 
     * @param params параметры фильтрации чеков
     * @return список чеков
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public List<Check> getChecks(GetChecksParams params) {
        TypeReference<ApiResponse<List<Check>>> typeRef = new TypeReference<ApiResponse<List<Check>>>() {};
        ApiResponse<List<Check>> response = httpClient.get("getChecks", params.toQueryParams(), typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Переводит средства пользователю Telegram.
     * 
     * @param userId идентификатор пользователя Telegram
     * @param asset код криптовалюты
     * @param amount сумма перевода
     * @return информация о переводе
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Transfer transfer(long userId, String asset, BigDecimal amount) {
        return transfer(TransferParams.builder()
                .userId(userId)
                .asset(asset)
                .amount(amount)
                .build());
    }
    
    /**
     * Переводит средства пользователю Telegram.
     * 
     * @param params параметры перевода
     * @return информация о переводе
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Transfer transfer(TransferParams params) {
        TypeReference<ApiResponse<Transfer>> typeRef = new TypeReference<ApiResponse<Transfer>>() {};
        ApiResponse<Transfer> response = httpClient.post("transfer", params, typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Получает список переводов с учетом фильтров.
     * 
     * @param params параметры фильтрации переводов
     * @return список переводов
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public List<Transfer> getTransfers(GetTransfersParams params) {
        TypeReference<ApiResponse<List<Transfer>>> typeRef = new TypeReference<ApiResponse<List<Transfer>>>() {};
        ApiResponse<List<Transfer>> response = httpClient.get("getTransfers", params.toQueryParams(), typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Получает баланс криптовалютного кошелька приложения.
     * 
     * @return список балансов по разным криптовалютам
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public List<Balance> getBalance() {
        TypeReference<ApiResponse<List<Balance>>> typeRef = new TypeReference<ApiResponse<List<Balance>>>() {};
        ApiResponse<List<Balance>> response = httpClient.get("getBalance", null, typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Получает текущие обменные курсы для заданной исходной валюты.
     * 
     * @param source код исходной валюты
     * @return список обменных курсов
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public List<ExchangeRate> getExchangeRates(String source) {
        Map<String, String> params = ImmutableMap.of("source", source);
        
        TypeReference<ApiResponse<List<ExchangeRate>>> typeRef = new TypeReference<ApiResponse<List<ExchangeRate>>>() {};
        ApiResponse<List<ExchangeRate>> response = httpClient.get("getExchangeRates", params, typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Получает все текущие обменные курсы.
     * 
     * @return список обменных курсов
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public List<ExchangeRate> getAllExchangeRates() {
        TypeReference<ApiResponse<List<ExchangeRate>>> typeRef = new TypeReference<ApiResponse<List<ExchangeRate>>>() {};
        ApiResponse<List<ExchangeRate>> response = httpClient.get("getExchangeRates", null, typeRef);
        
        checkResponse(response);
        return response.getResult();
    }
    
    /**
     * Получает курс обмена для пары валют.
     * 
     * @param source код исходной валюты
     * @param target код целевой валюты
     * @return курс обмена или null, если курс не найден
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public Optional<ExchangeRate> getExchangeRate(String source, String target) {
        return getAllExchangeRates().stream()
                .filter(rate -> rate.getSource().equalsIgnoreCase(source) && rate.getTarget().equalsIgnoreCase(target))
                .findFirst();
    }
    
    /**
     * Получает информацию о поддерживаемых криптовалютах и их ограничениях.
     * 
     * @return список поддерживаемых активов
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public List<Asset> getCurrencies() {
        return getAssets();
    }
    
    /**
     * Получает информацию о поддерживаемых криптовалютах и их ограничениях.
     * Результат кэшируется на 1 час.
     * 
     * @return список поддерживаемых активов
     * @throws CryptoPayApiException если произошла ошибка при выполнении запроса
     */
    public List<Asset> getAssets() {
        return assetsCache.get("assets", key -> {
            TypeReference<ApiResponse<List<Asset>>> typeRef = new TypeReference<ApiResponse<List<Asset>>>() {};
            ApiResponse<List<Asset>> response = httpClient.get("getCurrencies", null, typeRef);
            
            checkResponse(response);
            return response.getResult();
        });
    }
    
    /**
     * Проверяет ответ API на наличие ошибок.
     * 
     * @param response ответ API
     * @param <T> тип результата ответа
     * @throws CryptoPayApiException если ответ содержит ошибку
     */
    private <T> void checkResponse(ApiResponse<T> response) {
        if (!response.isSuccess()) {
            log.error("Ошибка API: {} (код: {})", response.getDescription(), response.getErrorCode());
            throw new CryptoPayApiException(response.getDescription(), response.getErrorCode());
        }
    }
    
    /**
     * Параметры для создания счета.
     */
    @Getter
    @Builder
    public static class CreateInvoiceParams {
        /**
         * Код криптовалюты.
         */
        private final String asset;
        
        /**
         * Сумма счета.
         */
        private final BigDecimal amount;
        
        /**
         * Описание счета.
         */
        private final String description;
        
        /**
         * Валюта фиатных денег.
         */
        @Builder.Default
        private final String fiatCurrency = null;
        
        /**
         * Сумма фиатных денег.
         */
        @Builder.Default
        private final BigDecimal fiatAmount = null;
        
        /**
         * Срок действия счета в секундах.
         */
        @Builder.Default
        private final Duration expiresIn = null;
        
        /**
         * Разрешенные методы оплаты.
         */
        @Builder.Default
        private final List<String> allowedPaymentMethods = null;
    }
    
    /**
     * Параметры для создания чека.
     */
    @Getter
    @Builder
    public static class CreateCheckParams {
        /**
         * Код криптовалюты.
         */
        private final String asset;
        
        /**
         * Сумма чека.
         */
        private final BigDecimal amount;
        
        /**
         * Комментарий к чеку.
         */
        @Builder.Default
        private final String comment = null;
    }
    
    /**
     * Параметры для перевода средств.
     */
    @Getter
    @Builder
    public static class TransferParams {
        /**
         * Идентификатор пользователя Telegram.
         */
        private final Long userId;
        
        /**
         * Код криптовалюты.
         */
        private final String asset;
        
        /**
         * Сумма перевода.
         */
        private final BigDecimal amount;
        
        /**
         * Комментарий к переводу.
         */
        @Builder.Default
        private final String comment = null;
    }
    
    /**
     * Базовые параметры фильтрации для запросов списков.
     */
    @Getter
    public abstract static class BaseFilterParams {
        /**
         * Смещение списка.
         */
        protected Integer offset = null;
        
        /**
         * Количество элементов в списке.
         */
        protected Integer count = null;
        
        /**
         * Преобразует параметры в карту для запроса.
         * 
         * @return карта параметров
         */
        public Map<String, String> toQueryParams() {
            Map<String, String> params = new HashMap<>();
            
            if (offset != null) {
                params.put("offset", offset.toString());
            }
            
            if (count != null) {
                params.put("count", count.toString());
            }
            
            return params;
        }
    }
    
    /**
     * Параметры для получения списка счетов.
     */
    @Getter
    public static class GetInvoicesParams extends BaseFilterParams {
        /**
         * Статусы инвойсов.
         */
        private List<String> status = null;
        
        /**
         * Валюта инвойса.
         */
        private String asset = null;
        
        /**
         * Идентификатор инвойса.
         */
        private Long invoiceId = null;
        
        /**
         * Построитель для GetInvoicesParams.
         */
        public static GetInvoicesParamsBuilder builder() {
            return new GetInvoicesParamsBuilder();
        }
        
        /**
         * Преобразует параметры в карту для запроса.
         * 
         * @return карта параметров
         */
        @Override
        public Map<String, String> toQueryParams() {
            Map<String, String> params = super.toQueryParams();
            
            if (status != null && !status.isEmpty()) {
                params.put("status", String.join(",", status));
            }
            
            if (asset != null) {
                params.put("asset", asset);
            }
            
            if (invoiceId != null) {
                params.put("invoice_id", invoiceId.toString());
            }
            
            return params;
        }
        
        /**
         * Построитель для GetInvoicesParams.
         */
        public static class GetInvoicesParamsBuilder {
            private Integer offset;
            private Integer count;
            private List<String> status;
            private String asset;
            private Long invoiceId;
            
            /**
             * Устанавливает смещение списка.
             * 
             * @param offset смещение
             * @return построитель
             */
            public GetInvoicesParamsBuilder offset(Integer offset) {
                this.offset = offset;
                return this;
            }
            
            /**
             * Устанавливает количество элементов в списке.
             * 
             * @param count количество
             * @return построитель
             */
            public GetInvoicesParamsBuilder count(Integer count) {
                this.count = count;
                return this;
            }
            
            /**
             * Устанавливает статусы счетов.
             * 
             * @param status статусы
             * @return построитель
             */
            public GetInvoicesParamsBuilder status(List<String> status) {
                this.status = status;
                return this;
            }
            
            /**
             * Устанавливает код криптовалюты.
             * 
             * @param asset код криптовалюты
             * @return построитель
             */
            public GetInvoicesParamsBuilder asset(String asset) {
                this.asset = asset;
                return this;
            }
            
            /**
             * Устанавливает идентификатор счета для получения счетов с идентификатором меньше указанного.
             * 
             * @param invoiceId идентификатор счета
             * @return построитель
             */
            public GetInvoicesParamsBuilder invoiceId(Long invoiceId) {
                this.invoiceId = invoiceId;
                return this;
            }
            
            /**
             * Создает GetInvoicesParams.
             * 
             * @return GetInvoicesParams
             */
            public GetInvoicesParams build() {
                GetInvoicesParams params = new GetInvoicesParams();
                params.offset = this.offset;
                params.count = this.count;
                params.status = this.status;
                params.asset = this.asset;
                params.invoiceId = this.invoiceId;
                return params;
            }
        }
    }
    
    /**
     * Параметры для получения списка чеков.
     */
    @Getter
    public static class GetChecksParams extends BaseFilterParams {
        /**
         * Статусы чеков.
         */
        private List<String> status = null;
        
        /**
         * Валюта чека.
         */
        private String asset = null;
        
        /**
         * Идентификатор чека.
         */
        private Long checkId = null;
        
        /**
         * Построитель для GetChecksParams.
         */
        public static GetChecksParamsBuilder builder() {
            return new GetChecksParamsBuilder();
        }
        
        /**
         * Преобразует параметры в карту для запроса.
         * 
         * @return карта параметров
         */
        @Override
        public Map<String, String> toQueryParams() {
            Map<String, String> params = super.toQueryParams();
            
            if (status != null && !status.isEmpty()) {
                params.put("status", String.join(",", status));
            }
            
            if (asset != null) {
                params.put("asset", asset);
            }
            
            if (checkId != null) {
                params.put("check_id", checkId.toString());
            }
            
            return params;
        }
        
        /**
         * Построитель для GetChecksParams.
         */
        public static class GetChecksParamsBuilder {
            private Integer offset;
            private Integer count;
            private List<String> status;
            private String asset;
            private Long checkId;
            
            /**
             * Устанавливает смещение списка.
             * 
             * @param offset смещение
             * @return построитель
             */
            public GetChecksParamsBuilder offset(Integer offset) {
                this.offset = offset;
                return this;
            }
            
            /**
             * Устанавливает количество элементов в списке.
             * 
             * @param count количество
             * @return построитель
             */
            public GetChecksParamsBuilder count(Integer count) {
                this.count = count;
                return this;
            }
            
            /**
             * Устанавливает статусы чеков.
             * 
             * @param status статусы
             * @return построитель
             */
            public GetChecksParamsBuilder status(List<String> status) {
                this.status = status;
                return this;
            }
            
            /**
             * Устанавливает код криптовалюты.
             * 
             * @param asset код криптовалюты
             * @return построитель
             */
            public GetChecksParamsBuilder asset(String asset) {
                this.asset = asset;
                return this;
            }
            
            /**
             * Устанавливает идентификатор чека для получения чеков с идентификатором меньше указанного.
             * 
             * @param checkId идентификатор чека
             * @return построитель
             */
            public GetChecksParamsBuilder checkId(Long checkId) {
                this.checkId = checkId;
                return this;
            }
            
            /**
             * Создает GetChecksParams.
             * 
             * @return GetChecksParams
             */
            public GetChecksParams build() {
                GetChecksParams params = new GetChecksParams();
                params.offset = this.offset;
                params.count = this.count;
                params.status = this.status;
                params.asset = this.asset;
                params.checkId = this.checkId;
                return params;
            }
        }
    }
    
    /**
     * Параметры для получения списка переводов.
     */
    @Getter
    public static class GetTransfersParams extends BaseFilterParams {
        /**
         * Валюта перевода.
         */
        private String asset = null;
        
        /**
         * Идентификатор перевода.
         */
        private Long transferId = null;
        
        /**
         * Построитель для GetTransfersParams.
         */
        public static GetTransfersParamsBuilder builder() {
            return new GetTransfersParamsBuilder();
        }
        
        /**
         * Преобразует параметры в карту для запроса.
         * 
         * @return карта параметров
         */
        @Override
        public Map<String, String> toQueryParams() {
            Map<String, String> params = super.toQueryParams();
            
            if (asset != null) {
                params.put("asset", asset);
            }
            
            if (transferId != null) {
                params.put("transfer_id", transferId.toString());
            }
            
            return params;
        }
        
        /**
         * Построитель для GetTransfersParams.
         */
        public static class GetTransfersParamsBuilder {
            private Integer offset;
            private Integer count;
            private String asset;
            private Long transferId;
            
            /**
             * Устанавливает смещение списка.
             * 
             * @param offset смещение
             * @return построитель
             */
            public GetTransfersParamsBuilder offset(Integer offset) {
                this.offset = offset;
                return this;
            }
            
            /**
             * Устанавливает количество элементов в списке.
             * 
             * @param count количество
             * @return построитель
             */
            public GetTransfersParamsBuilder count(Integer count) {
                this.count = count;
                return this;
            }
            
            /**
             * Устанавливает код криптовалюты.
             * 
             * @param asset код криптовалюты
             * @return построитель
             */
            public GetTransfersParamsBuilder asset(String asset) {
                this.asset = asset;
                return this;
            }
            
            /**
             * Устанавливает идентификатор перевода для получения переводов с идентификатором меньше указанного.
             * 
             * @param transferId идентификатор перевода
             * @return построитель
             */
            public GetTransfersParamsBuilder transferId(Long transferId) {
                this.transferId = transferId;
                return this;
            }
            
            /**
             * Создает GetTransfersParams.
             * 
             * @return GetTransfersParams
             */
            public GetTransfersParams build() {
                GetTransfersParams params = new GetTransfersParams();
                params.offset = this.offset;
                params.count = this.count;
                params.asset = this.asset;
                params.transferId = this.transferId;
                return params;
            }
        }
    }
} 