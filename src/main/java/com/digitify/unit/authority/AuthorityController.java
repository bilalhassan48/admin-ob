package com.digitify.unit.authority;

import com.digitify.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/authority")
public class AuthorityController {
    @Autowired
    private AuthorityService service;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('AUTHORITY_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AuthorityDto>>> getAllAuthority() {
        log.info("In AuthorityController getAllAuthority method");
        return new ResponseEntity<>(new ApiResponse<>(null, service.getAll()), HttpStatus.OK);
    }


    /*@GetMapping(path = "/{uuid}")
    public ResponseEntity<ApiResponse<AuthorityDto>> getAuthority(@PathVariable String uuid) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
    }*/

    @PostMapping(path = "/search/{page-no}/{page-size}")
    @PreAuthorize("hasAnyAuthority('AUTHORITY_READ', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<AuthorityDto>>> searchAuthority(@PathVariable("page-no") int pageNo,
                                                                           @PathVariable("page-size") int pageSize,
                                                                           @RequestBody CleanAuthorityDto dto) {
        log.info("In AuthorityController searchAuthority method with pageNo {} and pageSize {}", pageNo, pageSize);
        return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
    }

    /*@PostMapping
    public ResponseEntity<ApiResponse<AuthorityDto>> saveAuthority(@Valid @RequestBody AuthorityDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{uuid}")
    public ResponseEntity<ApiResponse<AuthorityDto>> updateAuthority(@PathVariable String uuid, @RequestBody AuthorityDto dto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<AuthorityDto>> partialUpdateAuthority(@Valid @RequestBody CleanAuthorityDto partialDto) {
        return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<ApiResponse<AuthorityDto>> deleteAuthority(@PathVariable String uuid) {
        service.delete(uuid);
        return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
    }*/

}
