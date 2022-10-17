package com.digitify.unit.authority;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class AuthorityDto {

    private Long id;

    @JsonProperty("uuid")
    private String uuid;


    @JsonProperty("active")
    private Boolean isActive;

    @NonNull
    private String name;

    private String description;


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