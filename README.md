<img src="https://github.com/ks12b0000/VocaProject/assets/102012155/3eb94c47-ee4a-442c-b206-dd8e9dc8044d" width="500" height="500"/>

# 영어 단어 학습 및 영어 단어 테스트 앱 서비스
단어 학습 및 단어 테스트 기능을 제공하여 학생들의 단어 공부에 도움을 주는 서비스입니다.

<br/>

## 프로젝트 기간 
2023.10.10 ~ 2024.01.15

<br/>

## Table of Contents
- [개요](#개요)
- [Skils](#skils)
- [ERD](#erd)
- [API Reference](#api-reference)
- [이슈 및 개선사항](#이슈-및-개선사항)
- [Test](#test)

<br/>

## 개요
본 서비스는 현재 운영중인 학원에서 학생들의 성적 관리 및 학습 능력 관리를 더 편리하게 할 수 있는 애플리케이션이 있었으면 좋겠다는 선생님의 의견을 들어 시작하게 되었습니다.
학생들이 간편하게 영어 단어 학습을 할 수 있도록 도움을 줄 수 있는 학생들의 클래스에 맞는 영어 단어 학습 기능과 영어 단어 테스트 기능을 만들었고
선생님들이 학생들의 학습 관리 및 성적 관리를 편리하게 할 수 있도록 클래스별 학생 관리 기능을 만들어 학원에 도움을 줄 수 있도록 개발한 서비스입니다.

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
![Static Badge](https://img.shields.io/badge/redis-red)

<br/>

## ERD
<img width="632" alt="스크린샷 2024-01-15 오후 3 05 07" src="https://github.com/ks12b0000/VocaProject/assets/102012155/0a9e84ee-88db-4ea3-9ceb-21e003c6f8e1">

<br/>

## API Reference
<img src="https://img.shields.io/badge/Swagger-6DB33F?style=for-the-badge&logo=SWAGGER&logoColor=white">
<img width="800" alt="스크린샷 2024-01-15 오후 3 09 06" src="https://github.com/ks12b0000/VocaProject/assets/102012155/5f41dc99-055c-4f9e-bffa-611ee146dcdf">

<br/>

## 이슈 및 개선사항

## 1. 단어 테스트 결과 목록 조회 No Offset, Covering Index, Non Clustered Index를 사용한 페이징 성능 9.85초 -> 0.014초로 개선
[자세한 페이징 성능 개선 과정 Click!](https://purple-knot-a8d.notion.site/No-Offset-Covering-Index-Non-Clustered-Index-7f2b909ed2bb417eb0d4cc8b778dbdcb?pvs=4)
#### Covering Index, Non Clustered Index 적용 전 9.85초
<img width="632" alt="스크린샷 2024-01-15 오후 3 05 07" src="https://github.com/ks12b0000/VocaProject/assets/102012155/58dff7c6-ae1a-4063-9cde-17a7872d251b">
<br/>

#### Covering Index, Non Clustered Index 적용 후 0.014초
<img width="632" alt="스크린샷 2024-01-15 오후 3 05 07" src="https://github.com/ks12b0000/VocaProject/assets/102012155/6f1c1416-28bb-450c-9cce-298e9ad06bc7">

<br/>

<br/>

## Test
- ServiceTest, RepositoryTest, ControllerTest 단위로 총 76개의 테스트 코드 작성
<img width="295" alt="스크린샷 2024-01-15 오후 4 12 04" src="https://github.com/ks12b0000/VocaProject/assets/102012155/7cda4c41-a16c-4713-8951-d364b4f8f7e8">
