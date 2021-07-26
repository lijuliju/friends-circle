package com.friends.result;

/**
 * @author Evan
 * @date 2019/4
 */
public enum ResultCode {
    SUCCESS(0),
    FAIL(10000),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    public long code;

    ResultCode(long code) {
        this.code = code;
    }
}
