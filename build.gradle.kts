plugins {
    id("java")
    id("war")
    kotlin("jvm") version "1.5.31"
    id("io.freefair.aspectj.post-compile-weaving") version "8.4"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("io.freefair.gradle:aspectj-plugin:8.4")
    }
}

apply(plugin = "io.freefair.aspectj.post-compile-weaving")

// Ментор сказал добавить в отдельный блок версии зависимостей. Я правильно понял ? :
dependencies {
    val aspectjVersion = "1.9.21"
    val slf4jVersion = "1.7.32"
    val logbackVersion = "1.2.6"
    val liquibaseVersion = "4.25.1"
    val mapstructVersion = "1.5.5.Final"
    val jacksonVersion = "2.16.1"
    val servletApiVersion = "5.0.0"
    val postgresqlVersion = "42.6.0"
    val lombokVersion = "1.18.30"
    val testcontainersVersion = "1.19.3"
    val junitVersion = "5.9.1"
    val assertjVersion = "3.24.2"
    val jakartaValidation = "3.0.2"

    runtimeOnly("org.aspectj:aspectjweaver:$aspectjVersion")
    implementation("org.aspectj:aspectjweaver:$aspectjVersion")
    implementation("jakarta.validation:jakarta.validation-api:$jakartaValidation")
    runtimeOnly("org.aspectj:aspectjrt:$aspectjVersion")
    implementation("org.aspectj:aspectjrt:$aspectjVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.liquibase:liquibase-core:$liquibaseVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.4.2.Final")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    compileOnly("jakarta.servlet:jakarta.servlet-api:$servletApiVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
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
