package com.attornatus.people.api.services;

import com.attornatus.people.api.entities.Address;
import com.attornatus.people.api.entities.Person;
import com.attornatus.people.api.exceptions.ObjectNotFoundException;
import com.attornatus.people.api.repositories.AddressRepository;
import com.attornatus.people.api.dto.address.RequestAddressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    public List<Address> listAddresses(Person person) {
        return person.getAddressList();
    }

    public Address findAddressById(Person person, Long id) {
        List<Address> addressList = person.getAddressList();

            Optional<Address> optionalAddress = addressList.stream()
                    .filter(address -> address.getId().equals(id))
                    .findFirst();

            return optionalAddress.orElseThrow(() -> new ObjectNotFoundException("Address not found!"));
    }

    public Address createAddress(Person person, RequestAddressDTO addressDTO) {
        List<Address> addressList = person.getAddressList();

        resetDefaultAddress(addressList);

        Address address = Address.builder()
                .publicArea(addressDTO.getPublicArea())
                .zipCode(addressDTO.getZipCode())
                .city(addressDTO.getCity())
                .number(addressDTO.getNumber())
                .defaultAddress(true)
                .build();


        Address addressSaved = addressRepository.save(address);

        addressList.add(addressSaved);

        person.setAddressList(addressList);

        return addressSaved;
    }

    public Address updateAddress(Long id, Person person, RequestAddressDTO addressDTO) {
        List<Address> addressList = person.getAddressList();

        if(addressDTO.getDefaultAddress()){
            resetDefaultAddress(addressList);
        }

        person.setAddressList(addressList.stream().map(address -> {
            if(address.getId().equals(id)) {
                address.setCity(addressDTO.getCity());
                address.setZipCode(addressDTO.getZipCode());
                address.setNumber(addressDTO.getNumber());
                address.setPublicArea(addressDTO.getPublicArea());
                address.setDefaultAddress(addressDTO.getDefaultAddress());
            }
            return address;
        }).toList());

        Optional<Address> optionalAddress = addressList.stream()
                .filter(address -> address.getId().equals(id))
                .findFirst();
        Address address = optionalAddress.orElseThrow(() -> new ObjectNotFoundException("Address not found!"));

        return addressRepository.save(address) ;
    }

    public void deleteAddress(Long id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        Address address = optionalAddress
                .orElseThrow(() -> new ObjectNotFoundException("Address not found"));
        addressRepository.delete(address);
    }

    public Address findDefaultAddress(Person person) {
        Optional<Address> optionalAddress = person.getAddressList()
                .stream().filter(address -> address.getDefaultAddress()).findFirst();

        return optionalAddress
                .orElseThrow(() -> new ObjectNotFoundException("Address not found"));
    }

    private void resetDefaultAddress(List<Address> addressList) {
        if(!addressList.isEmpty()){
            for(Address address : addressList ) {
                address.setDefaultAddress(false);
            }
        }
    }
}
