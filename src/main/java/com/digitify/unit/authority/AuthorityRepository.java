package com.digitify.unit.authority;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    boolean existsByUuid(String uuid);

    Optional<Authority> findByUuid(String uuid);

    List<Authority> findAllByIdIn(List<Long> idList);

    List<Authority> findAllByUuidIn(List<String> uuidList);

    void deleteAllByUuidIn(List<String> uuids);

    List<Authority> findAllByIsAdminTrue();

}