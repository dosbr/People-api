package com.attornatus.people.api.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAddressDTO {
    private Long id;

    private String publicArea;

    private String zipCode;

    private Integer number;

    private String city;

    private Boolean defaultAddress;
}
