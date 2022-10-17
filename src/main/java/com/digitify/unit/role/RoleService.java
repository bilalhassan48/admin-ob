package com.digitify.unit.role;

import com.digitify.framework.face.IService;
import com.digitify.unit.authority.AuthorityDto;

import java.util.List;

public interface RoleService extends IService<RoleDto, CleanRoleDto> {


    List<AuthorityDto> getAuthoritiesAgainstRole(Long id);

    RoleDto assignAuthoritiesToRole(Long roleId, List<AuthorityDto> authorityDtos);
}