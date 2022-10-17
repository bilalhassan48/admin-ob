package com.digitify.unit.authorizationgroup;

import com.digitify.enums.EntityType;
import com.digitify.framework.annotation.DataService;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.ErrorCode;
import com.digitify.framework.util.Rethrow;
import com.digitify.unit.role.Role;
import com.digitify.unit.role.RoleDto;
import com.digitify.unit.role.RoleMapper;
import com.digitify.unit.user.AppUser;
import com.digitify.unit.user.AppUserDto;
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
public class AuthorizationGroupDataServiceImpl implements AuthorizationGroupDataService {
    @Autowired
    private AuthorizationGroupRepository repository;
    @Autowired
    private AuthorizationGroupMapper mapper;

    @Autowired
    private AuthorizationGroupEntityValidator validator;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean existsByUuid(String uuid) {
        return repository.existsByUuid(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationGroupDto> getAll() {
        return Stream.ofNullable(repository.findAll()).flatMap(Collection::stream).map(entity -> mapper.toDto(entity)).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorizationGroupDto> getByUuid(String uuid) throws ApplicationUncheckException {
        return Optional.ofNullable(uuid)
                .filter(StringUtils::hasLength)
                .flatMap(s -> repository.findByUuid(s))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .map(entity -> mapper.toDto(entity));
    }

    @Override
    public Page<AuthorizationGroupDto> search(CleanAuthorizationGroupDto dto, PageRequest pageRequest) {
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
    public Optional<AuthorizationGroupDto> save(AuthorizationGroupDto dto) throws ApplicationUncheckException {
        return Optional.ofNullable(dto)
                .map(Rethrow.rethrowFunction(this::prepareEntityToSave))
                .map(entity -> repository.save(entity))
                .map(entity -> mapper.toDto(entity));
    }

    private AuthorizationGroup prepareEntityToSave(AuthorizationGroupDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.POST).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.AUTHORIZATIONGROUP.toString()).build();
        if (dto == null) {
            throw new ApplicationUncheckException(errorCode, "Invalid request");
        }
        Optional<AuthorizationGroup> optionalEntity = Optional.ofNullable(dto)
                .map(AuthorizationGroupDto::getUuid)
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
    public List<AuthorizationGroupDto> save(List<AuthorizationGroupDto> list) throws ApplicationUncheckException {
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
    public Optional<AuthorizationGroupDto> update(AuthorizationGroupDto dto) throws ApplicationUncheckException {
        return Optional.ofNullable(dto)
                .map(Rethrow.rethrowFunction(this::prepareEntityToUpdate))
                .map(entity -> repository.save(entity))
                .map(entity -> mapper.toDto(entity));
    }

    private AuthorizationGroup prepareEntityToUpdate(AuthorizationGroupDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PUT).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.AUTHORIZATIONGROUP.toString()).build();
        return Optional.of(dto)
                .map(AuthorizationGroupDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity)))
                .map(entity -> mapper.update(dto, entity))
                .orElseThrow(() -> new ApplicationUncheckException(errorCode, "unable to prepare entity to update."));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AuthorizationGroupDto> update(List<AuthorizationGroupDto> list) throws ApplicationUncheckException {
        return Optional.ofNullable(list)
                .filter(dtoList -> !dtoList.isEmpty())
                .map(Rethrow.rethrowFunction(dtoList -> dtoList.stream().map(Rethrow.rethrowFunction(this::prepareEntityToUpdate)).collect(Collectors.toList())))
                .map(entityList -> repository.saveAll(entityList))
                .map(entityList -> mapper.toDtoList(entityList))
                .orElse(new ArrayList<>());
    }

    @Override
    public Optional<AuthorizationGroupDto> partialUpdate(CleanAuthorizationGroupDto dto) throws ApplicationUncheckException {
        return Optional.ofNullable(dto)
                .map(Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate))
                .map(entity -> repository.save(entity))
                .map(entity -> mapper.toDto(entity));
    }

    private AuthorizationGroup prepareEntityToPartialUpdate(CleanAuthorizationGroupDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PATCH).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.AUTHORIZATIONGROUP.toString()).build();
        return Optional.ofNullable(dto)
                .map(CleanAuthorizationGroupDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity)))
                .map(entity -> mapper.partialUpdate(dto, entity))
                .orElseThrow(() -> new ApplicationUncheckException(errorCode, "Unable to prepare entity to partial update"));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AuthorizationGroupDto> partialUpdate(List<CleanAuthorizationGroupDto> list) throws ApplicationUncheckException {
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
    public void delete(AuthorizationGroupDto dto) throws ApplicationUncheckException {
        Optional.ofNullable(dto)
                .map(AuthorizationGroupDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .ifPresent(appUser -> repository.delete(appUser));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(List<AuthorizationGroupDto> list) throws ApplicationUncheckException {
        List<AuthorizationGroup> entityList = repository.findAllByUuidIn(
                Stream.ofNullable(list).flatMap(Collection::stream).map(AuthorizationGroupDto::getUuid).collect(Collectors.toList()));
        repository.deleteAllByUuidIn(Stream.ofNullable(entityList)
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .flatMap(Collection::stream)
                .map(AuthorizationGroup::getUuid)
                .collect(Collectors.toList()));
    }

    public AuthorizationGroupDto assignUsersToGroup(Long groupId, List<AppUserDto> appUserDtos) {
        Optional<AuthorizationGroup> authorizationGroupOptional = repository.findById(groupId);
        if(authorizationGroupOptional.isPresent()) {
            AuthorizationGroup authorizationGroup = authorizationGroupOptional.get();
            List<AppUser> appUsers = new ArrayList<>();
            appUserDtos.forEach(appUserDto -> {
                AppUser appUser = new AppUser();
                appUser.setId(appUserDto.getId());
                appUser.setUuid(appUserDto.getUuid());
                appUsers.add(appUser);
            });
            //authorizationGroup.setUsers(new HashSet<>(appUsers));
            return mapper.toDto(repository.save(authorizationGroup));
        }
        throw new RuntimeException("Group does not exist");
    }

    public List<RoleDto> getRolesAgainstGroup(Long id) {
        Optional<AuthorizationGroup> authorizationGroupOptional = repository.findById(id);
        AuthorizationGroup authorizationGroup;
        List<Role> roles = new ArrayList<>();
        if(authorizationGroupOptional.isPresent()) {
            authorizationGroup = authorizationGroupOptional.get();
            roles = authorizationGroup.getRoles().stream().collect(Collectors.toList());
        }
        return roleMapper.toDtoList(roles);
    }

    public AuthorizationGroupDto assignRolesToGroup(Long groupId, List<RoleDto> roleDtos) {
        Optional<AuthorizationGroup> authorizationGroupOptional = repository.findById(groupId);
        if(authorizationGroupOptional.isPresent()) {
            AuthorizationGroup authorizationGroup = authorizationGroupOptional.get();
            List<Role> roles = new ArrayList<>();
            roleDtos.forEach(roleDto -> {
                Role role = new Role();
                role.setId(roleDto.getId());
                role.setUuid(roleDto.getUuid());
                roles.add(role);
            });
            try {
                authorizationGroup.setRoles(new HashSet<>(roles));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mapper.toDto(repository.save(authorizationGroup));
        }
        throw new RuntimeException("Group does not exist");
    }
}

