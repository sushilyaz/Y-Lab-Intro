plugins {
    id("java")
    id("war")
//    kotlin("jvm") version "1.5.31"
//    id("io.freefair.aspectj.post-compile-weaving") version "8.4"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}
//
//buildscript {
//    repositories {
//        maven {
//            url = uri("https://plugins.gradle.org/m2/")
//        }
//    }
//    dependencies {
//        classpath("io.freefair.gradle:aspectj-plugin:8.4")
//    }
//}
//
//apply(plugin = "io.freefair.aspectj.post-compile-weaving")

// Ментор сказал добавить в отдельный блок версии зависимостей. Я правильно понял ? :
dependencies {
    val slf4jVersion = "1.7.32"
    val logbackVersion = "1.2.6"
    val liquibaseVersion = "4.25.1"

    val mapstructVersion = "1.5.5.Final"
    val lombokVersion = "1.18.30"
    val mapstructLombokVersion = "0.2.0"

    val jacksonVersion = "2.16.1"
    val postgresqlVersion = "42.6.0"
    val testcontainersVersion = "1.19.3"
    val junitVersion = "5.9.1"
    val assertjVersion = "3.24.2"
    val jakartaValidationVersion = "3.0.2"
    val hibernateValidatorVersion = "8.0.1.Final"
    val mockitoVersion = "5.10.0"
    val glassfishVersion = "5.0.0"

    val aspectjVersion = "1.9.21"
    val springVersion = "6.1.3"

    val servletApiVersion = "6.0.0"

    val swaggerVersion = "2.3.0"

    implementation("org.hibernate.validator:hibernate-validator:$hibernateValidatorVersion")

    implementation("jakarta.validation:jakarta.validation-api:$jakartaValidationVersion")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")
//    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.liquibase:liquibase-core:$liquibaseVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    implementation("org.glassfish.expressly:expressly:$glassfishVersion")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$mapstructLombokVersion")

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    compileOnly("jakarta.servlet:jakarta.servlet-api:$servletApiVersion")
    implementation("jakarta.servlet:jakarta.servlet-api:$servletApiVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    // Да, springdoc подтягивает еще и либы спринг бута, НО: Я его нигде абсолютно не использую, в имортах можете сами убедиться
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")
    implementation("org.springframework:spring-core:$springVersion")
    implementation("org.springframework:spring-context:$springVersion")
    implementation("org.springframework:spring-web:$springVersion")
    implementation("org.springframework:spring-webmvc:$springVersion")
    implementation("org.springframework:spring-beans:$springVersion")
    implementation("org.springframework:spring-aop:$springVersion")
    runtimeOnly("org.aspectj:aspectjweaver:$aspectjVersion")
    implementation("org.aspectj:aspectjweaver:$aspectjVersion")

    testImplementation("org.springframework:spring-test:${springVersion}")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertjVersion")

}

tasks.test {
    useJUnitPlatform()
}
