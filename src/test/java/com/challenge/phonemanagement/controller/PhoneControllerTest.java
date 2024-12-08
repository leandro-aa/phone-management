package com.challenge.phonemanagement.controller;

import com.challenge.phonemanagement.model.dto.phone.CreatePhoneDto;
import com.challenge.phonemanagement.model.dto.phone.PhoneDto;
import com.challenge.phonemanagement.service.phone.PhoneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhoneController.class)
class PhoneControllerTest {
    public static final String PHONE_NUMBER = "number";
    public static final String PHONE_NAME = "name";
    public static final long PHONE_ID = 1L;
    public static final String PHONE_PATH = "/api/v1/phones";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhoneService mockPhoneService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePhone() throws Exception {
        CreatePhoneDto createPhoneDto = new CreatePhoneDto();
        createPhoneDto.setNumber(PHONE_NUMBER);
        createPhoneDto.setName(PHONE_NAME);

        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setId(PHONE_ID);
        phoneDto.setNumber(PHONE_NUMBER);
        phoneDto.setName(PHONE_NAME);

        when(mockPhoneService.create(any(CreatePhoneDto.class))).thenReturn(phoneDto);

        mockMvc.perform(post(PHONE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPhoneDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost" + PHONE_PATH + "/" + PHONE_ID))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testGetAll() throws Exception {
        when(mockPhoneService.getAll()).thenReturn(List.of(buildPhoneDto()));

        mockMvc.perform(get(PHONE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(PHONE_ID))
                .andExpect(jsonPath("$[0].name").value(PHONE_NAME))
                .andExpect(jsonPath("$[0].number").value(PHONE_NUMBER));
    }

    @Test
    void testGetById() throws Exception {
        PhoneDto phoneDto = buildPhoneDto();
        when(mockPhoneService.getById(PHONE_ID)).thenReturn(phoneDto);

        mockMvc.perform(get(PHONE_PATH + "/{id}", phoneDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PHONE_ID))
                .andExpect(jsonPath("$.name").value(PHONE_NAME))
                .andExpect(jsonPath("$.number").value(PHONE_NUMBER));
    }

    private static PhoneDto buildPhoneDto() {
        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setId(PHONE_ID);
        phoneDto.setName(PHONE_NAME);
        phoneDto.setNumber(PHONE_NUMBER);
        return phoneDto;
    }
}
