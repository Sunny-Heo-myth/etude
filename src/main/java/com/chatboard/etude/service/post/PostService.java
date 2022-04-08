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

    public PostDto read(Long id) {
        return PostDto.toDto(postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new));
    }

    public PostListDto readAll(PostReadCondition condition) {
        return PostListDto.toDto(
                postRepository.findAllByCondition(condition)
        );
    }

    @Transactional
    public PostCreateResponse create(PostCreateRequest request) {

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

        uploadImages(post.getImages(), request.getImages());
        return new PostCreateResponse(post.getId());
    }

    @Transactional
    @PreAuthorize("@postGuard.check(#id)")
    public void delete(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        deleteImages(post.getImages());

        postRepository.delete(post);
    }

    @Transactional
    @PreAuthorize("@postGuard.check(#id)")
    public PostUpdateResponse update(Long id, PostUpdateRequest request) {

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        Post.ImageUpdatedResult result = post.update(request);

        uploadImages(result.getAddedImages(), result.getAddedImageFiles());

        deleteImages(result.getDeletedImages());

        return new PostUpdateResponse(id);
    }

    private void uploadImages(List<Image> images, List<MultipartFile> files) {
        IntStream.range(0, images.size())
                .forEach(num -> fileService.upload(files.get(num), images.get(num).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.forEach(image -> fileService.delete(image.getUniqueName()));
    }
}
