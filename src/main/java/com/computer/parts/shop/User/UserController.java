package com.computer.parts.shop.User;


import com.computer.parts.shop.User.Response.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
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

    @GetMapping("/admin")
    public List<Map<String, Object>> getAllUsers(
            @RequestParam(
                    value = "searchBy",
                    defaultValue = "",
                    required = false
            )
            String email
    ){
        List<User> allUsers = service.findAllBySimilarEmail(email);

        return allUsers.stream().map(f->{
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
        }).toList();
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
