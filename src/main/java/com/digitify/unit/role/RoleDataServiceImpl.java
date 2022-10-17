package com.digitify.unit.role;

import com.digitify.enums.EntityType;
import com.digitify.framework.annotation.DataService;
import com.digitify.framework.enums.ApiType;
import com.digitify.framework.enums.LayerType;
import com.digitify.framework.enums.RequestType;
import com.digitify.framework.exception.ApplicationUncheckException;
import com.digitify.framework.handler.ErrorCode;
import com.digitify.framework.util.Rethrow;
import com.digitify.unit.authority.Authority;
import com.digitify.unit.authority.AuthorityDto;
import com.digitify.unit.authority.AuthorityMapper;
import com.digitify.unit.authority.AuthorityRepository;
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
public class RoleDataServiceImpl implements RoleDataService {
    @Autowired
    private RoleRepository repository;
    @Autowired
    private RoleMapper mapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Autowired
    private RoleEntityValidator validator;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public boolean existsByUuid(String uuid) {
        return repository.existsByUuid(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAll() {
        return Stream.ofNullable(repository.findAll()).flatMap(Collection::stream).map(entity -> mapper.toDto(entity)).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDto> getByUuid(String uuid) throws ApplicationUncheckException {
        return Optional.ofNullable(uuid)
                .filter(StringUtils::hasLength)
                .flatMap(s -> repository.findByUuid(s))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .map(entity -> mapper.toDto(entity));
    }

    @Override
    public Page<RoleDto> search(CleanRoleDto dto, PageRequest pageRequest) {
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
    public Optional<RoleDto> save(RoleDto dto) throws ApplicationUncheckException {
        return Optional.ofNullable(dto)
                .map(Rethrow.rethrowFunction(this::prepareEntityToSave))
                .map(entity -> repository.save(entity))
                .map(entity -> mapper.toDto(entity));
    }

    private Role prepareEntityToSave(RoleDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.POST).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.ROLE.toString()).build();
        if (dto == null) {
            throw new ApplicationUncheckException(errorCode, "Invalid request");
        }
        Optional<Role> optionalEntity = Optional.ofNullable(dto)
                .map(RoleDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)));
        if (optionalEntity.isPresent()) {
            throw new ApplicationUncheckException(errorCode, "Already exists");
        }

        if(dto.getAuthorities() != null) {
            List<Long> authorityIds = dto.getAuthorities().stream().map(AuthorityDto::getId).collect(Collectors.toList());
            List<Authority> authorities = authorityRepository.findAllByIdIn(authorityIds);
            dto.setAuthorities(authorityMapper.toDtoList(authorities));
        }
        return mapper.toEntity(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<RoleDto> save(List<RoleDto> list) throws ApplicationUncheckException {
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
    public Optional<RoleDto> update(RoleDto dto) throws ApplicationUncheckException {
        return Optional.ofNullable(dto)
                .map(Rethrow.rethrowFunction(this::prepareEntityToUpdate))
                .map(entity -> repository.save(entity))
                .map(entity -> mapper.toDto(entity));
    }

    private Role prepareEntityToUpdate(RoleDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PUT).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.ROLE.toString()).build();
        return Optional.of(dto)
                .map(RoleDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity)))
                .map(entity -> mapper.update(dto, entity))
                .orElseThrow(() -> new ApplicationUncheckException(errorCode, "unable to prepare entity to update."));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<RoleDto> update(List<RoleDto> list) throws ApplicationUncheckException {
        return Optional.ofNullable(list)
                .filter(dtoList -> !dtoList.isEmpty())
                .map(Rethrow.rethrowFunction(dtoList -> dtoList.stream().map(Rethrow.rethrowFunction(this::prepareEntityToUpdate)).collect(Collectors.toList())))
                .map(entityList -> repository.saveAll(entityList))
                .map(entityList -> mapper.toDtoList(entityList))
                .orElse(new ArrayList<>());
    }

    @Override
    public Optional<RoleDto> partialUpdate(CleanRoleDto dto) throws ApplicationUncheckException {
        return Optional.ofNullable(dto)
                .map(Rethrow.rethrowFunction(this::prepareEntityToPartialUpdate))
                .map(entity -> repository.save(entity))
                .map(entity -> mapper.toDto(entity));
    }

    private Role prepareEntityToPartialUpdate(CleanRoleDto dto) throws ApplicationUncheckException {
        ErrorCode errorCode = ErrorCode.with(ApiType.UNIT).with(RequestType.PATCH).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.ROLE.toString()).build();
        return Optional.ofNullable(dto)
                .map(CleanRoleDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity)))
                .map(entity -> mapper.partialUpdate(dto, entity))
                .orElseThrow(() -> new ApplicationUncheckException(errorCode, "Unable to prepare entity to partial update"));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<RoleDto> partialUpdate(List<CleanRoleDto> list) throws ApplicationUncheckException {
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
    public void delete(RoleDto dto) throws ApplicationUncheckException {
        Optional.ofNullable(dto)
                .map(RoleDto::getUuid)
                .filter(StringUtils::hasLength)
                .flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .ifPresent(appUser -> repository.delete(appUser));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(List<RoleDto> list) throws ApplicationUncheckException {
        List<Role> entityList = repository.findAllByUuidIn(
                Stream.ofNullable(list).flatMap(Collection::stream).map(RoleDto::getUuid).collect(Collectors.toList()));
        repository.deleteAllByUuidIn(Stream.ofNullable(entityList)
                .filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity)))
                .flatMap(Collection::stream)
                .map(Role::getUuid)
                .collect(Collectors.toList()));
    }

    @Override
    public List<AuthorityDto> getAuthoritiesAgainstRole(Long id) {
        Optional<Role> roleOptional = repository.findById(id);
        Role role;
        List<Authority> authorities = new ArrayList<>();
        if(roleOptional.isPresent()) {
            role = roleOptional.get();
            authorities = role.getAuthorities().stream().collect(Collectors.toList());
        }
        return authorityMapper.toDtoList(authorities);
    }

    @Override
    public RoleDto assignAuthoritiesToRole(Long roleId, List<AuthorityDto> authorityDtos) {
        Optional<Role> roleOptional = repository.findById(roleId);
        if(roleOptional.isPresent()) {
            Role role = roleOptional.get();
            List<Authority> authorities = new ArrayList<>();
            authorityDtos.forEach(authorityDto -> {
                Authority authority = new Authority();
                authority.setId(authorityDto.getId());
                authority.setUuid(authorityDto.getUuid());
                authorities.add(authority);
            });
            role.setAuthorities(new HashSet<>(authorities));
            return mapper.toDto(repository.save(role));
        }
        throw new RuntimeException("Role does not exist");
    }
}

