plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.ksp)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
//    implementation(project(":util"))

    // jpa
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.bundles.querydsl)
    ksp(libs.querydsl.ksp.codegen)
}
