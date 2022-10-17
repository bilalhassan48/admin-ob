package com.digitify.unit.authorizationgroup;

import com.digitify.framework.face.IService;
import com.digitify.unit.role.RoleDto;

import java.util.List;

public interface AuthorizationGroupService extends IService<AuthorizationGroupDto, CleanAuthorizationGroupDto> {

    List<RoleDto> getRolesAgainstGroup(Long id);

    AuthorizationGroupDto assignRolesToGroup(Long groupId, List<RoleDto> roleDtos);
}