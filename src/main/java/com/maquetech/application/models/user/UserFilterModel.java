package com.maquetech.application.models.user;

import com.maquetech.application.enums.user.UserTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilterModel {

    private UserTypeEnum type;
}
