package com.chatboard.etude.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReadConditionDto {

    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_MAX_SIZE = 50;
    @NotNull(message = "Enter page number.")
    @PositiveOrZero(message = "Enter not non-negative page number.")
    private Integer page;
    @NotNull(message = "Enter page size.")
    @Positive(message = "Enter not positive page size.")
    private Integer size;
    private String keyWord;
    private List<Long> categoryId = new ArrayList<>();
    private List<Long> memberId = new ArrayList<>();

    public void setSize(int size) {
        this.size = size < DEFAULT_SIZE || size > DEFAULT_MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = Arrays.stream(categoryId.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public void setMemberId(String memberId) {
        this.memberId = Arrays.stream(memberId.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

//    public Pageable makePageable(int direction, String... property) {
//        Sort.Direction dir = direction == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
//        return PageRequest.of(this.page - 1, this.size, dir, property);
//    }
}
