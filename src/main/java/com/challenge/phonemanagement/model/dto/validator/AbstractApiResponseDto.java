package com.challenge.phonemanagement.model.dto.validator;

public class AbstractApiResponseDto {
    private String phone;
    private boolean valid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
