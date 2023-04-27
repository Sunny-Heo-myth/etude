package org.alan.etude.dto.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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

    // parsing query string
    public void setCategoryId(String categoryId) {
        if (StringUtils.isBlank(categoryId)) {
            this.categoryId = Arrays.asList(1L,2L,3L,4L,5L,6L,7L,8L);
        }
        else {
            this.categoryId = Arrays.stream(categoryId.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
    }

    // parsing query string
    public void setMemberId(String memberId) {
        if (StringUtils.isBlank(memberId)) {
            this.memberId = Arrays.asList(1L,2L,3L,4L,5L,6L,7L,8L);
        }
        else {
            this.memberId = Arrays.stream(memberId.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
    }

}
