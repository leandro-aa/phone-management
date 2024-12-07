package com.challenge.phonemanagement.repository;

import com.challenge.phonemanagement.model.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByNumber(String number);

}
