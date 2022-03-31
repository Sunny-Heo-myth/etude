package com.chatboard.etude;

import com.chatboard.etude.entity.category.Category;
import com.chatboard.etude.entity.member.Member;
import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.entity.member.RoleType;
import com.chatboard.etude.exception.RoleNotFoundException;
import com.chatboard.etude.repository.category.CategoryRepository;
import com.chatboard.etude.repository.member.MemberRepository;
import com.chatboard.etude.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
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

    @EventListener(ApplicationReadyEvent.class) //
    @Transactional
    public void initDB() {
        log.info("initialize database.");

        initRole();
        initTestAdmin();
        initTestMember();
        initCategory();
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
                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                                .orElseThrow(RoleNotFoundException::new),
                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN)
                                        .orElseThrow(RoleNotFoundException::new)))
        );
    }

    private void initTestMember() {
        memberRepository.saveAll(
                List.of(
                new Member("member1@member.com",
                        passwordEncoder.encode("123456a!"),
                        "member1",
                        "member1nick",
                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                                .orElseThrow(RoleNotFoundException::new))),

                new Member("member2@member.com",
                        passwordEncoder.encode("123456a!"),
                        "member2",
                        "member2nick",
                        List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                                .orElseThrow(RoleNotFoundException::new)))
                )
        );
    }

    private void initCategory() {
        Category c1 = categoryRepository.save(new Category("category1", null));
        Category c2 = categoryRepository.save(new Category("category2", c1));
        Category c3 = categoryRepository.save(new Category("category3", c1));
        Category c4 = categoryRepository.save(new Category("category4", c2));
        Category c5 = categoryRepository.save(new Category("category5", c2));
        Category c6 = categoryRepository.save(new Category("category6", c4));
        Category c7 = categoryRepository.save(new Category("category7", c3));
        Category c8 = categoryRepository.save(new Category("category8", null));
    }
}
