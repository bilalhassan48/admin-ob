package com.digitify.unit.authority;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class CleanAuthorityDto {
    private Long id;

    @NotNull
    @JsonProperty("uuid")
    private String uuid;


    @JsonProperty("active")
    private Boolean isActive;

    private String name;

    private String description;

    private Boolean isAdmin;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorityDto that = (AuthorityDto) o;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }
}