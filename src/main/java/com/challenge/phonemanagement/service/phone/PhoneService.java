package com.challenge.phonemanagement.service.phone;

import com.challenge.phonemanagement.model.dto.phone.CreatePhoneDto;
import com.challenge.phonemanagement.model.dto.phone.PhoneDto;

import java.util.List;

public interface PhoneService {

    List<PhoneDto> getAll();

    PhoneDto create(CreatePhoneDto phone);

    PhoneDto getById(Long id);
}
