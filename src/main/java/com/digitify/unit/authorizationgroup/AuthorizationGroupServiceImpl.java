package com.digitify.unit.authorizationgroup;

import com.digitify.constant.ErrorMessage;
import com.digitify.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationException;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.exception.BadRequestException;
import com.digitify.framework.handler.ErrorCode;
import com.digitify.unit.role.RoleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthorizationGroupServiceImpl implements AuthorizationGroupService {
    @Autowired
    private AuthorizationGroupDataServiceImpl dataService;
    @Autowired
    private AuthorizationGroupDtoValidator validator;

    @Override
    public boolean existsByUuid(String uuid) {
        return dataService.existsByUuid(uuid);
    }

    @Override
    public List<AuthorizationGroupDto> getAll() {
        return dataService.getAll();
    }

    @Override
    public AuthorizationGroupDto get(String uuid) {
        AuthorizationGroupDto parentDto = null;
        try {
            parentDto = dataService.getByUuid(uuid).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return parentDto;
    }

    @Override
    public Page<AuthorizationGroupDto> search(CleanAuthorizationGroupDto dto, int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        return dataService.search(dto, pageRequest);
    }


    @Override
    public AuthorizationGroupDto save(AuthorizationGroupDto dto) {
        AuthorizationGroupDto parentDto = null;
        try {
            validator.validateDto(dto);
            parentDto = dataService.save(dto).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return parentDto;
    }

    @Override
    public AuthorizationGroupDto update(String uuid, AuthorizationGroupDto dto) {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PUT).with(LayerType.SERVICE_LAYER).with(EntityType.AUTHORIZATIONGROUP.toString()).build();
        Optional.ofNullable(dto).map(AuthorizationGroupDto::getUuid).orElseThrow(() -> new BadRequestException(errorCode, "Invalid Uuid"));
        Optional.of(dto)
                .map(AuthorizationGroupDto::getUuid)
                .filter(s -> s.equalsIgnoreCase(uuid))
                .orElseThrow(() -> new BadRequestException(errorCode, "Invalid Uuid"));
        Optional.of(uuid).filter(s -> dataService.existsByUuid(s)).orElseThrow(() -> new BadRequestException(errorCode, "Already exists."));

        AuthorizationGroupDto updatedDto;
        try {
            validator.validateDto(dto);
            updatedDto = dataService.update(dto).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return updatedDto;

    }

    @Override
    public AuthorizationGroupDto partialUpdate(CleanAuthorizationGroupDto dto) {
        AuthorizationGroupDto updatedDto;
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
    public List<RoleDto> getRolesAgainstGroup(Long id) {
        return dataService.getRolesAgainstGroup(id);
    }

    @Override
    public AuthorizationGroupDto assignRolesToGroup(Long groupId, List<RoleDto> roleDtos) {
        return dataService.assignRolesToGroup(groupId, roleDtos);
    }
}