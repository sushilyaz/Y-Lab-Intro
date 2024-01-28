plugins {
    id("java")
    application
}

application {
    mainClass="org.example.Main"
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
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.24.2")

}

tasks.test {
    useJUnitPlatform()
}