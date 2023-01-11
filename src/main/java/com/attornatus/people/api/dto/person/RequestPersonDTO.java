package com.attornatus.people.api.dto.person;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPersonDTO {
    private String name;
    private String birthday;
}
