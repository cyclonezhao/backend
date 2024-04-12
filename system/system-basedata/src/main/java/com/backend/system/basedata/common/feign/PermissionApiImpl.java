package com.backend.system.basedata.common.feign;

import com.backend.system.basedata.api.feign.PermissionApi;
import com.backend.system.basedata.permission.service.PermissionService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

@RestController
public class PermissionApiImpl implements PermissionApi {
    @Resource
    private PermissionService permissionService;

    @Override
    public Set<String> getPermissionsByLoginName(String loginName) {
        return permissionService.getPermissionsByLoginName(loginName);
    }
}
