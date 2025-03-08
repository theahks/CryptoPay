package com.cryptopay.util;

import com.cryptopay.exception.CryptoPayApiException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Утилитный класс для выполнения HTTP-запросов к CryptoBot API.
 */
@Slf4j
public class HttpClient {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final int TIMEOUT_SECONDS = 30;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiToken;
    private final String baseUrl;

    /**
     * Создает экземпляр HttpClient с заданным API токеном и базовым URL.
     *
     * @param apiToken токен API
     * @param baseUrl базовый URL API
     */
    public HttpClient(String apiToken, String baseUrl) {
        Preconditions.checkNotNull(apiToken, "API токен не может быть null");
        Preconditions.checkNotNull(baseUrl, "Базовый URL не может быть null");
        
        this.apiToken = apiToken;
        this.baseUrl = baseUrl;
        
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
                
        this.objectMapper = new ObjectMapper();
        JsonUtils.configureObjectMapper(this.objectMapper);
    }

    /**
     * Выполняет GET-запрос к указанному методу API.
     *
     * @param method имя метода API
     * @param params параметры запроса
     * @param typeReference ссылка на тип возвращаемого результата
     * @param <T> тип возвращаемого результата
     * @return результат запроса
     * @throws CryptoPayApiException если возникла ошибка при выполнении запроса
     */
    public <T> T get(String method, Map<String, String> params, TypeReference<T> typeReference) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl + method)).newBuilder();
        
        if (params != null && !params.isEmpty()) {
            params.forEach(urlBuilder::addQueryParameter);
        }
        
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header("Crypto-Pay-API-Token", apiToken)
                .get()
                .build();
        
        return executeRequest(request, typeReference);
    }

    /**
     * Выполняет POST-запрос к указанному методу API.
     *
     * @param method имя метода API
     * @param body тело запроса
     * @param typeReference ссылка на тип возвращаемого результата
     * @param <T> тип возвращаемого результата
     * @return результат запроса
     * @throws CryptoPayApiException если возникла ошибка при выполнении запроса
     */
    public <T> T post(String method, Object body, TypeReference<T> typeReference) {
        try {
            String url = baseUrl + method;
            
            RequestBody requestBody = null;
            if (body != null) {
                String jsonBody = objectMapper.writeValueAsString(body);
                requestBody = RequestBody.create(jsonBody, JSON);
            } else {
                requestBody = RequestBody.create("", null);
            }
            
            Request request = new Request.Builder()
                    .url(url)
                    .header("Crypto-Pay-API-Token", apiToken)
                    .post(requestBody)
                    .build();
            
            return executeRequest(request, typeReference);
        } catch (IOException e) {
            throw new CryptoPayApiException("Ошибка при сериализации тела запроса", e);
        }
    }

    /**
     * Выполняет HTTP-запрос и обрабатывает ответ.
     *
     * @param request HTTP-запрос
     * @param typeReference ссылка на тип возвращаемого результата
     * @param <T> тип возвращаемого результата
     * @return результат запроса
     * @throws CryptoPayApiException если возникла ошибка при выполнении запроса
     */
    private <T> T executeRequest(Request request, TypeReference<T> typeReference) {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CryptoPayApiException(
                        "Ошибка HTTP запроса: " + response.code() + " " + response.message(), null, null);
            }
            
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new CryptoPayApiException("Пустой ответ от сервера", null, null);
            }
            
            String responseString = responseBody.string();
            log.debug("Ответ API: {}", responseString);
            
            try {
                return objectMapper.readValue(responseString, typeReference);
            } catch (IOException e) {
                throw new CryptoPayApiException("Ошибка при десериализации ответа: " + responseString, e);
            }
        } catch (IOException e) {
            throw new CryptoPayApiException("Ошибка при выполнении HTTP-запроса", e);
        }
    }
} 