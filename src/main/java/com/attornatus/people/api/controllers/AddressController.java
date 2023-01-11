package com.attornatus.people.api.controllers;

import com.attornatus.people.api.entities.Address;
import com.attornatus.people.api.entities.Person;
import com.attornatus.people.api.services.AddressService;
import com.attornatus.people.api.services.PersonService;
import com.attornatus.people.api.dto.address.RequestAddressDTO;
import com.attornatus.people.api.dto.address.ResponseAddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/people/{personId}")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private PersonService personService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/addresses")
    public ResponseEntity<List<ResponseAddressDTO>> listAddresses(@PathVariable Long personId) {
        Person person = personService.findPersonById(personId);
        List<ResponseAddressDTO> responseAddressDTOList = addressService.listAddresses(person)
                .stream().map(address -> modelMapper.map(address, ResponseAddressDTO.class)).toList();
        return ResponseEntity.ok().body(responseAddressDTOList);
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<ResponseAddressDTO> findAddressById(@PathVariable Long personId, @PathVariable Long id) {
        Person person = personService.findPersonById(personId);
        return ResponseEntity.ok().body(modelMapper.map(addressService.findAddressById(person, id), ResponseAddressDTO.class));
    }
    @GetMapping("/defaultAddress")
    public ResponseEntity<ResponseAddressDTO> findDefaultAddress(@PathVariable Long personId) {
        Person person = personService.findPersonById(personId);
        return ResponseEntity.ok().body(modelMapper.map(addressService.findDefaultAddress(person), ResponseAddressDTO.class));
    }

    @PostMapping("/addresses")
    public ResponseEntity<ResponseAddressDTO> createAddress(@PathVariable Long personId, @RequestBody RequestAddressDTO addressDTO) {
        Person person = personService.findPersonById(personId);
        Address address = addressService.createAddress(person, addressDTO);

        URI uri = URI.create("/api/v1/people/" + person.getId() + "/addresses/" + address.getId());

        personService.updateAddresses(person);

        return ResponseEntity.created(uri).body(modelMapper.map(address, ResponseAddressDTO.class));
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<ResponseAddressDTO> updateAddress(@PathVariable Long personId, @PathVariable Long id, @RequestBody RequestAddressDTO addressDTO) {
        Person person = personService.findPersonById(personId);
        Address address = addressService.updateAddress(id, person, addressDTO);

        return ResponseEntity.ok().body(modelMapper.map(address, ResponseAddressDTO.class));
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<ResponseAddressDTO> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

}
