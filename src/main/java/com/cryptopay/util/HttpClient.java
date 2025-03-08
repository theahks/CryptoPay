package com.cryptopay.util;

import com.cryptopay.exception.CryptoPayApiException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Утилитный класс для выполнения HTTP-запросов к CryptoBot API.
 */
@Slf4j
public class HttpClient {
    private final CloseableHttpClient httpClient;
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
        this.httpClient = HttpClients.createDefault();
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
        try {
            URIBuilder uriBuilder = new URIBuilder(baseUrl + method);
            
            if (params != null && !params.isEmpty()) {
                params.forEach(uriBuilder::addParameter);
            }
            
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            addAuthHeader(httpGet);
            
            return executeRequest(httpGet, typeReference);
        } catch (URISyntaxException e) {
            throw new CryptoPayApiException("Неверный URL: " + baseUrl + method, e);
        }
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
            URI uri = new URI(baseUrl + method);
            HttpPost httpPost = new HttpPost(uri);
            addAuthHeader(httpPost);
            
            if (body != null) {
                String jsonBody = objectMapper.writeValueAsString(body);
                StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);
            }
            
            return executeRequest(httpPost, typeReference);
        } catch (URISyntaxException e) {
            throw new CryptoPayApiException("Неверный URL: " + baseUrl + method, e);
        } catch (IOException e) {
            throw new CryptoPayApiException("Ошибка при сериализации тела запроса", e);
        }
    }

    /**
     * Добавляет заголовок авторизации к запросу.
     *
     * @param request запрос
     */
    private void addAuthHeader(HttpRequestBase request) {
        request.addHeader("Crypto-Pay-API-Token", apiToken);
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
    private <T> T executeRequest(HttpRequestBase request, TypeReference<T> typeReference) {
        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
                String responseBody = EntityUtils.toString(entity);
                log.debug("Ответ API: {}", responseBody);
                
                try {
                    return objectMapper.readValue(responseBody, typeReference);
                } catch (IOException e) {
                    throw new CryptoPayApiException("Ошибка при десериализации ответа: " + responseBody, e);
                }
            } else {
                throw new CryptoPayApiException("Пустой ответ от сервера", null);
            }
        } catch (IOException e) {
            throw new CryptoPayApiException("Ошибка при выполнении HTTP-запроса", e);
        }
    }
} 