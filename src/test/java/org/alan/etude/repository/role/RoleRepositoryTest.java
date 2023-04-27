package org.alan.etude.repository.role;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.alan.etude.config.QuerydslConfig;
import org.alan.etude.entity.member.Role;
import org.alan.etude.exception.notFoundException.RoleNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.alan.etude.factory.entity.RoleFactory.createRole;
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
