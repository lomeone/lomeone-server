plugins {
    alias(libs.plugins.kotlin.jpa)
    // preview 끝나면 사용해보기
//    alias(libs.plugins.aws.sdk.kotlin.dynamodb.mapper)
}

dependencies {
    implementation(project(":domain"))

    // jpa
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.bundles.querydsl)

    // Openfeign
    implementation(libs.spring.cloud.starter.openfeign)
    // Openfeign에서 spring web annotations를 그대로 사용하기 때문에 web engine을 exclude하고 import
    implementation(libs.spring.boot.starter.web) {
        exclude(module = "spring-boot-starter-tomcat")
    }

    // preview 끝나면 사용해보기
//    implementation(libs.bundles.aws.sdk.kotlin.dynamodb)

    // JDBC Driver
    runtimeOnly(libs.mysql)
    runtimeOnly(libs.h2)

    // test
//    testRuntimeOnly(libs.h2)
}
