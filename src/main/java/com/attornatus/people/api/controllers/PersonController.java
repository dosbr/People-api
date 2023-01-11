package com.attornatus.people.api.controllers;

import com.attornatus.people.api.entities.Person;
import com.attornatus.people.api.dto.person.RequestPersonDTO;
import com.attornatus.people.api.dto.person.ResponsePersonDTO;
import com.attornatus.people.api.services.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/people")
public class PersonController {
    @Autowired
    private PersonService personService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ResponsePersonDTO>> listPeople() {
        List<Person> personList = personService.listPeople();
        List<ResponsePersonDTO> responsePersonDTOList = personList
                .stream()
                .map(person -> modelMapper.map(person, ResponsePersonDTO.class))
                .toList();
        return ResponseEntity.ok().body(responsePersonDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePersonDTO> findPersonById(@PathVariable Long id) {
        return ResponseEntity.ok().body(modelMapper.map(personService.findPersonById(id), ResponsePersonDTO.class));
    }

    @PostMapping
    public ResponseEntity<ResponsePersonDTO> createPerson(@RequestBody RequestPersonDTO personDTO) {
        Person person = personService.createPerson(personDTO);
        URI uri = URI.create("/api/v1/people/" + person.getId());
        return ResponseEntity.created(uri).body(modelMapper.map(person, ResponsePersonDTO.class));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponsePersonDTO> updatePerson(@PathVariable Long id, @RequestBody RequestPersonDTO personDTO) {
        Person person = personService.updatePerson(id, personDTO);
        URI uri = URI.create("/api/v1/people/" + person.getId());
        return ResponseEntity.ok().body(modelMapper.map(person, ResponsePersonDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePersonDTO> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
