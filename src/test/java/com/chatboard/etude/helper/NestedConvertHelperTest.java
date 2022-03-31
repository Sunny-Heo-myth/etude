package com.chatboard.etude.helper;

import com.chatboard.etude.exception.CannotConvertNestedStructureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NestedConvertHelperTest {

    private static class MyEntity {
        private final Long id;
        private final String name;
        private final MyEntity parent;

        public MyEntity(Long id, String name, MyEntity parent) {
            this.id = id;
            this.name = name;
            this.parent = parent;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public MyEntity getParent() {
            return parent;
        }
    }

    private static class MyDto {
        private final Long id;
        private final String name;
        private final List<MyDto> children;

        public MyDto(Long id, String name, List<MyDto> children) {
            this.id = id;
            this.name = name;
            this.children = children;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<MyDto> getChildren() {
            return children;
        }
    }

    @Test
    void convertTest() {
        // given
        // 1    null
        // 8    null
        // 2    1
        // 3    1
        // 4    2
        // 5    2
        // 7    3
        // 6    4
        MyEntity m1 = new MyEntity(1L, "myEntity1", null);
        MyEntity m8 = new MyEntity(8L, "myEntity8", null);
        MyEntity m2 = new MyEntity(2L, "myEntity2", m1);
        MyEntity m3 = new MyEntity(3L, "myEntity3", m1);
        MyEntity m4 = new MyEntity(4L, "myEntity4", m2);
        MyEntity m5 = new MyEntity(5L, "myEntity5", m2);
        MyEntity m7 = new MyEntity(7L, "myEntity7", m3);
        MyEntity m6 = new MyEntity(6L, "myEntity6", m4);
        List<MyEntity> myEntities = List.of(m1, m8, m2, m3, m4, m5, m7, m6);

        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                myEntities,
                e -> new MyDto(e.getId(), e.getName(), new ArrayList<>()),
                MyEntity::getParent,
                MyEntity::getId,
                MyDto::getChildren
        );

        // when
        List<MyDto> result = helper.convert();

        // then
        // 1
        //  2
        //   4
        //    6
        //   5
        //  3
        //   7
        // 8
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getChildren().size()).isEqualTo(2);
        assertThat(result.get(0).getChildren().get(0).getId()).isEqualTo(2);
        assertThat(result.get(0).getChildren().get(0).getChildren().size()).isEqualTo(2);
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getId()).isEqualTo(4);
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getChildren().size()).isEqualTo(1);
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getId()).isEqualTo(6);
        assertThat(result.get(0).getChildren().get(0).getChildren().get(1).getId()).isEqualTo(5);
        assertThat(result.get(0).getChildren().get(1).getId()).isEqualTo(3);
        assertThat(result.get(0).getChildren().get(1).getChildren().size()).isEqualTo(1);
        assertThat(result.get(0).getChildren().get(1).getChildren().get(0).getId()).isEqualTo(7);
        assertThat(result.get(1).getId()).isEqualTo(8);
        assertThat(result.get(1).getChildren().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Parent precede child.")
    void convertExceptionByNotOrderedValueTest() {
        // given
        // 1		NULL
        // 8		NULL
        // 3		1
        // 4		2
        // 2		1
        // 5		2
        // 7		3
        // 6		4
        MyEntity m1 = new MyEntity(1L,"myEntity1", null);
        MyEntity m8 = new MyEntity(8L,"myEntity8", null);
        MyEntity m2 = new MyEntity(2L,"myEntity2", m1);
        MyEntity m3 = new MyEntity(3L,"myEntity3", m1);
        MyEntity m4 = new MyEntity(4L,"myEntity4", m2);
        MyEntity m5 = new MyEntity(5L,"myEntity5", m2);
        MyEntity m7 = new MyEntity(7L,"myEntity7", m3);
        MyEntity m6 = new MyEntity(6L,"myEntity6", m4);

        List<MyEntity> myEntities = List.of(m1, m8, m3, m4, m2, m5, m7, m6);

        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                myEntities,
                e -> new MyDto(e.getId(), e.getName(), new ArrayList<>()),
                MyEntity::getParent,
                MyEntity::getId,
                MyDto::getChildren
        );

        // when, then
        assertThatThrownBy(helper::convert)
                .isInstanceOf(CannotConvertNestedStructureException.class);
    }

    @Test
    @DisplayName("Root comes first.")
    void convertExceptionByNotOrderedValueNullsLastTest() {
        // given
        // 2		1
        // 3		1
        // 4		2
        // 5		2
        // 7		3
        // 6		4
        // 1		NULL
        // 8		NULL
        MyEntity m1 = new MyEntity(1L,"myEntity1", null);
        MyEntity m8 = new MyEntity(8L,"myEntity8", null);
        MyEntity m2 = new MyEntity(2L,"myEntity2", m1);
        MyEntity m3 = new MyEntity(3L,"myEntity3", m1);
        MyEntity m4 = new MyEntity(4L,"myEntity4", m2);
        MyEntity m5 = new MyEntity(5L,"myEntity5", m2);
        MyEntity m7 = new MyEntity(7L,"myEntity7", m3);
        MyEntity m6 = new MyEntity(6L,"myEntity6", m4);

        List<MyEntity> myEntities = List.of(m2, m3, m4, m5, m7, m6, m1, m8);

        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                myEntities,
                e -> new MyDto(e.getId(), e.getName(), new ArrayList<>()),
                MyEntity::getParent,
                MyEntity::getId,
                MyDto::getChildren
        );

        // when, then
        assertThatThrownBy(helper::convert)
                .isInstanceOf(CannotConvertNestedStructureException.class);
    }


}
