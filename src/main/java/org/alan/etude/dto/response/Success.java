package org.alan.etude.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Success<T> implements ResponseResult {
    private T data;
}
