package com.digitify.unit.user;

import com.digitify.dto.ApiResponse;
import com.digitify.unit.authorizationgroup.AuthorizationGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/api/user")
public class AppUserController {
    @Autowired
    private AppUserService service;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AppUserDto>>> getAllAppUser() {
        return new ResponseEntity<>(new ApiResponse<>(null, service.getAll()), HttpStatus.OK);
    }


    @GetMapping(path = "/{uuid}")
    @PreAuthorize("hasAnyAuthority('USER_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AppUserDto>> getAppUser(@PathVariable String uuid) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
    }

    @PostMapping(path = "/search/{page-no}/{page-size}")
    @PreAuthorize("hasAnyAuthority('USER_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<AppUserDto>>> searchAppUser(@PathVariable("page-no") int pageNo,
                                                                       @PathVariable("page-size") int pageSize,
                                                                       @RequestBody CleanAppUserDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER_WRITE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AppUserDto>> saveAppUser(@Valid @RequestBody AppUserDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{uuid}")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AppUserDto>> updateAppUser(@PathVariable String uuid, @RequestBody AppUserDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
    }

    @PatchMapping
    @PreAuthorize("hasAnyAuthority('USER_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AppUserDto>> partialUpdateAppUser(@Valid @RequestBody CleanAppUserDto partialDto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{uuid}")
    @PreAuthorize("hasAnyAuthority('USER_DELETE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AppUserDto>> deleteAppUser(@PathVariable String uuid) {
        service.delete(uuid);
        return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/group/{id}")
    @PreAuthorize("hasAnyAuthority('USER_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AppUserDto>>> getUsersAgainstGroup(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.getUsersAgainstGroup(id)), HttpStatus.OK);
    }

    @PatchMapping("/{user-id}/assign/groups")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AppUserDto>> assignGroupsToUser(@PathVariable("user-id") Long userId, @RequestBody List<AuthorizationGroupDto> authorizationGroupDtos) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.assignGroupsToUser(userId, authorizationGroupDtos)), HttpStatus.OK);
    }

}
