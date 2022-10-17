package com.digitify.unit.role;

import com.digitify.constant.ErrorMessage;
import com.digitify.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationException;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.exception.BadRequestException;
import com.digitify.framework.handler.ErrorCode;
import com.digitify.unit.authority.AuthorityDto;
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
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDataServiceImpl dataService;
    @Autowired
    private RoleDtoValidator validator;

    @Override
    public boolean existsByUuid(String uuid) {
        return dataService.existsByUuid(uuid);
    }

    @Override
    public List<RoleDto> getAll() {
        return dataService.getAll();
    }

    @Override
    public RoleDto get(String uuid) {
        RoleDto parentDto = null;
        try {
            parentDto = dataService.getByUuid(uuid).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return parentDto;
    }

    @Override
    public Page<RoleDto> search(CleanRoleDto dto, int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        return dataService.search(dto, pageRequest);
    }


    @Override
    public RoleDto save(RoleDto dto) {
        RoleDto parentDto = null;
        try {
            validator.validateDto(dto);
            parentDto = dataService.save(dto).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return parentDto;
    }

    @Override
    public RoleDto update(String uuid, RoleDto dto) {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PUT).with(LayerType.SERVICE_LAYER).with(EntityType.ROLE.toString()).build();
        Optional.ofNullable(dto).map(RoleDto::getUuid).orElseThrow(() -> new BadRequestException(errorCode, "Invalid Uuid"));
        Optional.of(dto)
                .map(RoleDto::getUuid)
                .filter(s -> s.equalsIgnoreCase(uuid))
                .orElseThrow(() -> new BadRequestException(errorCode, "Invalid Uuid"));
        Optional.of(uuid).filter(s -> dataService.existsByUuid(s)).orElseThrow(() -> new BadRequestException(errorCode, "Already exists."));

        RoleDto updatedDto;
        try {
            validator.validateDto(dto);
            updatedDto = dataService.update(dto).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return updatedDto;

    }

    @Override
    public RoleDto partialUpdate(CleanRoleDto dto) {
        RoleDto updatedDto;
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
    public List<AuthorityDto> getAuthoritiesAgainstRole(Long id) {
        return dataService.getAuthoritiesAgainstRole(id);
    }

    @Override
    public RoleDto assignAuthoritiesToRole(Long roleId, List<AuthorityDto> authorityDtos) {
        return dataService.assignAuthoritiesToRole(roleId, authorityDtos);
    }
}