package com.challenge.phonemanagement.service.validate;

import com.challenge.phonemanagement.model.dto.validator.AbstractApiResponseDto;
import com.challenge.phonemanagement.service.restclient.AbstractApi;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExternalPhoneValidator implements PhoneValidator {
    private final AbstractApi abstractApi;

    public ExternalPhoneValidator(AbstractApi abstractApi) {
        this.abstractApi = abstractApi;
    }

    @Override
    public boolean isValid(String number) {
        return Optional.ofNullable(abstractApi.phoneValidation(number))
                .map(AbstractApiResponseDto::isValid)
                .orElse(false);
    }
}
