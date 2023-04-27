package learning.QueryDSLTest;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.alan.etude.dto.post.PostReadConditionDto;
import org.alan.etude.dto.post.PostSimpleDto;
import org.alan.etude.entity.post.Post;
import org.alan.etude.repository.post.CustomPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.chatboard.etude.entity.post.QPost.post;

@Transactional(readOnly = true)
public class CustomPostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    public CustomPostRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<PostSimpleDto> findAllByCondition(PostReadConditionDto condition) {
        Predicate predicate = createPredicate(condition);
        Pageable pageable = PageRequest.of(condition.getPage(), condition.getSize());
        return new PageImpl<>(
                fetchAll(predicate, pageable),
                pageable,
                fetchCount(predicate)
        );
    }

    private List<PostSimpleDto> fetchAll(Predicate predicate, Pageable pageable) {
        return Objects.requireNonNull(getQuerydsl()).applyPagination(
                pageable,
                queryFactory
                        .select(constructor(
                                PostSimpleDto.class,
                                post.id,
                                post.title,
                                post.member.nickname,
                                post.createdAt)
                        )
                        .from(post)
                        .join(post.member)
                        .where(predicate)
                        .orderBy(post.id.desc())
        ).fetch();
    }

    private Predicate createPredicate(PostReadConditionDto condition) {
        return new BooleanBuilder()
                .and(orConditionsByEqCategoryIds(condition.getCategoryId()))
                .and(orConditionsByEqMemberIds(condition.getMemberId()));
    }

    private Predicate orConditionsByEqCategoryIds(List<Long> categoryIds) {
        return orConditions(categoryIds, post.category.id::eq);
    }

    private Predicate orConditionsByEqMemberIds(List<Long> memberIds) {
        return orConditions(memberIds, post.member.id::eq);
    }

    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) {
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

    private Long fetchCount(Predicate predicate) {
        return queryFactory
                .select(post.count())
                .from(post)
                .where(predicate)
                .fetchOne();
    }

}
