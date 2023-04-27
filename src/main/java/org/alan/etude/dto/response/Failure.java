package org.alan.etude.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Failure implements ResponseResult {

    private String message;
}
