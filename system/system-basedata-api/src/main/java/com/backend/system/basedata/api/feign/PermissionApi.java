package com.backend.system.basedata.api.feign;

import com.backend.common.core.constaint.AppNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@FeignClient(contextId = "permissionApi", name = AppNameConstants.SYSTEM_BASEDATA)
public interface PermissionApi {
    String BASE_URL = "/feign/system/basedata/permission";

    @GetMapping(value = BASE_URL + "/getPermissionsByLoginName")
    Set<String> getPermissionsByLoginName(String loginName);
}
