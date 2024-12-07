package com.challenge.phonemanagement.service.restclient;

import com.challenge.phonemanagement.model.dto.validator.AbstractApiResponseDto;

public interface AbstractApi {
    AbstractApiResponseDto phoneValidation(String number);
}
