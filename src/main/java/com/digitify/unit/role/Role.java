package com.digitify.unit.role;

import com.digitify.unit.authority.Authority;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;


    @Column(name = "active")
    private Boolean isActive=true;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(cascade = {
            CascadeType.REFRESH
    })
    @JoinTable(name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    @JsonManagedReference
    private Set<Authority> authorities;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role that = (Role) o;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
