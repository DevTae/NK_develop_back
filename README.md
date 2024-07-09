# NK에듀 학생 숙제 관리 시스템 백엔드 개발 프로젝트

- 프로젝트 설명
  - `NK인피니트영수전문학원`에서의 학생 숙제 관리 플랫폼을 위한 백엔드 프로젝트 저장소이다. 학생, 학부모, 관리자에 대한 데이터베이스 설정과 이에 대한 API 를 제공하고자 한다.

- API 명세서
  - [다음 링크](https://legend-sceptre-7da.notion.site/API-aaba977ac1354949b5228e3840e11dd1?pvs=4)에서 API 명세서 확인이 가능함.
  - 계정 및 수업, 공지, 숙제에 대한 세부 API 기능 및 역할을 정의하였다.

- 데이터베이스 ERD
  - 아래와 같은 ERD로 데이터베이스 시스템을 구성하였다.

    ![image](https://github.com/NKdevelop1/NK_develop_back/assets/55177359/0b82acb7-2954-49a5-9c4c-d3cc5cb21ad6)


- 개발 환경
  - Java 17
  - Java Spring Boot 3.2.1
  - Lombok 1.18.24
  - MySQL 5.7.44
    - MySQL Workbench 실행 → root 계정 로그인 → Create a new schema named `nkedu` using `utf8`
  - JPA

- 패키지 구조
  - `com.nkedu.back` : SpringBootApplication
  - `com.nkedu.back.api` : API 인터페이스
  - `com.nkedu.back.serviceImpl` : API 구현체 클래스
  - `com.nkedu.back.controller` : API 컨트롤러 클래스
    
  - `com.nkedu.back.repository` : 데이터베이스 레포지토리 클래스
  - `com.nkedu.back.entity` : 데이터베이스 엔터티 클래스
  - `com.nkedu.back.dto` : 데이터베이스 DTO (Data Transfer Object) 클래스

  - `com.nkedu.back.security` : Spring Security 관련 클래스
  - `com.nkedu.back.exception` : 예외처리 관련 클래스

- 프론트와의 협업을 위한 도커 이미지 제공
  1. `cd nkedu-back`
  2. `.\gradlew.bat clean build` (윈도우) / `./gradlew clean build` (맥북 M1 / 리눅스)
  3. `docker-compose up`
  4. `http://localhost:8080/` 링크를 바탕으로 테스트용 백엔드 서버 활용 가능

- 깃허브 관리
  - `develop` 브랜치를 바탕으로 기능마다 새로운 브랜치를 만들어 개발을 진행한다.
  - `Github Issue`, `Jira Ticket` 등을 바탕으로 브랜치 명칭을 설정하여 개발한 뒤, `pull` 후에 `develop` 브랜치에 병합한다.
