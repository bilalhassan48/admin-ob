package com.digitify.unit.role;

import com.digitify.framework.annotation.Mapper;
import com.digitify.unit.authority.AuthorityMapperImpl;
import org.hibernate.LazyInitializationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@Mapper
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toEntity(RoleDto dto) {
        Role entity = new Role();
        entity.setUuid(UUID.randomUUID().toString());
        entity.setIsActive(dto.getIsActive());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if(dto.getAuthorities() != null) {
            try {
                entity.setAuthorities(new HashSet<>(new AuthorityMapperImpl().toEntityList(dto.getAuthorities())));
            } catch (LazyInitializationException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    @Override
    public Role toCleanEntity(CleanRoleDto dto) {
        Role entity = new Role();
        entity.setUuid(dto.getUuid());
        entity.setIsActive(dto.getIsActive());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUuid(dto.getUuid());
        return entity;
    }

    @Override
    public RoleDto toDto(Role entity) {
        RoleDto dto = new RoleDto();
        dto.setUuid(entity.getUuid());
        dto.setIsActive(entity.getIsActive());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        try {
            if(entity.getAuthorities() != null) {
                dto.setAuthorities(new AuthorityMapperImpl().toDtoList(new ArrayList<>(entity.getAuthorities())));
            }
        } catch (LazyInitializationException e) {
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public Role update(RoleDto dto, Role entity) {
        entity.setIsActive(dto.getIsActive());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUuid(dto.getUuid());

        if(dto.getAuthorities() != null) {
            try {
                entity.setAuthorities(new HashSet<>(new AuthorityMapperImpl().toEntityList(dto.getAuthorities())));
            } catch (LazyInitializationException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    @Override
    public Role partialUpdate(CleanRoleDto dto, Role entity) {
        if (isNotEmpty(dto.getIsActive())) {
            entity.setIsActive(dto.getIsActive());
        }
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUuid(dto.getUuid());
        return entity;
    }
}
