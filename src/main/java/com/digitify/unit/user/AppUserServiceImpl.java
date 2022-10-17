package com.digitify.unit.user;

import com.digitify.constant.ErrorMessage;
import com.digitify.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationException;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.exception.BadRequestException;
import com.digitify.framework.handler.ErrorCode;
import com.digitify.unit.authorizationgroup.AuthorizationGroup;
import com.digitify.unit.authorizationgroup.AuthorizationGroupMapperImpl;
import com.digitify.unit.authorizationgroup.AuthorizationGroupRepository;
import com.digitify.unit.authorizationgroup.AuthorizationGroupDto;
import com.digitify.unit.authorizationgroup.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {
    @Autowired
    private AppUserDataServiceImpl dataService;
    @Autowired
    private AppUserDtoValidator validator;

    @Autowired
    private AuthorizationGroupRepository authorizationGroupRepository;

    @Override
    public boolean existsByUuid(String uuid) {
        return dataService.existsByUuid(uuid);
    }

    @Override
    public List<AppUserDto> getAll() {
        return dataService.getAll();
    }

    @Override
    public AppUserDto get(String uuid) {
        AppUserDto parentDto = null;
        try {
            parentDto = dataService.getByUuid(uuid).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return parentDto;
    }

    @Override
    public Page<AppUserDto> search(CleanAppUserDto dto, int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        return dataService.search(dto, pageRequest);
    }


    @Override
    public AppUserDto save(AppUserDto dto) {
        AppUserDto parentDto = null;
        try {
            if(dto.getPassword() == null)
                dto.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(UUID.randomUUID().toString()));

            assignDefaultGroup(dto);

            validator.validateDto(dto);
            parentDto = dataService.save(dto).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return parentDto;
    }

    private void assignDefaultGroup(AppUserDto dto) {
        AuthorizationGroup authorizationGroup;
        if(dto.getUserType().equals(AppUserType.MOBILE_APP)) {
            authorizationGroup = authorizationGroupRepository.findByName(Constants.DEFAULT_MOBILE_GROUP_NAME);
        } else {
            authorizationGroup = authorizationGroupRepository.findByName(Constants.DEFAULT_ADMIN_GROUP_NAME);
        }
        dto.setAuthorizationGroupDtos(new AuthorizationGroupMapperImpl().toDtoList(Arrays.asList(authorizationGroup)));
    }

    @Override
    public AppUserDto update(String uuid, AppUserDto dto) {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PUT).with(LayerType.SERVICE_LAYER).with(EntityType.APPUSER.toString()).build();
        Optional.ofNullable(dto).map(AppUserDto::getUuid).orElseThrow(() -> new BadRequestException(errorCode, "Invalid Uuid"));
        Optional.of(dto)
                .map(AppUserDto::getUuid)
                .filter(s -> s.equalsIgnoreCase(uuid))
                .orElseThrow(() -> new BadRequestException(errorCode, "Invalid Uuid"));
        Optional.of(uuid).filter(s -> dataService.existsByUuid(s)).orElseThrow(() -> new BadRequestException(errorCode, "Already exists."));

        AppUserDto updatedDto;
        try {
            validator.validateDto(dto);
            updatedDto = dataService.update(dto).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return updatedDto;

    }

    @Override
    public AppUserDto partialUpdate(CleanAppUserDto dto) {
        AppUserDto updatedDto;
        try {
            validator.validatePartialUpdateDto(dto);
            updatedDto = dataService.partialUpdate(dto).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return updatedDto;
    }

    @Override
    public void delete(String uuid) {
        try {
            dataService.delete(uuid);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
    }


    @Override
    public AppUserDto assignGroupsToUser(Long userId, List<AuthorizationGroupDto> authorizationGroupDtos) {
        return dataService.assignGroupsToUser(userId, authorizationGroupDtos);
    }

    @Override
    public List<AppUserDto> getUsersAgainstGroup(Long id) {
        return dataService.getUsersAgainstGroup(id);
    }
}