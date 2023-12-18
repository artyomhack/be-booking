plugins {
    id("java-library")
    id("maven-publish")
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "net.bebooking"
version = "0.0.1"

repositories {
    mavenLocal()
    mavenCentral()
}

allprojects {

    plugins.apply("java-library")

    plugins.apply("org.springframework.boot")
    plugins.apply("io.spring.dependency-management")

    dependencies {
        api("org.ecom24.common:ecom-common-types:1.0.0")

        implementation("jakarta.validation:jakarta.validation-api")
        implementation("org.springframework.data:spring-data-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
        implementation("org.mongodb:mongo-java-driver:3.12.14")
        implementation("org.mongodb:mongodb-driver-sync")

        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.testcontainers:testcontainers:1.18.3")
        testImplementation("org.testcontainers:mongodb:1.18.3")
        testImplementation("org.testcontainers:junit-jupiter")

        testCompileOnly("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    }
}

tasks.test {
    useJUnitPlatform()
}