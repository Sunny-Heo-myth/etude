# Etude 2.0.0

상품 판매 게시판 api

## 개요
일대일 메세지 기능과 댓글 알람이 덧붙여진 상품 판매 게시판 api 입니다. 


## 요구사항
Java 17 이상<br>
스프링 6.0.0 이상<br>
스프링부트 3.0.0 이상

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

