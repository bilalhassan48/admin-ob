package com.digitify.unit.user;

import com.digitify.unit.authorizationgroup.AuthorizationGroup;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;

    @Column(name = "active")
    private Boolean isActive=true;

    private String username;
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @ManyToMany(cascade = {
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "authorization_user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authorization_group_id")
    )
    private Set<AuthorizationGroup> authorizationGroups;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUser that = (AppUser) o;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
