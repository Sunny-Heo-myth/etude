package com.chatboard.etude.helper;

import com.chatboard.etude.exception.CannotConvertNestedStructureException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class NestedConvertHelper <K, E, D> {

    private final List<E> entities;
    private final Function<E, D> toDto;
    private final Function<E, E> getParent;
    private final Function<E, K> getKey;
    private final Function<D, List<D>> getChildren;

    private NestedConvertHelper(List<E> entities,
                               Function<E, D> toDto,
                               Function<E, E> getParent,
                               Function<E, K> getKey,
                               Function<D, List<D>> getChildren) {
        this.entities = entities;
        this.toDto = toDto;
        this.getParent = getParent;
        this.getKey = getKey;
        this.getChildren = getChildren;
    }

    public static <K, E, D> NestedConvertHelper newInstance(List<E> entities,
                                                            Function<E, D> toDto,
                                                            Function<E, E> getParent,
                                                            Function<E, K> getKey,
                                                            Function<D, List<D>> getChildren) {
        return new NestedConvertHelper(entities, toDto, getParent, getKey, getChildren);
    }

    public List<D> convert() {
        try {
            return convertInternal();
        }
        catch (NullPointerException e) {
            throw new CannotConvertNestedStructureException(e.getMessage());
        }
    }

    private List<D> convertInternal() {
        Map<K, D> map = new HashMap<>();
        List<D> roots = new ArrayList<>();

        for (E entity : entities) {
            D dto = toDto(entity);
            map.put(getKey(entity), dto);

            if (hasParent(entity)) {
                E parent = getParent(entity);
                K parentKey = getKey(parent);
                D parentDto = map.get(parentKey);
                getChildren(parentDto).add(dto);
            }
            else {
                roots.add(dto);
            }
        }
        return roots;
    }

    private boolean hasParent(E entity) {
        return getParent(entity) != null;
    }

    private E getParent(E entity) {
        return getParent.apply(entity);
    }

    private D toDto(E entity) {
        return toDto.apply(entity);
    }

    private K getKey(E entity) {
        return getKey.apply(entity);
    }

    private List<D> getChildren(D dto) {
        return getChildren.apply(dto);
    }
}
