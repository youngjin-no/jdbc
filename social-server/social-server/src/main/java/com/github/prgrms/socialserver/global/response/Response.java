package com.github.prgrms.socialserver.global.response;

import static com.github.prgrms.socialserver.global.response.Response.Result.FAIL;
import static com.github.prgrms.socialserver.global.response.Response.Result.SUCCESS;

/**
 *  이시안님 코드를 참조했습니다
 */
public class Response<T, R> {
    private final Result result;
    private final T data;
    private final R message;

    private Response(Result result, T data, R message) {
        this.result = result;
        this.data = data;
        this.message = message;
    }

    public static <T> Response success(T data) {
        return new Response(SUCCESS, data, null);
    }

    public static <R> Response fail(R message) {
        return new Response(FAIL, null, message);
    }

    public enum Result {
        SUCCESS, FAIL
    }

    @Override
    public String toString() {
        return "Response{" +
            "result=" + result +
            ", data=" + data +
            ", message=" + message +
            '}';
    }

    public Result getResult() {
        return result;
    }

    public T getData() {
        return data;
    }

    public R getMessage() {
        return message;
    }
}
