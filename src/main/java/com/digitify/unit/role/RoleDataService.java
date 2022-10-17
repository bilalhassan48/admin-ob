package com.digitify.unit.role;

import com.digitify.framework.face.DataService;
import com.digitify.unit.authority.AuthorityDto;

import java.util.List;


public interface RoleDataService extends DataService<RoleDto, CleanRoleDto> {


    List<AuthorityDto> getAuthoritiesAgainstRole(Long id);

    RoleDto assignAuthoritiesToRole(Long roleId, List<AuthorityDto> authorityDtos);
}