package com.digitify.unit.user;

import com.digitify.framework.face.DataService;
import com.digitify.unit.authorizationgroup.AuthorizationGroupDto;

import java.util.List;


public interface AppUserDataService extends DataService<AppUserDto, CleanAppUserDto> {


    AppUserDto assignGroupsToUser(Long userId, List<AuthorizationGroupDto> authorizationGroupDtos);

    List<AppUserDto> getUsersAgainstGroup(Long id);
}