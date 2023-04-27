package org.alan.etude.repository.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.alan.etude.config.QuerydslConfig;
import org.alan.etude.dto.post.PostUpdateRequestDto;
import org.alan.etude.entity.category.Category;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.post.Image;
import org.alan.etude.entity.post.Post;
import org.alan.etude.exception.notFoundException.PostNotFoundException;
import org.alan.etude.repository.category.CategoryRepository;
import org.alan.etude.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.alan.etude.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static org.alan.etude.factory.entity.CategoryFactory.createCategory;
import static org.alan.etude.factory.entity.ImageFactory.createImage;
import static org.alan.etude.factory.entity.ImageFactory.createImageWithOriginName;
import static org.alan.etude.factory.entity.MemberFactory.createMember;
import static org.alan.etude.factory.entity.PostFactory.createPost;
import static org.alan.etude.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(QuerydslConfig.class)
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageRepository imageRepository;
    @PersistenceContext
    EntityManager entityManager;

    Member member;
    Category category;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(createMember());
        category = categoryRepository.save(createCategory());
    }

    @Test
    void createAndReadTest() {
        // given
        Post post = postRepository.save(createPost(member, category));
        clear();

        // when
        Post foundPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFoundException::new);

        // then
        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void deleteTest() {
        // given
        Post post = postRepository.save(createPost(member, category));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then
        assertThatThrownBy(() -> postRepository.findById(post.getId())
                .orElseThrow(PostNotFoundException::new))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void createCascadeImageTest() {
        // given
        Post post = postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        Post foundPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFoundException::new);

        // then
        List<Image> images = foundPost.getImages();
        assertThat(images.size()).isEqualTo(2);
    }

    @Test
    void deleteCascadeByMemberTest() {
        // given
        postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        List<Post> result =postRepository.findAll();
        assertThat(result.size()).isZero();
    }

    @Test
    void deleteCascadeByCategoryTest() {
        // given
        postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        categoryRepository.deleteById(category.getId());
        clear();

        // then
        List<Post> result = postRepository.findAll();
        assertThat(result.size()).isZero();
    }

    @Test
    void findByIdWithMemberTest() {
        // given
        Post post = postRepository.save(createPost(member, category));

        // when
        Post foundPost = postRepository.findByIdWithMember(post.getId())
                .orElseThrow(PostNotFoundException::new);

        // then
        Member foundMember = foundPost.getMember();
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void updateTest() {
        // given
        Image image1 = createImageWithOriginName("image1.png");
        Image image2 = createImageWithOriginName("image2.png");
        Post post =postRepository.save(createPostWithImages(member, category, List.of(image1, image2)));
        clear();

        // when
        MockMultipartFile file = new MockMultipartFile(
                "image3", "image3.png", MediaType.IMAGE_PNG_VALUE, "image3File".getBytes());
        PostUpdateRequestDto postUpdateRequestDto = createPostUpdateRequest(
                "update title", "update content", 1234L, List.of(file), List.of(image1.getId()));
        Post foundPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFoundException::new);
        foundPost.update(postUpdateRequestDto);
        clear();

        // then
        Post result = postRepository.findById(post.getId())
                .orElseThrow(PostNotFoundException::new);
        assertThat(result.getTitle()).isEqualTo(postUpdateRequestDto.getTitle());
        assertThat(result.getContent()).isEqualTo(postUpdateRequestDto.getContent());
        assertThat(result.getPrice()).isEqualTo(postUpdateRequestDto.getPrice());

        List<Image> images = result.getImages();
        List<String> originNames = images.stream()
                .map(Image::getOriginName)
                .collect(Collectors.toList());
        assertThat(images.size()).isEqualTo(2);
        assertThat(originNames).contains(image2.getOriginName(), file.getOriginalFilename());

        List<Image> resultImages = imageRepository.findAll();
        assertThat(resultImages.size()).isEqualTo(2);
    }

    private void clear() {
        entityManager.flush();
        entityManager.clear();
    }
}
