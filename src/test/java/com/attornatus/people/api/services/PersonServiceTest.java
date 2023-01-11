package com.attornatus.people.api.services;

import com.attornatus.people.api.dto.person.RequestPersonDTO;
import com.attornatus.people.api.entities.Address;
import com.attornatus.people.api.entities.Person;
import com.attornatus.people.api.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PersonServiceTest {
    public static final Long ID = 1L;
    public static final String NAME     = "John Doe";
    public static final String BIRTH_DAY = "1991-03-16";

    @InjectMocks
    private PersonService personService;
    @Mock
    PersonRepository personRepository;
    private Person person;
    private RequestPersonDTO requestPersonDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startPerson();
    }

    @Test
    void whenFindPersonByIdReturnSuccess() {
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        Person response = personService.findPersonById(ID);

        assertNotNull(response);
        assertEquals(Person.class, response.getClass());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(BIRTH_DAY, response.getBirthday().toString());
        assertTrue(response.getAddressList().isEmpty());
    }

    @Test
    void whenListPeopleThenReturnListOfPerson() {
        when(personRepository.findAll()).thenReturn(List.of(person));

        List<Person> response = personService.listPeople();

        assertNotNull(response);
        assertEquals(Person.class, response.get(0).getClass());

        assertEquals(ID, response.get(0).getId());
        assertEquals(NAME, response.get(0).getName());
        assertEquals(BIRTH_DAY, response.get(0).getBirthday().toString());
        assertTrue(response.get(0).getAddressList().isEmpty());

        assertFalse(response.isEmpty());
    }

    @Test
    void whenCreatePersonThenReturnPersonCreatedWithSuccess() {
        when(personRepository.save(any())).thenReturn(person);

        Person response = personService.createPerson(requestPersonDTO);

        assertNotNull(response);
        assertEquals(Person.class, response.getClass());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(BIRTH_DAY, response.getBirthday().toString());
        assertTrue(response.getAddressList().isEmpty());
    }
    @Test
    void whenUpdatePersonThenReturnPersonUpdatedWithSuccess() {
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        when(personRepository.save(any())).thenReturn(person);

        Person response = personService.updatePerson(ID, requestPersonDTO);

        assertNotNull(response);
        assertEquals(Person.class, response.getClass());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(BIRTH_DAY, response.getBirthday().toString());
        assertTrue(response.getAddressList().isEmpty());
    }

    @Test
    void whenDeletePersonThenReturnSuccess() {
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        doNothing().when(personRepository).deleteById(anyLong());

        personService.deletePerson(ID);

        verify(personRepository, times(1)).deleteById(anyLong());
    }


    private void startPerson(){
        person = new Person(ID, NAME, LocalDate.parse(BIRTH_DAY), new ArrayList<Address>());

        requestPersonDTO = new RequestPersonDTO(NAME, BIRTH_DAY);
    }
}
