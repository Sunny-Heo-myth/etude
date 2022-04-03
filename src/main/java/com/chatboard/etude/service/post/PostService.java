package com.chatboard.etude.service.post;

import com.chatboard.etude.dto.post.*;
import com.chatboard.etude.entity.post.Image;
import com.chatboard.etude.entity.post.Post;
import com.chatboard.etude.exception.PostNotFoundException;
import com.chatboard.etude.repository.category.CategoryRepository;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.post.PostRepository;
import com.chatboard.etude.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    @Transactional
    public PostCreateResponse create(PostCreateRequest request) {
        Post post = postRepository.save(
                PostCreateRequest.toEntity(
                        request,
                        memberRepository,
                        categoryRepository
                )
        );
        uploadImages(post.getImages(), request.getImages());
        return new PostCreateResponse(post.getId());
    }

    @Transactional
    public PostUpdateResponse update(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        Post.ImageUpdatedResult result = post.update(request);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return new PostUpdateResponse(id);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        deleteImages(post.getImages());
        postRepository.delete(post);
    }

    private void uploadImages(List<Image> images, List<MultipartFile> files) {
        IntStream.range(0, images.size())
                .forEach(num -> fileService.upload(files.get(num), images.get(num).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.forEach(image -> fileService.delete(image.getUniqueName()));
    }
}
