plugins {
    id("java")
    id("io.papermc.paperweight.userdev")
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly(project(":version"))
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    disableAutoTargetJvm()
}

tasks {
    compileJava {
        options.release.set(17)
    }
}

tasks.test {
    useJUnitPlatform()
}