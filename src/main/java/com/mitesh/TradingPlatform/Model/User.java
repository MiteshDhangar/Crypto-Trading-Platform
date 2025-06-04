package com.mitesh.TradingPlatform.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mitesh.TradingPlatform.Domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Better suited for most SQL DBs
    private Long id;

    private String fullName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    private String mobile;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
}
