group = "com.lomeone"
version = "7cd52c5bea"

plugins {
    alias(libs.plugins.dgs.codegen)
    alias(libs.plugins.jib)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure"))

    // Web
    implementation(libs.spring.boot.starter.web)

    // Swagger
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // Observability
    implementation(libs.spring.boot.starter.actuator)

    // Eunoia
    implementation(libs.eunoia.spring.web.rest)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
