package com.digitify.unit.authorizationgroup;

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
public class AuthorizationGroupDtoValidatorImpl implements AuthorizationGroupDtoValidator {
    @Autowired
    private AuthorizationGroupRepository authorizationGroupRepository;

    @Override
    public Boolean validateDto(AuthorizationGroupDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode
                .with(ApiType.UNIT)
                .with(RequestType.NOOP)
                .with(LayerType.VALIDATION_LAYER)
                .with(EntityType.AUTHORIZATIONGROUP.getCode())
                .build();


        if(validateName(dto))
            throw new ApplicationUncheckException(errorCode, "Group already exists");

        return true;
    }

    private Boolean validateName(AuthorizationGroupDto dto) {
        return Optional.ofNullable(dto)
                .filter(authorizationGroupDto -> StringUtils.hasLength(authorizationGroupDto.getName()))
                .map(authorizationGroupDto -> authorizationGroupRepository.existsByName(authorizationGroupDto.getName()))
                .get();
    }

    @Override
    public Boolean validatePartialUpdateDto(CleanAuthorizationGroupDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode
                .with(ApiType.UNIT)
                .with(RequestType.PATCH)
                .with(LayerType.VALIDATION_LAYER)
                .with(EntityType.AUTHORIZATIONGROUP.getCode())
                .build();

        if(validateName(dto))
            throw new ApplicationUncheckException(errorCode, "Email already exists");

        return true;
    }

    private Boolean validateName(CleanAuthorizationGroupDto dto) {
        return Optional.ofNullable(dto)
                .filter(authorizationGroupDto -> StringUtils.hasLength(authorizationGroupDto.getName()))
                .map(authorizationGroupDto -> authorizationGroupRepository.existsByName(authorizationGroupDto.getName()))
                .get();
    }
}
