package com.chatboard.etude.aop;

import com.chatboard.etude.config.security.guard.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AssignMemberIdAspect {

    @Before("@annotation(com.chatboard.etude.aop.AssignMemberId)")  // pointcut
    public void assignMemberId(JoinPoint joinPoint) {   // advice (JoinPoint must be the first parameter.)
        // get argument type of this joinPoint (ex : PostCreateRequest)
        Arrays.stream(joinPoint.getArgs())
                // if there is method called "setMemberId" for each argument
                .forEach(request -> getMethod(request.getClass())
                        // invokeMethod with argument extracted from SecurityContextHolder
                        .ifPresent(setMemberId -> invokeMethod(request, setMemberId, AuthUtils.extractMemberId())));

    }

    // get methods from the target class with reflection
    private Optional<Method> getMethod(Class<?> targetClass) {
        try {
            return Optional.of(targetClass.getMethod("setMemberId", Long.class));
        }
        catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    // invoke Object's method with argument.
    private void invokeMethod(Object obj, Method method, Object... args) {
        try {
            method.invoke(obj, args);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
