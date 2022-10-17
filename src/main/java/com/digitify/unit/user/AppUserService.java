package com.digitify.unit.user;

import com.digitify.framework.face.IService;
import com.digitify.unit.authorizationgroup.AuthorizationGroupDto;

import java.util.List;

public interface AppUserService extends IService<AppUserDto, CleanAppUserDto> {


    AppUserDto assignGroupsToUser(Long userId, List<AuthorizationGroupDto> authorizationGroupDtos);

    List<AppUserDto> getUsersAgainstGroup(Long id);
}