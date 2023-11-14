<img src="https://github.com/ks12b0000/VocaProject/assets/102012155/3eb94c47-ee4a-442c-b206-dd8e9dc8044d" width="500" height="500"/>

# 영어 단어 학습 및 영어 단어 테스트 앱 서비스
영어 학원에서 사용 할 학생들의 영어 단어 학습 및 영어 단어 테스트 기능으로 선생님이 학생별로 성적 관리를 해주는 서비스입니다.

<br/>

## 프로젝트 기간 
2023.10.10 ~ 진행 중

<br/>

## Table of Contents
- [개요](#개요)
- [Skils](#skils)
- [ERD](#erd)
- [API Reference](#api-reference)
- [구현과정(설계 및 의도)](<#구현과정(설계-및-의도)>)
- [TIL 및 회고](#til-및-회고)
- [Authors](#authors)
- [References](#references)

<br/>

## 개요
본 서비스는 학원에서 학생들의 성적 관리 및 학습 능력 관리를 더 편리하게 할 수 있는 애플리케이션이 있었으면 좋겠다는 생각이 들어 시작하게 되었습니다. 클래스에 맞는 영어 단어 학습 기능, 영어 단어 테스트 기능으로 인해 학생들의 영어 단어 학습에 도움을 줄 수 있고, 클래스별 학생 관리 기능으로 인해 선생님들이 학생들의 학습 관리 및 성적 관리를 편리하게 할 수 있도록 만든 서비스입니다.

<br/>

## Skils
가상 환경: ![Static Badge](https://img.shields.io/badge/Docker-blue) 
<br/>
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/Java-red) 
![Static Badge](https://img.shields.io/badge/SpringBoot-grean)
![Static Badge](https://img.shields.io/badge/SpringDataJPA-grean)
![Static Badge](https://img.shields.io/badge/SpringSecurity-grean)
<br/>
데이터 베이스: ![Static Badge](https://img.shields.io/badge/mysql-blue)

<br/>

## ERD
<img width="632" alt="스크린샷 2023-11-14 오후 10 23 11" src="https://github.com/ks12b0000/VocaProject/assets/102012155/a6d8544b-7da7-4eda-a157-9a25935c4238">

<br/>

## API Reference
<img src="https://img.shields.io/badge/Swagger-6DB33F?style=for-the-badge&logo=SWAGGER&logoColor=white">
<img width="697" alt="스크린샷 2023-11-14 오후 10 26 25" src="https://github.com/ks12b0000/VocaProject/assets/102012155/da20074d-9d28-4e6b-8af8-dbcd88825f8a">

<br/>

## 구현과정(설계 및 의도)

<details>
<summary>Admin - click</summary>
  
  - **유저 목록 조회**

    1. 고려사항 : Admin 중 MasterAdmin은 전체 유저를 조회 가능 및 입력한 키워드에 맞는 클래스의 유저들을 조회할 수 있어야 하고, MiddleAdmin은 자신이 관리하는 클래스에 유저들만 조회할 수 있어야 한다.
    2. 구현과정 : Parameter로 넘어온 adminId로 관리자를 조회해서 만약 masterAdmin이면 Parameter로 넘어온 class의 값으로 전체 유저를 조회하거나, 값에 맞는 클래스의 유저들을 조회한다. 만약 masterAdmin이 아니면 자신이 관리하는 클래스의 유저들만 조회할 수 있도록 하고 UserListResponse(username, loginId, role, approval, className)에 맞게 매핑하여 리스트로 반환하도록 설계하였습니다.
   
 - **기능 사용 승인이 되지 않은 유저 조회**
    1. 고려사항 : 회원가입은 되었지만 아직 승인이 되지 않아 기능을 사용할 수 없는 유저만 조회해야한다.
    2. 구현과정 : DB에서 유저 정보에 Approval(승인)이 되지 않은 유저만 필터링 해서 조회해서 UserListResponse(username, loginId, role, approval, className)에 맞게 매핑하여 리스트로 반환하도록 설계하였습니다.

 - **유저 승인 여부 변경**
    1. 고려사항 : 유저의 승인을 허용하거나, 허용하지 않도록 변경해야한다.
    2. 구현과정 : RequestBody로 userLoginId, approval, role을 받아 approval = N이면 userLoginId에 맞는 유저의 승인을 허용하지 않도록 변경하고, approval = Y이면 승인을 허용하게 변경하도록 설계하였습니다.

 - **유저 정보 변경**
    1. 고려사항 : Admin 중 MasterAdmin은 전체 유저의 role, className만 변경 가능할 수 있어야 하고, MiddleAdmin은 자신이 관리하는 클래스에 유저의 className만 변경 가능할 수 있어야 한다.
    2. 구현과정 : Parameter로 넘어온 adminId로 관리자를 조회해서 만약 masterAdmin이면 RequestBody로 받은 role, className으로 유저의 정보를 변경하고, 만약 masterAdmin이 아니면 className으로 자신이 관리하는 클래스의 유저만의 정보를 변경하도록 설계했습니다.
  
 - **유저 비밀번호 변경**
    1. 고려사항 : Admin 중 MasterAdmin은 전체 유저의 password만 변경 가능할 수 있어야 하고, MiddleAdmin은 자신이 관리하는 클래스에 유저의 password만 변경 가능할 수 있어야 한다.
    2. 구현과정 : Parameter로 넘어온 adminId로 관리자를 조회해서 만약 masterAdmin이면 RequestBody로 받은 password를 받아 암호화하여 유저의 password를 변경하고, 만약 masterAdmin이 아니면 암호화한 password로 자신이 관리하는 클래스의 유저만의 password를 변경하도록 설계했습니다.
      
 - **유저 삭제**
    1. 고려사항 : MasterAdmin만이 유저를 삭제할 수 있고 MiddleAdmin은 API에 접근할 수 없어야 한다.
    2. 구현과정 : SpringSecurity를 이용해 MasterAdmin만이 API에 접근할 수 있도록 설정하였고, Parameter로 넘어온 userLoginId로 유저를 삭제하도록 설계했습니다.
  
 - **단어장 삭제**
    1. 고려사항 : MasterAdmin만이 단어장을 삭제할 수 있고 MiddleAdmin은 API에 접근할 수 없어야 한다.
    2. 구현과정 : SpringSecurity를 이용해 MasterAdmin만이 API에 접근할 수 있도록 설정하였고, Parameter로 넘어온 categoryId로 단어장을 삭제하도록 설계했습니다.
</details>

<details>
<summary>CSV - click</summary>
  
  - **CSV DB에 저장**
     1. 고려사항 : csv파일에 단어, 뜻, 카테고리, day로 저장된 단어장을 단어장 DB 컬럼에 맞게 매핑해야 한다.
     2. 구현과정 : MultipartFile로 csv파일을 받고, 파일을 읽어 각 행을 ,로 나눠 단어장 컬럼에 맞게 매핑해서 저장하고, 만약 ,로 나눴을 때 "안녕, 안녕1" 처럼 단어의 뜻이 여러개라 ,으로 나누면 매핑이 제대로 되지 않을 수 있어 행을 나눌 때 ""안에 있는 ,은 제외하도록 설계하였습니다.
</details>

<details>
<summary>Users - click</summary>
  
  - **유저 회원가입**
     1. 고려사항 : 유저는 이름, 로그인 아이디, 비밀번호로 회원가입을 할 수 있고, 만약 셋 중 하나라도 값이 들어오지 않거나, 중복된 아이디라면 예외처리를 해야한다. 회원가입이 되면 유저의 승인여부는 N, 권한은 ROLE_NULL로 자동 설정되어 있어야 한다.
     2. 구현과정 : RequestBody에 username, loginId, password를 입력 받아 만약 빈 값이 들어오면 예외처리를 해주기위해 Validated 어노테이션을 사용했고, loginId가 중복됐는지 확인하기 위해 loginId로 DB에 유저를 조회해서 있으면 예외처리를 해주고 없으면 password를 암호화해서 회원가입을 진행하도록 설계하였습니다.
   
  - **유저 로그인**
     1. 고려사항 : 로그인 아이디, 비밀번호로 로그인을 할 수 있고, 만약 빈 값이 들어오거나, 승인되지 않은 유저가 로그인을 요청했을 경우 예외처리를 해야한다. 로그인이 완료되면 JWT Token도 발급되어야 한다.
     2. 구현과정 : RequestBody에 loginId, password를 입력 받고, loginId로 DB에 유저를 조회해서 없거나, 있는데 승인여부가 N이라면 바로 예외처리를 해주고 있으면 유저의 password와 body로 받은 password가 일치한지 확인하고 JWT Token을 생성하도록 설계하였습니다.
     
</details>
