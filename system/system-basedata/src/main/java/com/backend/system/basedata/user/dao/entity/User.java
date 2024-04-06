package com.backend.system.basedata.user.dao.entity;

import com.backend.common.base.BaseEntity;
import lombok.Data;

@Data
public class User extends BaseEntity {
    private String number;
    private String name;
}
