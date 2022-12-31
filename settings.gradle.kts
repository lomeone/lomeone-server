val serviceName: String by settings
rootProject.name = serviceName

pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagement: String by settings
    val kotlinVersion: String by settings
    val coverallsVersion: String by settings
    val jibVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagement
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        id("com.github.kt3k.coveralls") version coverallsVersion
        id("com.google.cloud.tools.jib") version jibVersion
    }
}

include("infrastructure")
include("domain")
include("service")
include("application")
