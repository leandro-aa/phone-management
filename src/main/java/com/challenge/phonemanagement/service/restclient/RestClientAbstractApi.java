package com.challenge.phonemanagement.service.restclient;

import com.challenge.phonemanagement.configuration.properties.AbstractApiProperties;
import com.challenge.phonemanagement.model.dto.validator.AbstractApiResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RestClientAbstractApi implements AbstractApi {
    private final RestClient restClient;
    private final String apiKey;

    public RestClientAbstractApi(AbstractApiProperties properties, RestClient restClient) {
        this.restClient = restClient;
        this.apiKey = properties.getApiKey();
    }

    public AbstractApiResponseDto phoneValidation(String number) {
        return restClient.get()
                .uri(UriComponentsBuilder.fromUriString("")
                        .queryParam("api_key", apiKey)
                        .queryParam("phone", number)
                        .build()
                        .toUri()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ResponseStatusException(response.getStatusCode(), response.getStatusText());
                })
                .body(AbstractApiResponseDto.class);
    }
}
