package com.computer.parts.shop.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.computer.parts.shop.Exceptions.BadRequestException;
import com.computer.parts.shop.JWT.JWTService;
import com.computer.parts.shop.Pageable.Pageable;
import com.computer.parts.shop.User.Request.PasswordChange;
import com.computer.parts.shop.User.Response.AddressDTO;
import com.computer.parts.shop.User.Response.UserDTO;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.AddressStandardClaim;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/v1/user")
@AllArgsConstructor
@RestController
public class UserController {

  private UserService service;
  private AddressService addressService;
  private BCryptPasswordEncoder passwordEncoder;

  @PostMapping
  public void addUser(@RequestBody User user) {
    service.registerUser(user);
  }

  @DeleteMapping(path = "/{id}")
  public void deleteUser(@PathVariable(value = "id") Long id) {
    service.deleteById(id);
  }

  @PostMapping("/{userId}/admin")
  public void blockUser(
    @PathVariable("userId") Long userId,
    @RequestParam("block") Boolean isBlock,
    Authentication authentication
  ) {
    User principal = (User) authentication.getPrincipal();

    if (principal.getId().equals(userId)) throw new BadRequestException(
      "You can't block your account"
    );

    service.blockUser(userId, isBlock);
  }

  @GetMapping("/admin")
  public Pageable<Map<String, Object>> getAllUsers(
    @RequestParam(
      value = "page",
      defaultValue = "0",
      required = false
    ) Integer page,
    @RequestParam(
      value = "limit",
      defaultValue = "20",
      required = false
    ) Integer limit,
    @RequestParam(
      value = "searchBy",
      defaultValue = "",
      required = false
    ) String email,
    @RequestParam(
      value = "sortBy",
      defaultValue = "id",
      required = false
    ) String sortBy,
    @RequestParam(
      value = "order",
      defaultValue = "ASC",
      required = false
    ) Sort.Direction direction
  ) {
    List<User> allUsers = service.findAllBySimilarEmail(
      email,
      page,
      limit,
      sortBy,
      direction
    );

    return new Pageable<>(
      service.countAllBySimilarEmail(email),
      allUsers
        .stream()
        .map(f -> {
          Map<String, Object> stringObjectMap = new TreeMap<>();

          stringObjectMap.put("id", f.getId());
          stringObjectMap.put("name", f.getName());
          stringObjectMap.put("surname", f.getSurname());
          stringObjectMap.put(
            "birthDay",
            f.getBirthDay() == null
              ? ""
              : new SimpleDateFormat("dd/MM/yyyy").format(f.getBirthDay())
          );
          stringObjectMap.put("email", f.getEmail());
          stringObjectMap.put("isBlocked", f.getIsAccountLocked());
          stringObjectMap.put("isSocialAccount", f.getIsSocialAccount());
          stringObjectMap.put("role", f.getRole());
          stringObjectMap.put("phoneNumber", f.getPhoneNumber());

          return stringObjectMap;
        })
        .toList()
    );
  }

  @GetMapping("/authenticatedUser")
  public UserDTO getAuthenticatedUser(
    Authentication authentication,
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    if (authentication == null || authentication.getPrincipal() == null) {
      return new UserDTO(
        "",
        "",
        "",
        Role.GUEST,
        false,
        false,
        null,
        null,
        null
      );
    }

    User user = (User) authentication.getPrincipal();
    Address address = user.getUserAddress();
    AddressDTO addressDTO = new AddressDTO(
      address.getId(),
      address.getPostCode(),
      address.getHomeNumber(),
      address.getStreet(),
      address.getCity()
    );

    return new UserDTO(
      user.getName(),
      user.getSurname(),
      user.getEmail(),
      user.getRole(),
      true,
      user.getIsSocialAccount(),
      addressDTO,
      user.getBirthDay(),
      user.getGetUserGender()
    );
  }

  @PutMapping("/address/{id}")
  public void updateAddress(
    @PathVariable("id") Long id,
    @RequestBody Address address
  ) {
    address.setId(id);
    addressService.updateAddress(address);
  }

  @PutMapping("/address")
  public void updateAddress(
    @RequestBody Address address,
    Authentication authentication
  ) {
    if (authentication == null) throw new BadRequestException(
      "User is not authenticated"
    );

    User principal = (User) authentication.getPrincipal();

    Address userAddress = principal.getUserAddress();

    address.setId(userAddress.getId());

    addressService.updateAddress(address);
  }

  @PutMapping("/password")
  public void updatePassword(
    @RequestBody PasswordChange passwordChange,
    Authentication authentication
  ) {
    if (authentication == null) throw new BadRequestException(
      "User is not authenticated"
    );

    User user = (User) authentication.getPrincipal();

    String encodedPassword = passwordEncoder.encode(
      passwordChange.getNewPassword()
    );
    user.setPassword(encodedPassword);
    service.updateUser(user);
  }

  @PutMapping("")
  public void updateUser(
    @RequestBody UserDTO userDTO,
    Authentication authentication
  ) {
    if (authentication == null) throw new BadRequestException(
      "User is not authenticated"
    );

    User user = (User) authentication.getPrincipal();

    user.setName(userDTO.getName());
    user.setSurname(userDTO.getSurname());
    user.setBirthDay(userDTO.getBirthDay());

    service.updateUser(user);
  }
}
