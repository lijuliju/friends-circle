package com.friends.result;

import lombok.Data;

/**
 * @author Evan
 * @date 2019/4
 */
@Data
public class Result {
    private Long status;
    private String statusText;
    private Object result;

    Result(Long status, String statusText, Object data) {
        this.status = status;
        this.statusText = statusText;
        this.result = data;
    }
}
