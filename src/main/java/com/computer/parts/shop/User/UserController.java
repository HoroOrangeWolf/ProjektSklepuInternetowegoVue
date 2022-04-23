package com.computer.parts.shop.User;


import com.computer.parts.shop.Pageable.Pageable;
import com.computer.parts.shop.User.Response.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RequestMapping(path = "/api/v1/user")
@AllArgsConstructor
@RestController

public class UserController {

    private UserService service;

    @PostMapping
    public void addUser(@RequestBody User user){
        service.registerUser(user);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable(value="id") Long id){
        service.deleteById(id);
    }

    @PostMapping("/{userId}/admin")
    public void blockUser(@PathVariable("userId") Long userId, @RequestParam("block") Boolean isBlock, Authentication authentication){
        User principal = (User)authentication.getPrincipal();

        if(principal.getId().equals(userId))
            throw new IllegalStateException("You can't block your account");

        service.blockUser(userId, isBlock);
    }

    @GetMapping("/admin")
    public Pageable<Map<String, Object>> getAllUsers(
            @RequestParam(
                    value = "page",
                    defaultValue = "0",
                    required = false
            )
            Integer page,
            @RequestParam(
                    value = "limit",
                    defaultValue = "20",
                    required = false
            )
            Integer limit,
            @RequestParam(
                    value = "searchBy",
                    defaultValue = "",
                    required = false
            )
            String email,
            @RequestParam(
                    value = "sortBy",
                    defaultValue = "id",
                    required = false
            )
            String sortBy,
            @RequestParam(
                    value = "order",
                    defaultValue = "ASC",
                    required = false
            )
            Sort.Direction direction
    ){
        List<User> allUsers = service.findAllBySimilarEmail(email, page, limit, sortBy, direction);

        return new Pageable<>(service.countAllBySimilarEmail(email),
                allUsers.stream().map(f-> {
                    Map<String, Object> stringObjectMap = new TreeMap<>();

                    stringObjectMap.put("id", f.getId());
                    stringObjectMap.put("name", f.getName());
                    stringObjectMap.put("surname", f.getSurname());
                    stringObjectMap.put("birthDay", f.getBirthDay() == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(f.getBirthDay()));
                    stringObjectMap.put("email", f.getEmail());
                    stringObjectMap.put("isBlocked", f.getIsAccountLocked());
                    stringObjectMap.put("isSocialAccount", f.getIsSocialAccount());
                    stringObjectMap.put("role", f.getRole());
                    stringObjectMap.put("phoneNumber", f.getPhoneNumber());

                    return stringObjectMap;
                }).toList()
       );
    }

    @GetMapping("/authenticatedUser")
    public UserDTO getAuthenticatedUser(Authentication authentication){
        if(authentication == null || authentication.getPrincipal() == null){
            return new UserDTO("", "", "", Role.GUEST,false);
        }
        User user = (User) authentication.getPrincipal();

        return new UserDTO(
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getRole(),
                true
        );
    }


}
