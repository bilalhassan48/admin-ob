package com.digitify.unit.user;

import com.digitify.enums.EntityType;
import com.digitify.framework.annotation.DataService;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.ErrorCode;
import com.digitify.framework.util.Rethrow;
import com.digitify.unit.authorizationgroup.AuthorizationGroup;
import com.digitify.unit.authorizationgroup.AuthorizationGroupDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@DataService
public class AppUserDataServiceImpl implements AppUserDataService {
    @Autowired
    private AppUserRepository repository;
    @Autowired
    private AppUserMapper mapper;

    @Autowired
    private AppUserEntityValidator validator;

    @Override
    public boolean existsByUuid(String uuid) {
        return repository.existsByUuid(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUserDto> getAll() {
        return Stream.ofNullable(repository.findAll()).flatMap(Collection::stream).map(entity -> mapper.toDto(entity)).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AppUserDto> getByUuid(String uuid) throws ApplicationUncheckException {
        return Optional.ofNullable(uuid)
                .filter(StringUtils::hasLength)
                .flatMap(s -> repository.findByUuid(s))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .map(entity -> mapper.toDto(entity));
    }

    @Override
    public Page<AppUserDto> search(CleanAppUserDto dto, PageRequest pageRequest) {
        return Optional.ofNullable(dto).filter(dto1 -> Objects.nonNull(pageRequest)).map(dto1 -> mapper.toCleanEntity(dto1))
                .map(Example::of)
                .map(example -> repository.findAll(example, pageRequest))
                .map(userCardPermissions ->new PageImpl<>(Optional.of(userCardPermissions)
                        .map(userCardPermissions1 -> userCardPermissions.getContent())
                        .stream()
                        .flatMap(Collection::stream)
                        .map(entity -> mapper.toDto(entity))
                        .collect(Collectors.toList()), pageRequest, userCardPermissions.getTotalElements()))
                .orElse(new PageImpl<>(new ArrayList<>()));
    }

    @Override
    public Optional<AppUserDto> save(AppUserDto dto) throws ApplicationUncheckException {
        return Optional.ofNullable(dto)
                .map(Rethrow.rethrowFunction(this::prepareEntityToSave))
                .map(entity -> repository.save(entity))
                .map(entity -> mapper.toDto(entity));
    }

    private AppUser prepareEntityToSave(AppUserDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.POST).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.APPUSER.toString()).build();
        if (dto == null) {
            throw new ApplicationUncheckException(errorCode, "Invalid request");
        }
        Optional<AppUser> optionalEntity = Optional.ofNullable(dto)
                .map(AppUserDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
        if (optionalEntity.isPresent()) {
            throw new ApplicationUncheckException(errorCode, "Already exists");
        }
        return mapper.toEntity(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AppUserDto> save(List<AppUserDto> list) throws ApplicationUncheckException {
        return Optional.ofNullable(list)
                .map(Rethrow.rethrowFunction(dtoList -> Stream.ofNullable(dtoList)
                        .flatMap(Collection::stream)
                        .map(Rethrow.rethrowFunction(dto -> prepareEntityToSave(dto)))
                        .collect(Collectors.toList())))
                .map(entityList -> repository.saveAll(entityList))
                .map(entityList -> mapper.toDtoList(entityList))
                .orElse(new ArrayList<>());
    }

    @Override
    public Optional<AppUserDto> update(AppUserDto dto) throws ApplicationUncheckException {
        return Optional.ofNullable(dto)
                .map(Rethrow.rethrowFunction(this::prepareEntityToUpdate))
                .map(entity -> repository.save(entity))
                .map(entity -> mapper.toDto(entity));
    }

    private AppUser prepareEntityToUpdate(AppUserDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PUT).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.APPUSER.toString()).build();
        return Optional.of(dto)
                .map(AppUserDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity)))
                .map(entity -> mapper.update(dto, entity))
                .orElseThrow(() -> new ApplicationUncheckException(errorCode, "unable to prepare entity to update."));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AppUserDto> update(List<AppUserDto> list) throws ApplicationUncheckException {
        return Optional.ofNullable(list)
                .filter(dtoList -> !dtoList.isEmpty())
                .map(Rethrow.rethrowFunction(dtoList -> dtoList.stream().map(Rethrow.rethrowFunction(this::prepareEntityToUpdate)).collect(Collectors.toList())))
                .map(entityList -> repository.saveAll(entityList))
                .map(entityList -> mapper.toDtoList(entityList))
                .orElse(new ArrayList<>());
    }

    @Override
    public Optional<AppUserDto> partialUpdate(CleanAppUserDto dto) throws ApplicationUncheckException {
        return Optional.ofNullable(dto)
                .map(Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate))
                .map(entity -> repository.save(entity))
                .map(entity -> mapper.toDto(entity));
    }

    private AppUser prepareEntityToPartialUpdate(CleanAppUserDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PATCH).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.APPUSER.toString()).build();
        return Optional.ofNullable(dto)
                .map(CleanAppUserDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity)))
                .map(entity -> mapper.partialUpdate(dto, entity))
                .orElseThrow(() -> new ApplicationUncheckException(errorCode, "Unable to prepare entity to partial update"));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AppUserDto> partialUpdate(List<CleanAppUserDto> list) throws ApplicationUncheckException {
        return Optional.ofNullable(list)
                .filter(dtoList -> !dtoList.isEmpty())
                .map(Rethrow.rethrowFunction(
                        dtoList -> dtoList.stream().map(Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate)).collect(Collectors.toList())))
                .map(entityList -> repository.saveAll(entityList))
                .map(entityList -> mapper.toDtoList(entityList))
                .orElse(new ArrayList<>());
    }

    @Override
    public void delete(String uuid) throws ApplicationUncheckException {
        Optional.ofNullable(uuid)
                .filter(StringUtils::hasLength)
                .flatMap(s -> repository.findByUuid(s))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .ifPresent(entity -> repository.delete(entity));
    }

    @Override
    public void delete(AppUserDto dto) throws ApplicationUncheckException {
        Optional.ofNullable(dto)
                .map(AppUserDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .ifPresent(appUser -> repository.delete(appUser));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(List<AppUserDto> list) throws ApplicationUncheckException {
        List<AppUser> entityList = repository.findAllByUuidIn(
                Stream.ofNullable(list).flatMap(Collection::stream).map(AppUserDto::getUuid).collect(Collectors.toList()));
        repository.deleteAllByUuidIn(Stream.ofNullable(entityList)
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .flatMap(Collection::stream)
                .map(AppUser::getUuid)
                .collect(Collectors.toList()));
    }

    @Override
    public AppUserDto assignGroupsToUser(Long userId, List<AuthorizationGroupDto> authorizationGroupDtos) {
        Optional<AppUser> appUserOptional = repository.findById(userId);
        if(appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            List<AuthorizationGroup> authorizationGroups = new ArrayList<>();
            authorizationGroupDtos.forEach(authorizationGroupDto -> {
                AuthorizationGroup authorizationGroup = new AuthorizationGroup();
                authorizationGroup.setId(authorizationGroupDto.getId());
                authorizationGroup.setUuid(authorizationGroupDto.getUuid());
                authorizationGroups.add(authorizationGroup);
            });
            appUser.setAuthorizationGroups(new HashSet<>(authorizationGroups));
            return mapper.toDto(repository.save(appUser));
        }
        throw new RuntimeException("User does not exist");
    }

    @Override
    public List<AppUserDto> getUsersAgainstGroup(Long id) {
        AuthorizationGroup authorizationGroup = new AuthorizationGroup();
        authorizationGroup.setId(id);
        List<AppUser> appUsers = repository.findAllByAuthorizationGroupsIn(Arrays.asList(authorizationGroup));
        return mapper.toDtoList(appUsers);
    }
}

