package com.interview.security.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author kj
 * @Date 2020/4/28 16:33
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyUser implements Serializable {
    private String userName;
    private String password;
    private String authority;
}
