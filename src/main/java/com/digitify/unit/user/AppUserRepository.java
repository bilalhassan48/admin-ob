package com.digitify.unit.user;

import com.digitify.unit.authorizationgroup.AuthorizationGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByUuid(String uuid);

    Optional<AppUser> findByUuid(String uuid);

    List<AppUser> findAllByUuidIn(List<String> uuidList);

    void deleteAllByUuidIn(List<String> uuids);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndUuidIsNot(String username, String uuid);

    boolean existsByMobileNo(String mobileNo);

    boolean existsByMobileNoAndUuidIsNot(String mobileNo, String uuid);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUuidIsNot(String email, String uuid);

    List<AppUser> findAllByAuthorizationGroupsIn(List<AuthorizationGroup> authorizationGroups);

}