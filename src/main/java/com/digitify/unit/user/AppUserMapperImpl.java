package com.digitify.unit.user;

import com.digitify.framework.annotation.Mapper;
import com.digitify.unit.authorizationgroup.AuthorizationGroupMapperImpl;
import org.hibernate.LazyInitializationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Mapper
public class AppUserMapperImpl implements AppUserMapper {

    @Override
    public AppUser toEntity(AppUserDto dto) {
        AppUser entity = new AppUser();
        if (dto.getUuid() != null)
            entity.setUuid(dto.getUuid());
        else
            entity.setUuid(UUID.randomUUID().toString());
        entity.setIsActive(dto.getIsActive());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setStatus(Optional.ofNullable(dto.getStatus()).map(AppUserStatus::getCode).orElse(null));
        entity.setUserType(Optional.ofNullable(dto.getUserType()).map(AppUserType::getCode).orElse(null));
        entity.setCountryCode(dto.getCountryCode());
        entity.setMobileNo(dto.getMobileNo());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setId(dto.getId());

        if(dto.getAuthorizationGroupDtos() != null) {
            try {
                entity.setAuthorizationGroups(new HashSet<>(new AuthorizationGroupMapperImpl().toEntityList(dto.getAuthorizationGroupDtos())));
            } catch (LazyInitializationException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    @Override
    public AppUser toCleanEntity(CleanAppUserDto dto) {
        AppUser entity = new AppUser();
        entity.setUuid(dto.getUuid());
        entity.setIsActive(dto.getIsActive());
        entity.setUsername(dto.getUsername());
        entity.setStatus(Optional.ofNullable(dto.getStatus()).map(AppUserStatus::getCode).orElse(null));
        entity.setUserType(Optional.ofNullable(dto.getUserType()).map(AppUserType::getCode).orElse(null));
        entity.setCountryCode(dto.getCountryCode());
        entity.setMobileNo(dto.getMobileNo());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        return entity;
    }

    @Override
    public AppUserDto toDto(AppUser entity) {
        AppUserDto dto = new AppUserDto();
        dto.setUuid(entity.getUuid());
        dto.setIsActive(entity.getIsActive());
        dto.setUsername(entity.getUsername());
        //dto.setPassword(entity.getPassword());
        dto.setStatus(AppUserStatus.getType(entity.getStatus()));
        dto.setUserType(AppUserType.getType(entity.getUserType()));
        dto.setCountryCode(entity.getCountryCode());
        dto.setMobileNo(entity.getMobileNo());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setId(entity.getId());

        try {
            if(entity.getAuthorizationGroups() != null) {
                dto.setAuthorizationGroupDtos(new AuthorizationGroupMapperImpl().toDtoList(new ArrayList<>(entity.getAuthorizationGroups())));
            }
        } catch (LazyInitializationException e) {
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public AppUser update(AppUserDto dto, AppUser entity) {
        if (dto.getUuid() != null)
            entity.setUuid(dto.getUuid());

        entity.setIsActive(dto.getIsActive());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setStatus(Optional.ofNullable(dto.getStatus()).map(AppUserStatus::getCode).orElse(null));
        entity.setUserType(Optional.ofNullable(dto.getUserType()).map(AppUserType::getCode).orElse(null));
        entity.setCountryCode(dto.getCountryCode());
        entity.setMobileNo(dto.getMobileNo());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setId(dto.getId());

        if(dto.getAuthorizationGroupDtos() != null) {
            try {
                entity.setAuthorizationGroups(new HashSet<>(new AuthorizationGroupMapperImpl().toEntityList(dto.getAuthorizationGroupDtos())));
            } catch (LazyInitializationException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    @Override
    public AppUser partialUpdate(CleanAppUserDto dto, AppUser entity) {
        if (isNotEmpty(dto.getIsActive())) {
            entity.setIsActive(dto.getIsActive());
        }
        if (isNotEmpty(dto.getStatus().getCode())) {
            entity.setStatus(dto.getStatus().getCode());
        }
        if (isNotEmpty(dto.getUserType().getCode())) {
            entity.setUserType(dto.getUserType().getCode());
        }
        if (isNotEmpty(dto.getUsername())) {
            entity.setUsername(dto.getUsername());
        }
        if (isNotEmpty(dto.getCountryCode())) {
            entity.setCountryCode(dto.getCountryCode());
        }
        if (isNotEmpty(dto.getMobileNo())) {
            entity.setMobileNo(dto.getMobileNo());
        }
        if (isNotEmpty(dto.getFirstName())) {
            entity.setFirstName(dto.getFirstName());
        }
        if (isNotEmpty(dto.getLastName())) {
            entity.setLastName(dto.getLastName());
        }
        if (isNotEmpty(dto.getEmail())) {
            entity.setEmail(dto.getEmail());
        }
        return entity;
    }
}
