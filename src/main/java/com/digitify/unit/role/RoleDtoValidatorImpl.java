package com.digitify.unit.role;

import com.digitify.enums.EntityType;
import com.digitify.framework.annotation.ValidationComponent;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Optional;

@ValidationComponent
public class RoleDtoValidatorImpl implements RoleDtoValidator {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Boolean validateDto(RoleDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode
                .with(ApiType.UNIT)
                .with(RequestType.NOOP)
                .with(LayerType.VALIDATION_LAYER)
                .with(EntityType.ROLE.getCode())
                .build();

        if(validateName(dto))
            throw new ApplicationUncheckException(errorCode, "Role already exists");

        return true;
    }

    private Boolean validateName(RoleDto dto) {
        return Optional.ofNullable(dto)
                .filter(roleDto -> StringUtils.hasLength(roleDto.getName()))
                .map(roleDto -> roleRepository.existsByName(roleDto.getName()))
                .get();
    }

    @Override
    public Boolean validatePartialUpdateDto(CleanRoleDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode
                .with(ApiType.UNIT)
                .with(RequestType.PATCH)
                .with(LayerType.VALIDATION_LAYER)
                .with(EntityType.ROLE.getCode())
                .build();

        if(validateName(dto))
            throw new ApplicationUncheckException(errorCode, "Role already exists");

        return true;
    }

    private Boolean validateName(CleanRoleDto dto) {
        return Optional.ofNullable(dto)
                .filter(roleDto -> StringUtils.hasLength(roleDto.getName()))
                .map(roleDto -> roleRepository.existsByName(roleDto.getName()))
                .get();
    }
}
