package com.attornatus.people.api.controllers;

import com.attornatus.people.api.dto.address.RequestAddressDTO;
import com.attornatus.people.api.dto.address.ResponseAddressDTO;
import com.attornatus.people.api.entities.Address;
import com.attornatus.people.api.entities.Person;
import com.attornatus.people.api.services.AddressService;
import com.attornatus.people.api.services.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AddressControllerTest {


    public static final Long ADDRESS_ID = 1L;

    public static final String PUBLIC_AREA = "Rua da esquina";

    public static final String ZIP_CODE = "22222-222";

    public static final Integer NUMBER = 30;

    public static final Boolean DEFAULT_ADDRESS = true;
    public static final String CITY = "Rio de Janeiro";

    public static final Long PERSON_ID = 1L;
    public static final String NAME     = "John Doe";
    public static final String BIRTH_DAY = "1991-03-16";





    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressService addressService;

    @Mock
    private PersonService personService;

    @Mock
    ModelMapper modelMapper;

    @Mock
    private HttpServletRequest request;

    private Address address;
    private Person person;
    private RequestAddressDTO requestAddressDTO;
    private ResponseAddressDTO responseAddressDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        startAddress();
    }

    @Test
    void whenFindAddressByIdReturnSuccess() {
        when(personService.findPersonById(anyLong())).thenReturn(person);
        when(addressService.findAddressById(any(), anyLong())).thenReturn(address);

        when(modelMapper.map(any(), any())).thenReturn(responseAddressDTO);

        ResponseEntity<ResponseAddressDTO> response = addressController.findAddressById(PERSON_ID, ADDRESS_ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ResponseAddressDTO.class, response.getBody().getClass());

        assertEquals(ADDRESS_ID, response.getBody().getId());
        assertEquals(ZIP_CODE, response.getBody().getZipCode());
        assertEquals(NUMBER, response.getBody().getNumber());
        assertEquals(CITY, response.getBody().getCity());
        assertEquals(PUBLIC_AREA, response.getBody().getPublicArea());
    }

    @Test
    void whenListAddressesThenReturnListOfPersonDTO() {
        when(personService.findPersonById(anyLong())).thenReturn(person);
        when(addressService.listAddresses(any())).thenReturn(person.getAddressList());
        when(modelMapper.map(any(), any())).thenReturn(responseAddressDTO);

        ResponseEntity<List<ResponseAddressDTO>> response = addressController.listAddresses(PERSON_ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ResponseAddressDTO.class, response.getBody().get(0).getClass());

        assertEquals(ADDRESS_ID, response.getBody().get(0).getId());
        assertEquals(ZIP_CODE, response.getBody().get(0).getZipCode());
        assertEquals(NUMBER, response.getBody().get(0).getNumber());
        assertEquals(CITY, response.getBody().get(0).getCity());
        assertEquals(PUBLIC_AREA, response.getBody().get(0).getPublicArea());

        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void whenCreateAddressThenReturnAddressCreatedWithSuccess() {
        when(addressService.createAddress(any(), any())).thenReturn(address);
        when(personService.findPersonById(anyLong())).thenReturn(person);
        when(modelMapper.map(any(), any())).thenReturn(responseAddressDTO);

        ResponseEntity<ResponseAddressDTO> response = addressController.createAddress(PERSON_ID, requestAddressDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ResponseAddressDTO.class, response.getBody().getClass());

        assertEquals(ADDRESS_ID, response.getBody().getId());
        assertEquals(ZIP_CODE, response.getBody().getZipCode());
        assertEquals(NUMBER, response.getBody().getNumber());
        assertEquals(CITY, response.getBody().getCity());
        assertEquals(PUBLIC_AREA, response.getBody().getPublicArea());

    }
    @Test
    void whenUpdateAddressThenReturnAddressUpdatedWithSuccess() {
        when(addressService.updateAddress(anyLong(), any(), any())).thenReturn(address);
        when(modelMapper.map(any(), any())).thenReturn(responseAddressDTO);


        ResponseEntity<ResponseAddressDTO> response = addressController.updateAddress(ADDRESS_ID, PERSON_ID, requestAddressDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ResponseAddressDTO.class, response.getBody().getClass());

        assertEquals(ADDRESS_ID, response.getBody().getId());
        assertEquals(ZIP_CODE, response.getBody().getZipCode());
        assertEquals(NUMBER, response.getBody().getNumber());
        assertEquals(CITY, response.getBody().getCity());
        assertEquals(PUBLIC_AREA, response.getBody().getPublicArea());

    }

    @Test
    void whenDeletePersonThenReturnSuccess() {
        doNothing().when(addressService).deleteAddress(anyLong());

        ResponseEntity<ResponseAddressDTO> response = addressController.deleteAddress(ADDRESS_ID);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());

        verify(addressService, times(1)).deleteAddress(anyLong());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    private void startAddress(){
        address = new Address(ADDRESS_ID, PUBLIC_AREA, ZIP_CODE, NUMBER, CITY, DEFAULT_ADDRESS);

        person = new Person(PERSON_ID, NAME, LocalDate.parse(BIRTH_DAY), List.of(address));

        requestAddressDTO = new RequestAddressDTO(PUBLIC_AREA, ZIP_CODE, NUMBER, CITY, DEFAULT_ADDRESS);

        responseAddressDTO  = new ResponseAddressDTO(ADDRESS_ID, PUBLIC_AREA, ZIP_CODE, NUMBER, CITY, DEFAULT_ADDRESS);
    }
}
