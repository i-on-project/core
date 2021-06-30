import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
}

group = "org.ionproject"
version = "0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("com.github.ben-manes.caffeine:caffeine:2.8.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.networknt:json-schema-validator:1.0.38")
    implementation("org.json:json:20171018")
    implementation("org.postgresql:postgresql:42.+")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.12.0.202106070339-r")

    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.20.0")

    implementation("org.flywaydb:flyway-core:6.5.7")

    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-RC")

    // test dependencies
    testImplementation("org.mnode.ical4j:ical4j:3.0.+")
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
        jvmTarget = "11"
    }
}

// DB automation
tasks.register<PgStart>("pgStart") // doesn't do a thing if the container is already running
tasks.register<PgStop>("pgStop") // doesn't do a thing if the container doesn't exist
tasks.register<PgToggle>("pgToggle") // destroys the container if it exists, otherwise creates it
tasks.register<PgInsertReadToken>("pgInsertReadToken")
tasks.register<PgInsertIssueToken>("pgInsertIssueToken")
tasks.register<PgInsertRevokeToken>("pgInsertRevokeToken")

/**
 * Will destroy the container before setting the database (which will be done inside
 * a new container).
 */
tasks.register("pgReset") {
    val setupTask = "pgStart"
    val stopTask = "pgStop"

    dependsOn(setupTask, stopTask)
    tasks[setupTask].mustRunAfter(stopTask)
}

tasks.register<Copy>("extractUberJar") {
    dependsOn("build")
    dependsOn("test")
    dependsOn("ktlintCheck")
    from(zipTree("$buildDir/libs/${rootProject.name}-$version.jar"))
    into("$buildDir/dependency")
}
