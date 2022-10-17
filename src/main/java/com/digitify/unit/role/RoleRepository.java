package com.digitify.unit.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByUuid(String uuid);

    Optional<Role> findByUuid(String uuid);

    List<Role> findAllByUuidIn(List<String> uuidList);

    void deleteAllByUuidIn(List<String> uuids);

    boolean existsByName(String name);
}