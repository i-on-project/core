import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC6-4"
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
}

detekt {
    version = "1.0.0.RC6-4"
    defaultProfile {
        input = "$projectDir/src/main/kotlin"
        config = "$projectDir/default-detekt-config.yml" // Code style rules file.
        filters = ".*/res/.*,.*build/.*"
    }
}

group = "org.ionproject"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jdbi:jdbi3-sqlobject:3.12.2")
    implementation("org.postgresql:postgresql:42.+")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

// -- BD automation

tasks.register<PgStart>("pgStart")
tasks.register<PgStop>("pgStop")
tasks.register<PgToggle>("pgToggle")
tasks.register<PgDropDb>("pgDropDb")
tasks.register<PgCreateDb>("pgCreateDb")
tasks.register<PgInitSchema>("pgInitSchema")
tasks.register<PgAddData>("pgAddData")
