plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.example:logg:0.0.1-SNAPSHOT")
    implementation("org.example:audit:0.0.1-SNAPSHOT")
    val liquibaseVersion = "4.25.1"
    val mapstructVersion = "1.5.5.Final"
    val lombokVersion = "1.18.30"
    val mapstructLombokVersion = "0.2.0"
    val postgresqlVersion = "42.6.0"
    val swaggerVersion = "2.3.0"
    val testcontainersVersion = "1.19.3"
    val junitVersion = "5.9.1"
    val assertjVersion = "3.24.2"
    val mockitoVersion = "5.10.0"
    val mockitoJUnitVersion = "5.9.0"
    implementation("org.liquibase:liquibase-core:$liquibaseVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$mapstructLombokVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")
    implementation("org.springframework.boot:spring-boot-starter-aop")


    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoJUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertjVersion")


    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
