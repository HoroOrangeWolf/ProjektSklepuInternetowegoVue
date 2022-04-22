package com.computer.parts.shop.User;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public void saveAddress(Address address){
        addressRepository.save(address);
    }

}
