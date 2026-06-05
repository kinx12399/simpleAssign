# 1. 자바 17 경량화 실행 환경 이미지 사용
FROM eclipse-temurin:17-jre-alpine
# 2. 컨테이너 내부 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 jar 파일을 컨테이너 내부로 복사
# (만약 실제 생성된 jar 파일명이 다르면 아래 이름을 수정해 주세요)
COPY build/libs/*-SNAPSHOT.jar app.jar

# 4. 고가용성 공유 볼륨이 바인딩될 폴더 미리 생성
RUN mkdir -p /app/logs

# 5. 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]