# NBE3-4-2-Team05
데브코스 3기 4회차 2차프로젝트 오🌏구 팀

---
## 서비스 시작하기
### 1. 다운로드

해당 Github 에서 무료로 다운로드 가능합니다.
[최신 릴리즈 다운로드](https://github.com/prgrms-be-devcourse/NBE3-4-2-team05/archive/refs/heads/main.zip)

### 2. 환경변수 셋팅

이 프로젝트를 실행하기 위해서는, `.env` 파일이 루트 경로에 필요합니다!
`/backend/.env`
그리고, 필수 환경 변수를 지정해줘야 합니다!
아래에는 필수 환경변수와 예시입니다.

```env
Z9_DB_URL = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul"
Z9_DB_USERNAME = "root"
Z9_DB_PASSWORD = "1234"
```

### 3. 프로젝트 사전 구성
- 기본적으로, MySQL 8.0v 이상이 설치되어 있어야 합니다.
- 또한, 설치된 MySQL 과 `.env` 파일 내용이 일치되어야 합니다.

### 4. 프로젝트 실행 방법
- Intellij 로 실행 시 최상단 프로젝트 실행 task에 옵션을 추가해줘야 합니다.
  1. task 화살표 클릭 후, `Edit Configurations...` 클릭
  2. 다음 창에서, 중간부분, `Modify options` 클릭
  3. 나오는 창들 중, `Enviorment variables` 클릭
  4. 중간 부분, `Enviroment variables` 에, 파일 아이콘 클릭해서, 생성한 .evn 파일 추가
  5. ![image](https://github.com/user-attachments/assets/e1b3497a-fc13-473e-92fa-c82ff8ec9cc6)
  6. 확인 버튼 후, 프로젝트 실행!


