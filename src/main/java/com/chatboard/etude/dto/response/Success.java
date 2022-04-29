package com.chatboard.etude.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Success<T> implements ResponseResult {
    private T data;
}
