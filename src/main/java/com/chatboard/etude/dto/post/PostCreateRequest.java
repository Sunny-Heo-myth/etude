package com.chatboard.etude.dto.post;

import com.chatboard.etude.entity.post.Image;
import com.chatboard.etude.entity.post.Post;
import com.chatboard.etude.exception.CategoryNotFoundException;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.repository.category.CategoryRepository;
import com.chatboard.etude.repository.member.MemberRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel(value = "post create request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @ApiModelProperty(value = "post title",
            notes = "Enter post title.",
            required = true,
            example = "my title")
    @NotBlank(message = "Enter post title.")
    private String title;

    @ApiModelProperty(value = "post content",
            notes = "Enter post content.",
            required = true,
            example = "my content")
    @NotBlank(message = "Enter post content.")
    private String content;

    @ApiModelProperty(value = "price",
            notes = "Enter price.",
            required = true,
            example = "50000")
    @NotNull(message = "Enter price.")
    @PositiveOrZero(message = "Enter above 0.")
    private Long price;

    @ApiModelProperty(hidden = true)
    @Null   // does not pass value from client but use token instead.
    private Long memberId;

    @ApiModelProperty(value = "category id",
            notes = "Enter category id.",
            required = true,
            example = "3")
    @NotNull(message = "Enter category id.")
    @PositiveOrZero(message = "Enter valid category id.")
    private Long categoryId;

    @ApiModelProperty(value = "image", notes = "Append image.")
    private List<MultipartFile> images = new ArrayList<>();

    public static Post toEntity(PostCreateRequest request,
                                MemberRepository memberRepository,
                                CategoryRepository categoryRepository) {
        return new Post(
                request.title,
                request.content,
                request.price,
                memberRepository.findById(request.getMemberId())
                        .orElseThrow(MemberNotFoundException::new),
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(CategoryNotFoundException::new),
                request.images.stream()
                        .map(file -> new Image(file.getOriginalFilename()))
                        .collect(Collectors.toList())
        );
    }

}
