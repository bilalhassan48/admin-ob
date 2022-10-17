package com.digitify.unit.role;

import com.digitify.dto.ApiResponse;
import com.digitify.unit.authority.AuthorityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/api/role")
public class RoleController {
    @Autowired
    private RoleService service;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRole() {
        return new ResponseEntity<>(new ApiResponse<>(null, service.getAll()), HttpStatus.OK);
    }


    @GetMapping(path = "/{uuid}")
    @PreAuthorize("hasAnyAuthority('ROLE_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<RoleDto>> getRole(@PathVariable String uuid) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
    }

    @PostMapping(path = "/search/{page-no}/{page-size}")
    @PreAuthorize("hasAnyAuthority('ROLE_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<RoleDto>>> searchRole(@PathVariable("page-no") int pageNo,
                                                                 @PathVariable("page-size") int pageSize,
                                                                 @RequestBody CleanRoleDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_WRITE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<RoleDto>> saveRole(@Valid @RequestBody RoleDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{uuid}")
    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(@PathVariable String uuid, @RequestBody RoleDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
    }

    @PatchMapping
    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<RoleDto>> partialUpdateRole(@Valid @RequestBody CleanRoleDto partialDto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{uuid}")
    @PreAuthorize("hasAnyAuthority('ROLE_DELETE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<RoleDto>> deleteRole(@PathVariable String uuid) {
        service.delete(uuid);
        return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/authority")
    @PreAuthorize("hasAnyAuthority('AUTHORITY_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AuthorityDto>>> getAuthoritiesAgainstRole(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.getAuthoritiesAgainstRole(id)), HttpStatus.OK);
    }

    @PatchMapping("/{role-id}/assign/authorities")
    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<RoleDto>> assignAuthoritiesToRole(@PathVariable("role-id") Long roleId, @RequestBody List<AuthorityDto> authorityDtos) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.assignAuthoritiesToRole(roleId, authorityDtos)), HttpStatus.OK);
    }

}
