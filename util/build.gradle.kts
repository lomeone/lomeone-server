plugins {
    alias(libs.plugins.kotlin.jpa)
}

dependencies {

    // jpa
    implementation(libs.spring.boot.starter.data.jpa)
}
