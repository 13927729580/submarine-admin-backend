package com.htnova.system.manage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.converter.DtoConverter;
import com.htnova.common.dto.Result;
import com.htnova.common.dto.XPage;
import com.htnova.common.converter.TreeConverter;
import com.htnova.system.manage.dto.PermissionDto;
import com.htnova.system.manage.entity.Permission;
import com.htnova.system.manage.mapstruct.PermissionMapStruct;
import com.htnova.system.manage.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @menu 权限
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 权限分页查询
     */
    @GetMapping("/list/page")
    public XPage<PermissionDto> findListByPage(PermissionDto permissionDto, XPage<Void> xPage) {
        IPage<Permission> permissionPage = permissionService.findPermissionList(permissionDto, xPage);
        return DtoConverter.toDto(permissionPage, PermissionMapStruct.class);
    }

    /**
     * 权限查询
     */
    @GetMapping("/list/all")
    public List<PermissionDto> findList(PermissionDto permissionDto) {
        List<Permission> permissionList = permissionService.findPermissionList(permissionDto);
        return DtoConverter.toDto(permissionList, PermissionMapStruct.class);
    }

    /**
     * 权限详情
     */
    @GetMapping("/detail")
    public PermissionDto getById(Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return DtoConverter.toDto(permission, PermissionMapStruct.class);
    }

    /**
     * 获取权限的tree
     * @return List<PermissionDto>: 返回值为list，可能有多个root节点
     */
    @GetMapping("/tree/list")
    public List<PermissionDto> getPermissionTree(PermissionDto permissionDto) {
        List<Permission> permissionList = permissionService.findPermissionList(permissionDto);
        List<Permission> treeList = TreeConverter.listToTree(permissionList);
        return DtoConverter.toDto(treeList, PermissionMapStruct.class);
    }

    /**
     * 权限保存
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody PermissionDto permissionDto) {
        permissionService.savePermission(DtoConverter.toEntity(permissionDto, PermissionMapStruct.class));
        return Result.build(ResultStatus.SAVE_SUCCESS);
    }

    /**
     * 模块权限保存
     */
    @PostMapping("/save/module")
    public Result<Void> saveModule(@RequestBody PermissionDto permissionDto) {
        Permission permission = DtoConverter.toEntity(permissionDto, PermissionMapStruct.class);
        permissionService.savePermission(permission);
        Map<String, String> map = new HashMap<>();
        map.put("查询", ".find");
        map.put("新增", ".add");
        map.put("编辑", ".edit");
        map.put("删除", ".del");
        map.forEach((name, value) -> {
            permissionService.savePermission(Permission.builder()
                .type(Permission.PermissionType.button)
                .name(name)
                .value(permission.getValue() + value)
                .pid(permission.getId())
                .build());
        });
        return Result.build(ResultStatus.SAVE_SUCCESS);
    }

    /**
     * 权限删除
     */
    @DeleteMapping("/del")
    public Result<Void> delete(@RequestBody PermissionDto permissionDto) {
        permissionService.deletePermission(permissionDto.getId());
        return Result.build(ResultStatus.DELETE_SUCCESS);
    }
}