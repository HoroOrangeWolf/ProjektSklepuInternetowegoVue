package com.computer.parts.shop.User;

import com.computer.parts.shop.Exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;

  public void saveAddress(Address address) {
    addressRepository.save(address);
  }

  public void updateAddress(Address address) {
    boolean existsById = addressRepository.existsById(address.getId());
    if (!existsById) throw new BadRequestException("Id not found");
    addressRepository.save(address);
  }
}
