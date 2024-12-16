val serviceName: String by settings
rootProject.name = serviceName

pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagement: String by settings
    val kotlinVersion: String by settings
    val jibVersion: String by settings
    val dgsCodegenVersion: String by settings
    val coverallsVersion: String by settings
    val sonarqubeVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagement
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("kapt") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        id("com.google.cloud.tools.jib") version jibVersion
        id("com.netflix.dgs.codegen") version dgsCodegenVersion
        id("com.github.kt3k.coveralls") version coverallsVersion
        id("org.sonarqube") version sonarqubeVersion
    }
}

include("infrastructure")
include("domain")
include("application")
include("util")
