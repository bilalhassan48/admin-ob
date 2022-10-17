package com.digitify.unit.authorizationgroup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorizationGroupRepository extends JpaRepository<AuthorizationGroup, Long> {
    boolean existsByUuid(String uuid);

    Optional<AuthorizationGroup> findByUuid(String uuid);

    List<AuthorizationGroup> findAllByUuidIn(List<String> uuidList);

    void deleteAllByUuidIn(List<String> uuids);

    boolean existsByName(String name);

    AuthorizationGroup findByName(String name);

    //List<AuthorizationGroup> findAllByUsersIn(List<AppUser> appUsers);

    Optional<AuthorizationGroup> findById(Long id);

}