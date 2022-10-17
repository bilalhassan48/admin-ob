package com.digitify.unit.authorizationgroup;

import com.digitify.unit.role.Role;
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
@Table(name = "authorization_group")
public class AuthorizationGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    @Column(name = "active")
    private Boolean isActive=true;
    private String name;
    private String description;

    /*@ManyToMany(cascade = {
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "authorization_user_group",
            joinColumns = @JoinColumn(name = "authorization_group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    private Set<AppUser> users;*/

    @ManyToMany(cascade = {
            CascadeType.REFRESH
    })
    @JoinTable(name = "group_role",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizationGroup that = (AuthorizationGroup) o;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
