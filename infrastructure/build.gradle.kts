plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":domain"))

    // Observability
    implementation(libs.aws.smithy.kotlin.telemetry.provider.otel)

    // JDBC Driver
    runtimeOnly(libs.mysql)


    // test
    testRuntimeOnly(libs.h2)
}
