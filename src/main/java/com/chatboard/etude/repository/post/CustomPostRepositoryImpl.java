package com.chatboard.etude.repository.post;

import com.chatboard.etude.dto.post.PostReadConditionDto;
import com.chatboard.etude.dto.post.PostSimpleDto;
import com.chatboard.etude.entity.post.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import static com.querydsl.core.types.Projections.constructor;

@Transactional(readOnly = true)
public class CustomPostRepositoryImpl
        extends QuerydslRepositorySupport
        implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomPostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<PostSimpleDto> findAllByCondition(PostReadConditionDto condition) {

        // page info
        Pageable pageable = PageRequest.of(condition.getPage(), condition.getSize());
        // search condition
        Predicate predicate = createPredicate(condition);

        //  return as page implement with find query and count query
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    // todo NonNull?
    private List<PostSimpleDto> fetchAll(Predicate predicate, Pageable pageable) {
        return Objects.requireNonNull(getQuerydsl()).applyPagination(   // build query with paging applied.
                pageable,
                jpaQueryFactory
                        // Projections.constructor
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

    private Long fetchCount(Predicate predicate) {
        return jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(predicate)
                .fetchOne();
    }

    // This method creates final Predicate.
    private Predicate createPredicate(PostReadConditionDto condition) {
        return new BooleanBuilder()
                .and(orConditionsByEqCategoryIds(condition.getCategoryId()))
                .and(orConditionsByEqMemberIds(condition.getMemberId()));
    }

    // List of categoryIds into query
    private Predicate orConditionsByEqCategoryIds(List<Long> categoryIds) {
        return orConditions(categoryIds, post.category.id::eq);
    }

    // List of MemberIds into query
    private Predicate orConditionsByEqMemberIds(List<Long> memberIds) {
        return orConditions(memberIds, post.member.id::eq);
    }

    // removing reduplication of conditions (::eq)
    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) {
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
