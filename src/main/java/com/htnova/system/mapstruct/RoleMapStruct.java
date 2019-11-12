package com.htnova.system.mapstruct;

import com.htnova.common.base.BaseMapStruct;
import com.htnova.system.dto.PermissionDto;
import com.htnova.system.dto.RoleDto;
import com.htnova.system.entity.Permission;
import com.htnova.system.entity.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface RoleMapStruct extends BaseMapStruct<RoleDto, Role> {

    @AfterMapping // or @BeforeMapping
    default void toEntityMenuList(Role role, @MappingTarget RoleDto roleDto) {
        if(!CollectionUtils.isEmpty(roleDto.getPermissionList())) {
            Map<Permission.PermissionType, List<PermissionDto>> permissionTypeListMap = roleDto.getPermissionList().stream().collect(Collectors.groupingBy(PermissionDto::getType));
            roleDto.setMenuList(permissionTypeListMap.get(Permission.PermissionType.menu));
            roleDto.setPermissionList(permissionTypeListMap.get(Permission.PermissionType.button));
        }
    }
}