package com.computer.parts.shop.User;

import com.computer.parts.shop.Order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.*;

@Table
@Entity(name = "account")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails, OidcUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence_generator")
    @SequenceGenerator(name = "user_sequence_generator", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @Length(min=6, max=64)
    @Column(name = "password", length = 64)
    private String password;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @JsonIgnore
    private Role role = Role.USER;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    @Column(name = "is_enabled", nullable = false)
    @JsonIgnore
    private Boolean isEnabled = true;

    @Column(name = "is_account_locked", nullable = false)
    private Boolean isAccountLocked = false;

    @Column(name = "is_social_account", nullable = false)
    @JsonIgnore
    private Boolean isSocialAccount = false;

    @Column(name = "birth_day", nullable = false)
    private Timestamp birthDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender getUserGender;

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_address_id")
    private Address userAddress;

    public User(String password, String email, String phoneNumber, Role role, String name, String surname, Timestamp birthDay, Gender getUserGender, Address userAddress) {
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.birthDay = birthDay;
        this.getUserGender = getUserGender;
        this.userAddress = userAddress;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());

        return Collections.singletonList(authority);
    }



    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public Map<String, Object> getClaims() {
        return Map.of();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", isEnabled=" + isEnabled +
                ", isAccountLocked=" + isAccountLocked +
                ", isSocialAccount=" + isSocialAccount +
                ", birthDay=" + birthDay +
                ", getUserGender=" + getUserGender +
                '}';
    }
}
