package com.digitify.unit.authority;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Entity
@Table(name = "authority")
public class Authority {

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

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Authority that = (Authority) o;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
