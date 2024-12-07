package com.challenge.phonemanagement.service.phone;

import com.challenge.phonemanagement.exception.InvalidNumberException;
import com.challenge.phonemanagement.exception.RecordAlreadyExistsException;
import com.challenge.phonemanagement.exception.RecordNotFoundException;
import com.challenge.phonemanagement.model.dto.phone.CreatePhoneDto;
import com.challenge.phonemanagement.model.dto.phone.PhoneDto;
import com.challenge.phonemanagement.model.entity.Phone;
import com.challenge.phonemanagement.repository.PhoneRepository;
import com.challenge.phonemanagement.service.validate.PhoneValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {

    public static final Type PHONE_LIST_TYPE_TOKEN_TYPE = new TypeToken<List<PhoneDto>>() {
    }.getType();

    private final PhoneRepository phoneRepository;
    private final ModelMapper modelMapper;
    private final PhoneValidator phoneValidator;

    public PhoneServiceImpl(PhoneRepository phoneRepository,
                            PhoneValidator phoneValidator,
                            ModelMapper modelMapper) {
        this.phoneRepository = phoneRepository;
        this.phoneValidator = phoneValidator;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PhoneDto> getAll() {
        List<Phone> phones = phoneRepository.findAll();
        return modelMapper.map(phones, PHONE_LIST_TYPE_TOKEN_TYPE);
    }

    @Override
    public PhoneDto create(CreatePhoneDto phone) {
        checkIfNumberAlreadyExists(phone.getNumber());
        checkIfNumberIsValid(phone.getNumber());

        Phone entity = modelMapper.map(phone, Phone.class);
        Phone savedEntity = phoneRepository.save(entity);

        return modelMapper.map(savedEntity, PhoneDto.class);
    }

    private void checkIfNumberIsValid(String number) {
        if (!phoneValidator.isValid(number)) {
            throw new InvalidNumberException("The number " + number + " is invalid!");
        }
    }

    @Override
    public PhoneDto getById(Long id) {
        return phoneRepository.findById(id)
                .map(entity -> modelMapper.map(entity, PhoneDto.class))
                .orElseThrow(() -> new RecordNotFoundException("The id " + id + " was not found!"));
    }

    private void checkIfNumberAlreadyExists(String number) {
        Optional<Phone> optionalNumber = phoneRepository.findByNumber(number);
        if (optionalNumber.isPresent()) {
            throw new RecordAlreadyExistsException("The number " + number + " already exists!");
        }
    }
}
