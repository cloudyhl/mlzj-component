package com.mlzj.component.mq.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yhl
 * @date 2020/5/14
 */
@Data
@AllArgsConstructor
public class User implements Serializable {

    private String name;

    private Integer age;

    private Class aClass;
}
