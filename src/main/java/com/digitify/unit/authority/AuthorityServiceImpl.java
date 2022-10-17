package com.digitify.unit.authority;

import com.digitify.constant.ErrorMessage;
import com.digitify.enums.EntityType;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationException;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.exception.BadRequestException;
import com.digitify.framework.handler.ErrorCode;
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
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityDataServiceImpl dataService;
    @Autowired
    private AuthorityDtoValidator validator;

    @Override
    public boolean existsByUuid(String uuid) {
        return dataService.existsByUuid(uuid);
    }

    @Override
    public List<AuthorityDto> getAll() {
        log.info("In AuthorityServiceImpl getAll method");
        return dataService.getAll();
    }

    @Override
    public AuthorityDto get(String uuid) {
        AuthorityDto parentDto = null;
        try {
            log.info("In AuthorityServiceImpl get method with uuid {}", uuid);
            parentDto = dataService.getByUuid(uuid).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return parentDto;
    }

    @Override
    public Page<AuthorityDto> search(CleanAuthorityDto dto, int pageNo, int pageSize) {
        log.info("In AuthorityServiceImpl search method with pageNo {} and pageSize {}", pageNo, pageSize);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        return dataService.search(dto, pageRequest);
    }


    @Override
    public AuthorityDto save(AuthorityDto dto) {
        AuthorityDto parentDto = null;
        try {
            validator.validateDto(dto);
            parentDto = dataService.save(dto).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return parentDto;
    }

    @Override
    public AuthorityDto update(String uuid, AuthorityDto dto) {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PUT).with(LayerType.SERVICE_LAYER).with(EntityType.AUTHORITY.toString()).build();
        Optional.ofNullable(dto).map(AuthorityDto::getUuid).orElseThrow(() -> new BadRequestException(errorCode, "Invalid Uuid"));
        Optional.of(dto)
                .map(AuthorityDto::getUuid)
                .filter(s -> s.equalsIgnoreCase(uuid))
                .orElseThrow(() -> new BadRequestException(errorCode, "Invalid Uuid"));
        Optional.of(uuid).filter(s -> dataService.existsByUuid(s)).orElseThrow(() -> new BadRequestException(errorCode, "Already exists."));

        AuthorityDto updatedDto;
        try {
            validator.validateDto(dto);
            updatedDto = dataService.update(dto).orElse(null);
        } catch (ApplicationUncheckException e) {
            throw new ApplicationException(e.getCode(), e.getMessage());
        }
        return updatedDto;

    }

    @Override
    public AuthorityDto partialUpdate(CleanAuthorityDto dto) {
        AuthorityDto updatedDto;
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


}