package com.challenge.phonemanagement.controller;

import com.challenge.phonemanagement.model.dto.phone.CreatePhoneDto;
import com.challenge.phonemanagement.model.dto.phone.PhoneDto;
import com.challenge.phonemanagement.service.phone.PhoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/phones")
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreatePhoneDto phoneDto) {
        PhoneDto createdRecord = phoneService.create(phoneDto);

        //Create resource location
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRecord.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<PhoneDto>> getAll() {
        List<PhoneDto> phones = phoneService.getAll();
        return ResponseEntity.ok(phones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhoneDto> getById(@PathVariable Long id) {
        PhoneDto phoneDto = phoneService.getById(id);
        return ResponseEntity.ok(phoneDto);
    }
}
