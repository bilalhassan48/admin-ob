package com.digitify.unit.user;

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
public class AppUserDtoValidatorImpl implements AppUserDtoValidator {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public Boolean validateDto(AppUserDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode
                .with(ApiType.UNIT)
                .with(RequestType.NOOP)
                .with(LayerType.VALIDATION_LAYER)
                .with(EntityType.APPUSER.getCode())
                .build();

        if(validateUsername(dto))
            throw new ApplicationUncheckException(errorCode, "Username already exists");

        if(validateMobileNo(dto))
            throw new ApplicationUncheckException(errorCode, "Mobile No. already exists");

        if(validateEmail(dto))
            throw new ApplicationUncheckException(errorCode, "Email already exists");

        return true;
    }

    @Override
    public Boolean validatePartialUpdateDto(CleanAppUserDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode
                .with(ApiType.UNIT)
                .with(RequestType.PATCH)
                .with(LayerType.VALIDATION_LAYER)
                .with(EntityType.APPUSER.getCode())
                .build();

        if(validateUsername(dto))
            throw new ApplicationUncheckException(errorCode, "Username already exists");

        if(validateMobileNo(dto))
            throw new ApplicationUncheckException(errorCode, "Mobile No. already exists");

        if(validateEmail(dto))
            throw new ApplicationUncheckException(errorCode, "Email already exists");

        return true;
    }

    private Boolean validateUsername(AppUserDto dto) {
        return Optional.ofNullable(dto)
                .filter(appUserDto -> StringUtils.hasLength(appUserDto.getUsername()))
                .map(appUserDto -> appUserRepository.existsByUsername(appUserDto.getUsername()))
                .get();
                //.filter(aBoolean -> aBoolean)
                //.orElseThrow(() -> new ApplicationUncheckException(errorCode, "Username already exists"));
    }

    private Boolean validateMobileNo(AppUserDto dto) {
        return Optional.ofNullable(dto)
                .filter(appUserDto -> StringUtils.hasLength(appUserDto.getMobileNo()))
                .map(appUserDto -> appUserRepository.existsByMobileNo(appUserDto.getMobileNo()))
                .get();
                //.filter(aBoolean -> !aBoolean)
                //.orElseThrow(() -> new ApplicationUncheckException(errorCode, "Mobile No. already exists"));
    }

    private Boolean validateEmail(AppUserDto dto) {
        return Optional.ofNullable(dto)
                .filter(appUserDto -> StringUtils.hasLength(appUserDto.getEmail()))
                .map(appUserDto -> appUserRepository.existsByEmail(appUserDto.getEmail()))
                .get();
        //.filter(aBoolean -> !aBoolean)
        //.orElseThrow(() -> new ApplicationUncheckException(errorCode, "Mobile No. already exists"));
    }

    private Boolean validateUsername(CleanAppUserDto dto) {
        return Optional.ofNullable(dto)
                .filter(appUserDto -> StringUtils.hasLength(appUserDto.getUsername()))
                .map(appUserDto -> appUserRepository.existsByUsernameAndUuidIsNot(appUserDto.getUsername(), appUserDto.getUuid()))
                .get();
                //.filter(aBoolean -> !aBoolean)
                //.orElseThrow(() -> new ApplicationUncheckException(errorCode, "Username already exists"));
    }

    private Boolean validateMobileNo(CleanAppUserDto dto) {
        return Optional.ofNullable(dto)
                .filter(appUserDto -> StringUtils.hasLength(appUserDto.getMobileNo()))
                .map(appUserDto -> appUserRepository.existsByMobileNoAndUuidIsNot(appUserDto.getMobileNo(), appUserDto.getUuid()))
                .get();
                //.filter(aBoolean -> !aBoolean)
                //.orElseThrow(() -> new ApplicationUncheckException(errorCode, "Mobile No. already exists"));
    }

    private Boolean validateEmail(CleanAppUserDto dto) {
        return Optional.ofNullable(dto)
                .filter(appUserDto -> StringUtils.hasLength(appUserDto.getEmail()))
                .map(appUserDto -> appUserRepository.existsByEmailAndUuidIsNot(appUserDto.getEmail(), appUserDto.getUuid()))
                .get();
        //.filter(aBoolean -> !aBoolean)
        //.orElseThrow(() -> new ApplicationUncheckException(errorCode, "Mobile No. already exists"));
    }
}
