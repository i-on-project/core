import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.Thread.sleep

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
version = "0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    // main dependencies
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jdbi:jdbi3-sqlobject:3.12.2")
    implementation("org.postgresql:postgresql:42.+")

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
        jvmTarget = "1.8"
    }
}

// DB automation
tasks.register<PgStart>("pgStart") // doesn't do a thing if the container is already running
tasks.register<PgStop>("pgStop") // doesn't do a thing if the container doesn't exist
tasks.register<PgToggle>("pgToggle") // destroys the container if it exists, otherwise creates it
tasks.register<PgDropDb>("pgDropDb")
tasks.register<PgCreateDb>("pgCreateDb")
tasks.register<PgCreateUser>("pgCreateUser")
tasks.register<PgInitSchema>("pgInitSchema")
tasks.register<PgAddData>("pgAddData")

/**
 * Will execute all the tasks needed to setup the database running in a container,
 * in the proper order.
 */
tasks.register("pgSetupDb") {
    val startTask = "pgStart"
    val createDbTask = "pgCreateDb"
    val createUserTask = "pgCreateUser"
    val initSchemaTask = "pgInitSchema"
    val addDataTask = "pgAddData"

    dependsOn(startTask, createDbTask, createUserTask, initSchemaTask, addDataTask)
    tasks[createUserTask].mustRunAfter(startTask)
    tasks[createDbTask].mustRunAfter(createUserTask)
    tasks[initSchemaTask].mustRunAfter(createDbTask)
    tasks[addDataTask].mustRunAfter(initSchemaTask)
}

/**
 * Will destroy the container before setting the database (which will be done inside
 * a new container).
 */
tasks.register("pgReset") {
    val setupTask = "pgSetupDb"
    val stopTask = "pgStop"

    dependsOn(setupTask, stopTask)
    tasks[setupTask].mustRunAfter(stopTask)
}
