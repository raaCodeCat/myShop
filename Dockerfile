FROM amazoncorretto:21
COPY target/*.jar appMyShop.jar
ENTRYPOINT ["java","-jar","/appMyShop.jar"]
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://myshop-db:5432/myShop
ENV POSTGRES_USER=shopUser
ENV POSTGRES_PASSWORD=f#57Hx9jz