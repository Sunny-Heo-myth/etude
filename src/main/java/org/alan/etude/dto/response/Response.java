package org.alan.etude.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response {

    private boolean success;
    private int code;
    private ResponseResult responseResult;

    // Thoroughly defining constructor pattern.
    public static Response success() {
        return new Response(true, 0, null);
    }

    public static <T> Response success(T data) {
        // Success is a subClass of Result.
        return new Response(true, 0, new Success<>(data));
    }

    public static Response failure(int code, String message) {
        // Failure is a subClass of Result.
        return new Response(false, code, new Failure(message));
    }
}
