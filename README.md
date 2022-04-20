# Etude 1.0.0

상품 판매 게시판 api

## 개요
일대일 메세지 기능 그리고 댓글 알람이 가능한 상품 판매 게시판 api


## 요구사항
JDK 11 이상<br>
스프링 2.6.5 이상

## 설치

1. main 클래스 manifest

```
tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.chatboard.etude.EtudeApplication.java"
    }
}
```

```
tasks.bootJar {
    mainClass = 'com.chatboard.etude.EtudeApplication'
}
```

2. Jar 파일 빌드
```
$ ./gradlew bootJar
```

3. java -jar 옵션으로 실행

```
$ java -jar etude-0.0.1-SNAPSHOT.jar
```

## 설치시 유의사항


## 빌드
* [Spring Boot](https://spring.io/projects/spring-boot) - Web Application Framework
* [Gradle](https://gradle.org/) - Dependency Management

## 버전 관리
1.0.0 

## 작성자

* **허선영**

## 라이선스

이 프로젝트는 MIT License 의 적용을 받습니다.- 자세한 내용은 [LICENSE.md](LICENSE.md) 을 참고하십시오.

