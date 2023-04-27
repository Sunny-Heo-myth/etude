package org.alan.etude;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alan.etude.entity.category.Category;
import org.alan.etude.entity.comment.Comment;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.member.Role;
import org.alan.etude.entity.member.RoleType;
import org.alan.etude.entity.post.Post;
import org.alan.etude.exception.notFoundException.RoleNotFoundException;
import org.alan.etude.repository.category.CategoryRepository;
import org.alan.etude.repository.comment.CommentRepository;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.post.PostRepository;
import org.alan.etude.repository.role.RoleRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("local")   // This class is registered as Bean only when active profile  is local.
public class InitDB {

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // after ApplicationReadyEvent call initDB()
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        

        initRole();
        initTestAdmin();
        initTestMember();
        initCategory();
        initPost();
        initComment();
        log.info("database initialized.");

    }

    private void initRole() {
        roleRepository.saveAll(
                Stream.of(RoleType.values())
                        .map(Role::new)
                        .collect(Collectors.toList())
        );
    }

    private void initTestAdmin() {
        memberRepository.save(
                new Member("admin@admin.com",
                        passwordEncoder.encode("123456!a"),
                        "admin",
                        "nickAdmin",
                        roleRepository.findAll()
                )
        );
    }

    private void initTestMember() {
        List<Member> memberList = new ArrayList<>();
        IntStream.range(0, 10)
                .forEach(i -> memberList.add(new Member(
                        "member" + i + "@email.com",
                        passwordEncoder.encode("123456!a"),
                        "userName" + i,
                        "memberNickname" + i,
                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                                .orElseThrow(RoleNotFoundException::new)))
                        )
                );
        memberRepository.saveAll(memberList);
    }

    private void initCategory() {
        Category c1 = new Category("category1", null);
        Category c2 = new Category("category2", c1);
        Category c3 = new Category("category3", c1);
        Category c4 = new Category("category4", c2);
        Category c5 = new Category("category5", c2);
        Category c6 = new Category("category6", c4);
        Category c7 = new Category("category7", c3);
        Category c8 = new Category("category8", null);
        List<Category> categoryList = List.of(c1, c2, c3, c4, c5, c6, c7, c8);
        categoryRepository.saveAll(categoryList);
    }

    private void initPost() {
        List<Member> memberList = memberRepository.findAll();
        List<Category> categoryList = categoryRepository.findAll();
        int memberListSize = memberList.size();
        int categoryListSize = categoryList.size();
        List<Post> postList = new ArrayList<>();
        IntStream.range(0, 400)
                .forEach(i -> postList.add(new Post(
                        "title" + i,
                        "content" + i,
                        (long) i * 1000000 + 4000000000L,
                        memberList.get(i % memberListSize),
                        categoryList.get(i % categoryListSize),
                        List.of()))
                );
        postRepository.saveAll(postList);
    }

    private void initComment() {
        List<Member> memberList = memberRepository.findAll();
        List<Post> postList = postRepository.findAll();
        int memberListSize = memberList.size();
        int postListSize = postList.size();
        List<Comment> parentCommentList = new ArrayList<>();
        IntStream.range(0, 1000)
                .forEach(i -> parentCommentList.add(new Comment(
                        "This is comment content" + i,
                        memberList.get(i % memberListSize),
                        postList.get(i % postListSize),
                        null))
                );
        commentRepository.saveAll(parentCommentList);
    }
}
