package com.chatboard.etude.entity.post;

import com.chatboard.etude.dto.post.PostUpdateRequestDto;
import com.chatboard.etude.entity.category.Category;
import com.chatboard.etude.entity.common.EntityDate;
import com.chatboard.etude.entity.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)  // for single query
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)  // for single query
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

    public Post(String title, String content, Long price, Member member, Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.member = member;
        this.category = category;
        this.images = new ArrayList<>();
        addImages(images);
    }

    // Image utilities.

    public ImageUpdatedResult update(PostUpdateRequestDto request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.price = request.getPrice();

        ImageUpdatedResult result =
                findImageUpdatedResult(request.getAddedImages(), request.getDeletedImages());

        addImages(result.getAddedImages());

        deleteImages(result.getDeletedImages());

        return result;
    }

    private void addImages(List<Image> added) {
        added.forEach(image -> {
            images.add(image);
            image.initPost(this);
        });
    }

    private void deleteImages(List<Image> deleted) {
        deleted.forEach(image -> this.images.remove(image));
    }

    private ImageUpdatedResult findImageUpdatedResult(
            List<MultipartFile> addImageFiles, List<Long> deletedImageIds) {

        List<Image> addedImages = convertImageFilesToImages(addImageFiles);
        List<Image> deletedImages = convertImageIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addImageFiles, addedImages, deletedImages);
    }

    // MultiPartFile to Image
    private List<Image> convertImageFilesToImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream()
                .map(imageFile -> new Image(imageFile.getOriginalFilename()))
                .collect(Collectors.toList());
    }

    // Ids to Images
    private List<Image> convertImageIdsToImages(List<Long> imageIds) {
        return imageIds.stream()
                .map(this::convertImageIdToImage)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Long to Image
    private Optional<Image> convertImageIdToImage(Long id) {
        return this.images.stream()
                .filter(image -> image.getId().equals(id))
                .findAny();
    }

    @Getter
    @AllArgsConstructor
    public static class ImageUpdatedResult {

        private List<MultipartFile> addedImageFiles;
        private List<Image> addedImages;
        private List<Image> deletedImages;
    }

}
