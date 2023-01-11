package com.attornatus.people.api.controllers;

import com.attornatus.people.api.dto.person.RequestPersonDTO;
import com.attornatus.people.api.entities.Address;
import com.attornatus.people.api.entities.Person;
import com.attornatus.people.api.services.PersonService;
import com.attornatus.people.api.dto.person.ResponsePersonDTO;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PersonControllerTest {

    public static final Long ID = 1L;
    public static final String NAME     = "John Doe";
    public static final String BIRTH_DAY = "1991-03-16";


    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonService personService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private HttpServletRequest request;

    private Person person;
    private RequestPersonDTO requestPersonDTO;
    private ResponsePersonDTO responsePersonDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        startPerson();
    }

    @Test
    void whenFindPersonByIdReturnSuccess() {
        when(personService.findPersonById(anyLong())).thenReturn(person);
        when(modelMapper.map(any(), any())).thenReturn(responsePersonDTO);

        ResponseEntity<ResponsePersonDTO> response = personController.findPersonById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ResponsePersonDTO.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(BIRTH_DAY, response.getBody().getBirthday().toString());
        assertTrue(response.getBody().getAddressList().isEmpty());

    }

    @Test
    void whenListPeopleThenReturnListOfPersonDTO() {

        when(personService.listPeople()).thenReturn(List.of(person));
        when(modelMapper.map(any(), any())).thenReturn(responsePersonDTO);

        ResponseEntity<List<ResponsePersonDTO>> response = personController.listPeople();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ResponsePersonDTO.class, response.getBody().get(0).getClass());

        assertEquals(ID, response.getBody().get(0).getId());
        assertEquals(NAME, response.getBody().get(0).getName());
        assertEquals(BIRTH_DAY, response.getBody().get(0).getBirthday().toString());
        assertTrue(response.getBody().get(0).getAddressList().isEmpty());

        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void whenCreatePersonThenReturnPersonCreatedWithSuccess() {
        when(personService.createPerson(any())).thenReturn(person);
        when(modelMapper.map(any(), any())).thenReturn(responsePersonDTO);

        ResponseEntity<ResponsePersonDTO> response = personController.createPerson(requestPersonDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ResponsePersonDTO.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(BIRTH_DAY, response.getBody().getBirthday().toString());
        assertTrue(response.getBody().getAddressList().isEmpty());
    }
    @Test
    void whenUpdatePersonThenReturnPersonUpdatedWithSuccess() {
        when(personService.updatePerson(anyLong(), any())).thenReturn(person);
        when(modelMapper.map(any(), any())).thenReturn(responsePersonDTO);


        ResponseEntity<ResponsePersonDTO> response = personController.updatePerson(person.getId(), requestPersonDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ResponsePersonDTO.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(BIRTH_DAY, response.getBody().getBirthday().toString());

        assertTrue(response.getBody().getAddressList().isEmpty());
    }

    @Test
    void whenDeletePersonThenReturnSuccess() {
        doNothing().when(personService).deletePerson(anyLong());

        ResponseEntity<ResponsePersonDTO> response = personController.deletePerson(ID);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());

        verify(personService, times(1)).deletePerson(anyLong());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    private void startPerson(){
        person = new Person(ID, NAME, LocalDate.parse(BIRTH_DAY), new ArrayList<Address>());

        requestPersonDTO = new RequestPersonDTO(NAME, BIRTH_DAY);

        responsePersonDTO  = new ResponsePersonDTO(ID, NAME, LocalDate.parse(BIRTH_DAY), new ArrayList<Address>());
    }
}
