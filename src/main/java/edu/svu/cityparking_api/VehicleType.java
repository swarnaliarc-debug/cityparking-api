package edu.svu.cityparking_api;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VehicleType {
    TWO_WHEELER("Two wheeler"),
    FOUR_WHEELER("Four wheeler");

    private final String value;

    @JsonValue // This ensures "Two wheeler" is sent in the JSON response
    @Override
    public String toString() {
        return value;
    }
}
