package com.digitify.unit.authorizationgroup;

import com.digitify.dto.ApiResponse;
import com.digitify.unit.role.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/authorization-group")
public class AuthorizationGroupController {
    @Autowired
    private AuthorizationGroupService service;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('GROUP_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AuthorizationGroupDto>>> getAllAuthorizationGroup() {
        return new ResponseEntity<>(new ApiResponse<>(null, service.getAll()), HttpStatus.OK);
    }


    @GetMapping(path = "/{uuid}")
    @PreAuthorize("hasAnyAuthority('GROUP_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AuthorizationGroupDto>> getAuthorizationGroup(@PathVariable String uuid) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
    }

    @PostMapping(path = "/search/{page-no}/{page-size}")
    @PreAuthorize("hasAnyAuthority('GROUP_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<AuthorizationGroupDto>>> searchAuthorizationGroup(@PathVariable("page-no") int pageNo,
                                                                                             @PathVariable("page-size") int pageSize,
                                                                                             @RequestBody CleanAuthorizationGroupDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('GROUP_WRITE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AuthorizationGroupDto>> saveAuthorizationGroup(@Valid @RequestBody AuthorizationGroupDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{uuid}")
    @PreAuthorize("hasAnyAuthority('GROUP_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AuthorizationGroupDto>> updateAuthorizationGroup(@PathVariable String uuid, @RequestBody AuthorizationGroupDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
    }

    @PatchMapping
    @PreAuthorize("hasAnyAuthority('GROUP_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AuthorizationGroupDto>> partialUpdateAuthorizationGroup(@Valid @RequestBody CleanAuthorizationGroupDto partialDto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{uuid}")
    @PreAuthorize("hasAnyAuthority('GROUP_DELETE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AuthorizationGroupDto>> deleteAuthorizationGroup(@PathVariable String uuid) {
        service.delete(uuid);
        return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
    }

    /*@GetMapping("/user/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AuthorizationGroupDto>>> getGroupsAgainstUser(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.getGroupsAgainstUser(id)), HttpStatus.OK);
    }*/

    /*@PatchMapping("/{group-id}/assign/users")
    @PreAuthorize("hasAnyAuthority('GROUP_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AuthorizationGroupDto>> assignUsersToGroup(@PathVariable("group-id") Long groupId, @RequestBody List<AppUserDto> appUserDtos) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.assignUsersToGroup(groupId, appUserDtos)), HttpStatus.OK);
    }*/

    @GetMapping("/{id}/role")
    @PreAuthorize("hasAnyAuthority('ROLE_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<RoleDto>>> getRolesAgainstGroup(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.getRolesAgainstGroup(id)), HttpStatus.OK);
    }

    @PatchMapping("/{group-id}/assign/roles")
    @PreAuthorize("hasAnyAuthority('GROUP_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AuthorizationGroupDto>> assignRolesToGroup(@PathVariable("group-id") Long groupId, @RequestBody List<RoleDto> roleDtos) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.assignRolesToGroup(groupId, roleDtos)), HttpStatus.OK);
    }
}

