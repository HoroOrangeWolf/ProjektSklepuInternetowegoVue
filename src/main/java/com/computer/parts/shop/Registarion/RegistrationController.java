package com.computer.parts.shop.Registarion;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

  private final RegistrationService service;

  @PostMapping
  public void register(
    @Valid @RequestBody RegistrationRequest registrationRequest
  ) {
    service.register(registrationRequest);
  }
}
