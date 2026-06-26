FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY src ./src
RUN mkdir -p out && javac -d out $(find src -name '*.java')
ENTRYPOINT ["java", "-cp", "/app/out", "controller.Program"]
