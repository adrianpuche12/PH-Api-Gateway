FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /app

# Copia todo el proyecto
COPY . .

# Ejecuta Maven con codificación explícita para evitar MalformedInputException
RUN ./mvnw package -DskipTests -Dfile.encoding=UTF-8

# Fase de ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia el JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Puerto en el que se ejecutará la aplicación
EXPOSE 8085

# Configura la codificación y ejecuta la aplicación
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app/app.jar"]