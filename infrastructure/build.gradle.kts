plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.ksp)
    // preview 끝나면 사용해보기
//    alias(libs.plugins.aws.sdk.kotlin.dynamodb.mapper)
}

dependencies {
    implementation(project(":domain"))

    // aws
    implementation(libs.bundles.aws.sdk.dynamodb)
    implementation(libs.aws.sdk.sts)

    // preview 끝나면 사용해보기
//    implementation(libs.bundles.aws.sdk.kotlin.dynamodb)

    // Observability
    implementation(libs.aws.smithy.kotlin.telemetry.provider.otel)
    implementation(libs.opentelemetry.aws.sdk)

    // JDBC Driver
    runtimeOnly(libs.mysql)

    // test
    testRuntimeOnly(libs.h2)
}
