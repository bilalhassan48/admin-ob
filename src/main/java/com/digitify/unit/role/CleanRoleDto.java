package com.digitify.unit.role;

import com.digitify.unit.authority.AuthorityDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class CleanRoleDto {

    private Long id;

    @NotNull
    @JsonProperty("uuid")
    private String uuid;


    @JsonProperty("active")
    private Boolean isActive;

    private String name;
    private String description;
    private List<AuthorityDto> authorities;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleDto that = (RoleDto) o;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }
}