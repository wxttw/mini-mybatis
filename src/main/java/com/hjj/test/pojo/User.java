package com.hjj.test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jay
 * @date 2024/4/13 19:11
 * @description: TODO
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private long id;
    private String username;
}
