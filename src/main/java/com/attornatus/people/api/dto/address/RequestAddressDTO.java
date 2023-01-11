package com.attornatus.people.api.dto.address;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddressDTO {

    private String publicArea;

    private String zipCode;

    private Integer number;

    private String city;

    private Boolean defaultAddress;
}
