# Spring-boot-security

![](https://img.shields.io/badge/Spring--boot-2.5.2-brightgreen?logo=spring)![](https://img.shields.io/badge/Thymeleaf-2.5.2-green?logo=thymeleaf)![](https://img.shields.io/badge/Lombok-1.18.20-white?logl=lombok)![](https://img.shields.io/badge/JUnit-4.12-red)![](https://img.shields.io/badge/MariaDB-10.6.3-%231F305F?logo=mariadb)



## 구현 내용

1. MariaDB 설계
   - JPA를 이용해서 설계
   - 스프링부트 실행시 data.sql 실행으로 데이터 생성
     - Role과 2명의 테스트 유저 (관리자와 일반 유저)
2. 기능
   1. 회원 가입 / 탈퇴 / 정보 수정
      - 웹 화면에서 기능 구현 (예외 처리 X)
   2. 로그인 / 로그아웃
      - 로그인 시 Access Token 및 Refresh Token 발행 후 Cookie에 저장
        - Refresh Token은 구현 못함
      - 로그아웃 할 경우 Cookie에 저장된 Token을 전부 만료
   3. 비디오 파일 업로드 
      - 비디오 파일 업로드시 프로젝트 폴더 밑에 Videos 파일을 생성하고 그곳에 저장
      - 100MB가 초과하는 비디오 업로드시 Exception 발생 (따로 예외처리는 못했습니다.)
   4. 비디오 파일 재생
      - 스트림을 요청하는 릴레이 방식으로 구현
      - 게시글 클릭할 경우 비디오 재생 화면이 나오는데 작성자 및 관리자만 접근할 수 있도록 함
      - 비디오 재생 화면 접근시 자동 플레이
   5. 리소스 권한 제어
      - Spring Security와 Token을 이용하여 구현
      - 타 유저의 리소스 조회 등 접근 권한 제어
        - 타 유저가 작성한 글은 접근 불가 및 관리자만 접근 할 수 있는 등 접근 권한 제어 추가
   6. 통계 관련 API
      - 게시판 메인에서 관리자만 통계 버튼에 접근할 수 있도록 설정
      - 통계 화면에 들어가서 날짜 지정후 검색하면 몇 명인지와 해당 유저들이 테이블 리스트로 나옴
   7. 로깅
      - AOP를 이용해 각 메서드의 실행 시간 체크 (병목현상 확인)
      - slf4j 라이브러리를 이용해서 필요한 부분 로깅하고 파일로 출력
3. 테스트 코드
   - 90% 이상 아직 작성 못함



