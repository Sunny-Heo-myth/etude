package com.chatboard.etude.service.post;

import com.chatboard.etude.dto.post.*;
import com.chatboard.etude.entity.category.Category;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.post.Image;
import com.chatboard.etude.entity.post.Post;
import com.chatboard.etude.exception.CategoryNotFoundException;
import com.chatboard.etude.exception.MemberNotFoundException;
import com.chatboard.etude.exception.PostNotFoundException;
import com.chatboard.etude.repository.category.CategoryRepository;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.post.PostRepository;
import com.chatboard.etude.service.file.FileService;
import com.chatboard.etude.dto.post.page.PageMakerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public PostDto readPost(Long id) {
        return PostDto.toDto(postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new));
    }

    public PostListDto readAllPost(PostReadConditionDto condition) {
        return PostListDto.toDto(postRepository.findAllByCondition(condition));
    }

    public PageMakerDto readAllPostWithPage(PostReadConditionDto condition) {
        return new PageMakerDto(postRepository.findAllByCondition(condition));
    }

    @Transactional
    public PostCreateResponseDto createPost(PostCreateRequestDto request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        List<Image> images = request.getImages().stream()
                .map(image -> new Image(image.getOriginalFilename()))
                .collect(Collectors.toList());

        Post post = postRepository.save(
                new Post(request.getTitle(), request.getContent(), request.getPrice(),
                        member, category, images)
        );

        uploadPostImage(post.getImages(), request.getImages());
        return new PostCreateResponseDto(post.getId());
    }

    @Transactional
    @PreAuthorize("@postGuard.check(#id)")
    public void deletePost(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        deletePostImage(post.getImages());

        postRepository.delete(post);
    }

    @Transactional
    @PreAuthorize("@postGuard.check(#id)")
    public PostUpdateResponseDto updatePost(Long id, PostUpdateRequestDto request) {

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        Post.ImageUpdatedResult result = post.update(request);

        uploadPostImage(result.getAddedImages(), result.getAddedImageFiles());

        deletePostImage(result.getDeletedImages());

        return new PostUpdateResponseDto(id);
    }

    private void uploadPostImage(List<Image> images, List<MultipartFile> files) {
        IntStream.range(0, images.size())
                .forEach(num ->
                        // Save image files in the designated location with an unique name.
                        fileService.upload(files.get(num), images.get(num).getUniqueName())
                );
    }

    private void deletePostImage(List<Image> images) {
        // Delete image files in the designated location with an unique name.
        images.forEach(image -> fileService.delete(image.getUniqueName()));
    }
}
