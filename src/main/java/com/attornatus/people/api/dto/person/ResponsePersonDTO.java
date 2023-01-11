package com.attornatus.people.api.dto.person;

import com.attornatus.people.api.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePersonDTO {
    private Long id;

    private String name;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;

    private List<Address> addressList;
}
