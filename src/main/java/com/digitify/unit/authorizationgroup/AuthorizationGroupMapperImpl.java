package com.digitify.unit.authorizationgroup;

import com.digitify.framework.annotation.Mapper;
import com.digitify.unit.role.RoleMapperImpl;
import org.hibernate.LazyInitializationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@Mapper
public class AuthorizationGroupMapperImpl implements AuthorizationGroupMapper {

    @Override
    public AuthorizationGroup toEntity(AuthorizationGroupDto dto) {
        AuthorizationGroup entity = new AuthorizationGroup();
        entity.setUuid(UUID.randomUUID().toString());
        entity.setId(dto.getId());
        entity.setIsActive(dto.getIsActive());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if(dto.getRoles() != null) {
            try {
                entity.setRoles(new HashSet<>(new RoleMapperImpl().toEntityList(dto.getRoles())));
            } catch (LazyInitializationException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    @Override
    public AuthorizationGroup toCleanEntity(CleanAuthorizationGroupDto dto) {
        AuthorizationGroup entity = new AuthorizationGroup();
        entity.setUuid(dto.getUuid());
        entity.setIsActive(dto.getIsActive());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public AuthorizationGroupDto toDto(AuthorizationGroup entity) {
        AuthorizationGroupDto dto = new AuthorizationGroupDto();
        dto.setUuid(entity.getUuid());
        dto.setId(entity.getId());
        dto.setIsActive(entity.getIsActive());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        try {
            if(entity.getRoles() != null) {
                dto.setRoles(new RoleMapperImpl().toDtoList(new ArrayList<>(entity.getRoles())));
            }
        } catch (LazyInitializationException e) {
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public AuthorizationGroup update(AuthorizationGroupDto dto, AuthorizationGroup entity) {
        entity.setIsActive(dto.getIsActive());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if(dto.getRoles() != null) {
            try {
                entity.setRoles(new HashSet<>(new RoleMapperImpl().toEntityList(dto.getRoles())));
            } catch (LazyInitializationException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    @Override
    public AuthorizationGroup partialUpdate(CleanAuthorizationGroupDto dto, AuthorizationGroup entity) {
        if (isNotEmpty(dto.getIsActive())) {
            entity.setIsActive(dto.getIsActive());
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
