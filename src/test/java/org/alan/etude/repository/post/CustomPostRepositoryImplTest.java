package org.alan.etude.repository.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.alan.etude.config.QuerydslConfig;
import org.alan.etude.dto.post.PostReadConditionDto;
import org.alan.etude.dto.post.PostSimpleDto;
import org.alan.etude.entity.category.Category;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.post.Post;
import org.alan.etude.repository.category.CategoryRepository;
import org.alan.etude.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.alan.etude.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static org.alan.etude.factory.entity.CategoryFactory.createCategoryWithName;
import static org.alan.etude.factory.entity.MemberFactory.createMember;
import static org.alan.etude.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)   // @Import manually register bean for test environment.
public class CustomPostRepositoryImplTest {

    @Autowired PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @PersistenceContext EntityManager entityManager;

    @Test
    void findAllByConditionTest() {
        // given
        List<Member> members = saveMembers(3);
        List<Category> categories = saveCategories(2);

        // 0 - (m0, c0)
        // 1 - (m1, c1)
        // 2 - (m2, c0)
        // 3 - (m0, c1)
        // 4 - (m1, c0)
        // 5 - (m2, c1)
        // 6 - (m0, c0)
        // 7 - (m1, c1)
        // 8 - (m2, c0)
        // 9 - (m0, c1)
        List<Post> posts = IntStream.range(0, 10)
                .mapToObj(num -> postRepository.save(createPost(
                        members.get(num % 3),
                        categories.get(num % 2))))
                .collect(Collectors.toList());

        clear();

        List<Long> categoryIds = List.of(categories.get(1).getId());
        List<Long> memberIds = List.of(members.get(0).getId(), members.get(2).getId());
        int sizePerPage = 2;
        int expectedTotalElements = 3;

        PostReadConditionDto page0Condition = createPostReadCondition(0, sizePerPage, categoryIds, memberIds);
        PostReadConditionDto page1Condition = createPostReadCondition(1, sizePerPage, categoryIds, memberIds);

        // when
        Page<PostSimpleDto> page0 = postRepository.findAllByCondition(page0Condition);
        Page<PostSimpleDto> page1 = postRepository.findAllByCondition(page1Condition);

        // then
        assertThat(page0.getTotalElements()).isEqualTo(expectedTotalElements);
        assertThat(page0.getTotalPages()).isEqualTo((expectedTotalElements + 1) / sizePerPage);
        assertThat(page0.getContent().size()).isEqualTo(2);
        assertThat(page1.getContent().size()).isEqualTo(1);

        assertThat(page0.getContent().get(0).getId()).isEqualTo(posts.get(9).getId());
        assertThat(page0.getContent().get(1).getId()).isEqualTo(posts.get(5).getId());
        assertThat(page0.hasNext()).isTrue();

        assertThat(page1.getContent().get(0).getId()).isEqualTo(posts.get(3).getId());
        assertThat(page1.hasNext()).isFalse();
    }

    // auxiliary

    private void clear() {
        entityManager.flush();
        entityManager.clear();
    }

    private List<Member> saveMembers(int size) {
        return IntStream.range(0, size)
                .mapToObj(num -> memberRepository.save(createMember(
                        "member" + num + "@email.com",
                        "member" + num + "password",
                        "member" + num + "name",
                        "member" + num + "nickname")))
                .collect(Collectors.toList());
    }

    private List<Category> saveCategories(int size) {
        return IntStream.range(0, size)
                .mapToObj(num -> categoryRepository.save(createCategoryWithName("category" + num)))
                .collect(Collectors.toList());
    }
}
