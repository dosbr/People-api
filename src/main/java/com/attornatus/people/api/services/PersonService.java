package com.attornatus.people.api.services;

import com.attornatus.people.api.entities.Person;
import com.attornatus.people.api.exceptions.ObjectNotFoundException;
import com.attornatus.people.api.dto.person.RequestPersonDTO;
import com.attornatus.people.api.repositories.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> listPeople() {
        return personRepository.findAll();
    }

    public Person findPersonById(Long id) {
        Optional<Person> optionalPerson =  personRepository.findById(id);

        return optionalPerson.orElseThrow(() -> new ObjectNotFoundException("Person not found"));
    }
    public Person createPerson (RequestPersonDTO personDTO){
        Person person = new Person();

        person.setName(personDTO.getName());
        person.setBirthday(LocalDate.parse(personDTO.getBirthday()));

        return personRepository.save(person);
    }

    public Person updatePerson(Long id, RequestPersonDTO personDTO) {
        Person person = findPersonById(id);

        person.setName(personDTO.getName());
        person.setBirthday(LocalDate.parse(personDTO.getBirthday()));

        return personRepository.save(person);
    }

    public void deletePerson(Long id) {
        findPersonById(id);
        personRepository.deleteById(id);
    }


    public void updateAddresses(Person person) {
        personRepository.save(person);
    }
}
