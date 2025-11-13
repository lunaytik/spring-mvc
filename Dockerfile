FROM gradle:9-jdk25

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle build -x test --no-daemon || true

VOLUME /app/src
COPY src ./src

EXPOSE 8080

CMD ["gradle", "bootRun"]
