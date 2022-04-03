package com.chatboard.etude.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReadCondition {

    @NotNull(message = "Enter page number.")
    @PositiveOrZero(message = "Enter not non-negative page number.")
    private Integer page;

    @NotNull(message = "Enter page size.")
    @Positive(message = "Enter not positive page size.")
    private Integer size;

    private List<Long> categoryId = new ArrayList<>();
    private List<Long> memberId = new ArrayList<>();

}
