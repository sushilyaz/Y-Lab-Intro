plugins {
    id("java")
    id("war")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}
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
    val mockitoVersion = "5.10.0"
    val aspectjVersion = "1.9.21"
    val springVersion = "6.1.3"
    val servletApiVersion = "6.0.0"
    val swaggerVersion = "2.3.0"
    val mockitoJUnitVersion = "5.9.0"


    implementation("jakarta.validation:jakarta.validation-api:$jakartaValidationVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.liquibase:liquibase-core:$liquibaseVersion")
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
    // Да, springdoc подтягивает еще и либы спринг бута, НО: Я его нигде абсолютно не использую, в импортах можете сами убедиться
    // Настраивал его в конфиге сам, ручками
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")
    implementation("org.springframework:spring-core:$springVersion")
    implementation("org.springframework:spring-context:$springVersion")
    implementation("org.springframework:spring-web:$springVersion")
    implementation("org.springframework:spring-webmvc:$springVersion")
    implementation("org.springframework:spring-beans:$springVersion")
    implementation("org.springframework:spring-aop:$springVersion")
    runtimeOnly("org.aspectj:aspectjweaver:$aspectjVersion")
    implementation("org.aspectj:aspectjweaver:$aspectjVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.springframework:spring-test:${springVersion}")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoJUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertjVersion")

}

tasks.test {
    useJUnitPlatform()
}
