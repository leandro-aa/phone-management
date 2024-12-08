package com.challenge.phonemanagement.controller;

import com.challenge.phonemanagement.model.dto.phone.CreatePhoneDto;
import com.challenge.phonemanagement.model.dto.phone.PhoneDto;
import com.challenge.phonemanagement.service.phone.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Phone")
@RestController
@RequestMapping("/api/v1/phones")
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @Operation(summary = "Create phone record", responses = {
            @ApiResponse(description = "Successful operation", responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "400", description = "The number is invalid", content = @Content),
            @ApiResponse(responseCode = "409", description = "The phone number already exists", content = @Content)})
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePhoneDto phoneDto) {
        PhoneDto createdRecord = phoneService.create(phoneDto);

        //Create resource location
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRecord.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Get All phone records")
    @GetMapping
    public ResponseEntity<List<PhoneDto>> getAll() {
        List<PhoneDto> phones = phoneService.getAll();
        return ResponseEntity.ok(phones);
    }

    @Operation(summary = "Get a phone records by ID", responses = {
            @ApiResponse(description = "Successful operation", responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Phone number not found for the given Id", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<PhoneDto> getById(@PathVariable Long id) {
        PhoneDto phoneDto = phoneService.getById(id);
        return ResponseEntity.ok(phoneDto);
    }
}
