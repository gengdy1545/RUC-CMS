package com.ruoyi.common.core.domain.model;

import lombok.Data;

/**
 * 用户注册对象
 * 
 * @author ruoyi
 */
@Data
public class RegisterBody extends LoginBody
{
    private String email;
    private String phone;
}
