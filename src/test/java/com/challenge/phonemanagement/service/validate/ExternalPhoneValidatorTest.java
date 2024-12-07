package com.challenge.phonemanagement.service.validate;

import com.challenge.phonemanagement.model.dto.validator.AbstractApiResponseDto;
import com.challenge.phonemanagement.service.restclient.AbstractApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExternalPhoneValidatorTest {
    public static final String PHONE_NUMBER = "number";
    private AbstractApi mockAbstractApi;
    private ExternalPhoneValidator classUnderTest;

    @BeforeEach
    void setUp() {
        mockAbstractApi = mock(AbstractApi.class);
        classUnderTest = new ExternalPhoneValidator(mockAbstractApi);
    }

    @ParameterizedTest
    @MethodSource
    void isValid(AbstractApiResponseDto apiResponse, boolean expected) {
        when(mockAbstractApi.phoneValidation(PHONE_NUMBER)).thenReturn(apiResponse);

        boolean result = classUnderTest.isValid(PHONE_NUMBER);
        assertEquals(expected, result);

        verify(mockAbstractApi).phoneValidation(PHONE_NUMBER);
    }

    public static Stream<Arguments> isValid() {
        AbstractApiResponseDto validResponse = new AbstractApiResponseDto();
        validResponse.setValid(true);

        AbstractApiResponseDto invalidResponse = new AbstractApiResponseDto();
        invalidResponse.setValid(false);

        return Stream.of(
                Arguments.of(validResponse, true),
                Arguments.of(invalidResponse, false),
                Arguments.of(null, false)
        );
    }
}
