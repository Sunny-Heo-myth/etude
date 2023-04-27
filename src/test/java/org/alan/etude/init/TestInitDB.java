package org.alan.etude.init;

import org.alan.etude.entity.category.Category;
import org.alan.etude.entity.member.Member;
import org.alan.etude.entity.member.Role;
import org.alan.etude.entity.member.RoleType;
import org.alan.etude.entity.message.Message;
import org.alan.etude.exception.notFoundException.MemberNotFoundException;
import org.alan.etude.exception.notFoundException.RoleNotFoundException;
import org.alan.etude.repository.category.CategoryRepository;
import org.alan.etude.repository.member.MemberRepository;
import org.alan.etude.repository.message.MessageRepository;
import org.alan.etude.repository.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class TestInitDB {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MessageRepository messageRepository;

    private final String adminEmail = "admin@admin.com";
    private final String member1Email = "member1@member.com";
    private final String member2Email = "member2@member.com";
    private final String password = "123456a!";

    @Transactional
    public void initDB() {
        initRole();
        initTestAdmin();
        initTestMember();
        initCategory();
        initMessage();
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
                new Member(adminEmail, encoder.encode(password), "admin", "admin",
                        List.of(
                                roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                                        .orElseThrow(RoleNotFoundException::new),
                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN)
                                        .orElseThrow(RoleNotFoundException::new)))
        );
    }

    private void initTestMember() {
        memberRepository.saveAll(
                List.of(
                        new Member(member1Email, encoder.encode(password), "member1", "member1",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                                        .orElseThrow(RoleNotFoundException::new))),
                        new Member(member2Email, encoder.encode(password), "member2", "member2",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)
                                        .orElseThrow(RoleNotFoundException::new)))
                )
        );
    }

    private void initCategory() {
        Category category1 = new Category("category1", null);
        Category category2 = new Category("category2", category1);
        categoryRepository.saveAll(List.of(category1, category2));
    }

    private void initMessage() {
        Member sender = memberRepository.findByEmail(getMember1Email()).orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByEmail(getMember2Email()).orElseThrow(MemberNotFoundException::new);
        IntStream.range(0, 5).forEach(i -> messageRepository.save(new Message("content" + i, sender, receiver)));
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getMember1Email() {
        return member1Email;
    }

    public String getMember2Email() {
        return member2Email;
    }

    public String getPassword() {
        return password;
    }

}
