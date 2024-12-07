package com.challenge.phonemanagement.service.phone;

import com.challenge.phonemanagement.exception.InvalidNumberException;
import com.challenge.phonemanagement.exception.RecordAlreadyExistsException;
import com.challenge.phonemanagement.exception.RecordNotFoundException;
import com.challenge.phonemanagement.model.dto.phone.CreatePhoneDto;
import com.challenge.phonemanagement.model.dto.phone.PhoneDto;
import com.challenge.phonemanagement.model.entity.Phone;
import com.challenge.phonemanagement.repository.PhoneRepository;
import com.challenge.phonemanagement.service.validate.PhoneValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PhoneServiceImplTest {

    public static final String PHONE_NUMBER = "number";
    public static final String PHONE_NAME = "name";
    public static final long PHONE_ID = 1L;

    private PhoneRepository mockPhoneRepository;
    private PhoneValidator mockPhoneValidator;
    private ModelMapper mockModelMapper;
    private PhoneServiceImpl classUnderTest;

    @BeforeEach
    void setUp() {
        mockPhoneRepository = mock(PhoneRepository.class);
        mockPhoneValidator = mock(PhoneValidator.class);
        mockModelMapper = mock(ModelMapper.class);
        classUnderTest = new PhoneServiceImpl(mockPhoneRepository, mockPhoneValidator, mockModelMapper);
    }

    @Test
    void testGetAll() {
        List<Phone> entityPhones = List.of(buildPhone());
        PhoneDto phoneDto = buildPhoneDto();

        when(mockPhoneRepository.findAll()).thenReturn(entityPhones);
        when(mockModelMapper.map(entityPhones, PhoneServiceImpl.PHONE_LIST_TYPE_TOKEN_TYPE)).thenReturn(List.of(phoneDto));

        List<PhoneDto> result = classUnderTest.getAll();

        assertEquals(entityPhones.size(), result.size());
        verify(mockPhoneRepository).findAll();
        verify(mockModelMapper).map(entityPhones, PhoneServiceImpl.PHONE_LIST_TYPE_TOKEN_TYPE);
    }

    @Nested
    class Create {

        @Test
        void success() {
            CreatePhoneDto createPhoneDto = new CreatePhoneDto();
            createPhoneDto.setNumber(PHONE_NUMBER);
            createPhoneDto.setName(PHONE_NAME);

            Phone phone = new Phone();
            phone.setNumber(PHONE_NUMBER);
            phone.setName(PHONE_NAME);

            Phone savedPhone = buildPhone();
            PhoneDto savedPhoneDto = buildPhoneDto();

            when(mockPhoneRepository.findByNumber(PHONE_NUMBER)).thenReturn(Optional.empty());
            when(mockPhoneValidator.isValid(PHONE_NUMBER)).thenReturn(true);
            when(mockModelMapper.map(createPhoneDto, Phone.class)).thenReturn(phone);
            when(mockPhoneRepository.save(phone)).thenReturn(savedPhone);
            when(mockModelMapper.map(savedPhone, PhoneDto.class)).thenReturn(savedPhoneDto);

            PhoneDto result = classUnderTest.create(createPhoneDto);

            assertEquals(PHONE_ID, result.getId());
            assertEquals(PHONE_NUMBER, result.getNumber());
            assertEquals(PHONE_NAME, result.getName());

            verify(mockPhoneRepository).findByNumber(PHONE_NUMBER);
            verify(mockPhoneValidator).isValid(PHONE_NUMBER);
            verify(mockPhoneRepository).save(phone);
        }

        @Test
        void phoneAlreadyExists() {
            CreatePhoneDto createPhoneDto = new CreatePhoneDto();
            createPhoneDto.setNumber(PHONE_NUMBER);
            createPhoneDto.setName(PHONE_NAME);

            Phone existingPhone = buildPhone();

            when(mockPhoneRepository.findByNumber(PHONE_NUMBER)).thenReturn(Optional.of(existingPhone));

            RecordAlreadyExistsException exception = assertThrows(RecordAlreadyExistsException.class, () -> classUnderTest.create(createPhoneDto));
            assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());

            verify(mockPhoneRepository).findByNumber(PHONE_NUMBER);
            verifyNoInteractions(mockPhoneValidator);
            verifyNoInteractions(mockModelMapper);
        }

        @Test
        void invalidPhoneNumber() {
            CreatePhoneDto createPhoneDto = new CreatePhoneDto();
            createPhoneDto.setNumber(PHONE_NUMBER);
            createPhoneDto.setName(PHONE_NAME);

            when(mockPhoneRepository.findByNumber(PHONE_NUMBER)).thenReturn(Optional.empty());
            when(mockPhoneValidator.isValid(PHONE_NUMBER)).thenReturn(false);

            InvalidNumberException exception = assertThrows(InvalidNumberException.class, () -> classUnderTest.create(createPhoneDto));
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

            verify(mockPhoneRepository).findByNumber(PHONE_NUMBER);
            verify(mockPhoneValidator).isValid(PHONE_NUMBER);
            verifyNoInteractions(mockModelMapper);
        }
    }

    @Nested
    class GetById {
        @Test
        void found() {
            Phone phone = buildPhone();
            PhoneDto phoneDto = buildPhoneDto();

            when(mockPhoneRepository.findById(PHONE_ID)).thenReturn(Optional.of(phone));
            when(mockModelMapper.map(phone, PhoneDto.class)).thenReturn(phoneDto);

            PhoneDto result = classUnderTest.getById(PHONE_ID);

            assertEquals(PHONE_ID, result.getId());
            assertEquals(PHONE_NAME, result.getName());
            assertEquals(PHONE_NUMBER, result.getNumber());
            verify(mockPhoneRepository).findById(PHONE_ID);
            verify(mockModelMapper).map(phone, PhoneDto.class);
        }

        @Test
        void notFound() {
            when(mockPhoneRepository.findById(PHONE_ID)).thenReturn(Optional.empty());

            Executable executable = () -> classUnderTest.getById(PHONE_ID);

            RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, executable);
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            verify(mockPhoneRepository).findById(PHONE_ID);
        }
    }

    private static PhoneDto buildPhoneDto() {
        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setId(PHONE_ID);
        phoneDto.setName(PHONE_NAME);
        phoneDto.setNumber(PHONE_NUMBER);
        return phoneDto;
    }

    private static Phone buildPhone() {
        Phone phone = new Phone();
        phone.setName(PHONE_NAME);
        phone.setNumber(PHONE_NUMBER);

        ReflectionTestUtils.setField(phone, "id", PHONE_ID);
        return phone;
    }
}
