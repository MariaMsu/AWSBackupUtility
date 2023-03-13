plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("aws.sdk.kotlin:s3:0.19.0-beta")
//    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")

    implementation("org.slf4j:slf4j-api:1.7.5")
    implementation("org.slf4j:slf4j-simple:1.6.4")

    // https://mvnrepository.com/artifact/com.xenomachina/kotlin-argparser
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
