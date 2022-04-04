package com.chatboard.etude.repository.role;

import com.chatboard.etude.config.QuerydslConfig;
import com.chatboard.etude.entity.member.Role;
import com.chatboard.etude.exception.RoleNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.chatboard.etude.factory.entity.RoleFactory.createRole;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(QuerydslConfig.class)
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;
    @PersistenceContext
    EntityManager entityManager;

    private void clear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void createAndReadTest() {
        // given
        Role role = createRole();

        // when
        roleRepository.save(role);
        clear();

        // then
        Role foundRole = roleRepository.findById(role.getId())
                .orElseThrow(RoleNotFoundException::new);
        assertThat(foundRole.getId()).isEqualTo(role.getId());
    }

    @Test
    void deleteTest() {
        // given
        Role role = roleRepository.save(createRole());
        clear();

        // when
        roleRepository.delete(role);

        // then
        assertThatThrownBy(() -> roleRepository.findById(role.getId())
                .orElseThrow(RoleNotFoundException::new))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    void uniqueRoleTypeTest() {
        // given
        roleRepository.save(createRole());
        clear();

        // when, then
        assertThatThrownBy(() -> roleRepository.save(createRole()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
