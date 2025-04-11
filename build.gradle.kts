import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

val groupName: String by project

val queryDslVersion: String by project
val jwtVersion: String by project

val kotestVersion: String by project
val kotestSpringVersion: String by project
val springMockkVersion: String by project

val springCloudOpenFeignVersion: String by project
val springCloudHystrixVersion: String by project
val springCloudRibbonVersion: String by project

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
	kotlin("kapt")
	kotlin("plugin.jpa")
	id("com.google.cloud.tools.jib")
	id("org.jetbrains.kotlinx.kover")
	id("com.github.kt3k.coveralls")
	id("org.sonarqube")
}

allprojects {
	group = groupName
	version = getGitHash()

	apply {
		plugin("kotlin")
		plugin("kotlin-spring")
		plugin("kotlin-jpa")
		plugin("kotlin-kapt")
		plugin("org.springframework.boot")
		plugin("io.spring.dependency-management")
		plugin("com.google.cloud.tools.jib")
		plugin("org.jetbrains.kotlinx.kover")
		plugin("org.sonarqube")
	}

	repositories {
		mavenCentral()
	}

	tasks.test {
		useJUnitPlatform()
		finalizedBy(tasks.koverVerify, tasks.koverHtmlReport, tasks.koverXmlReport)
	}

	kover {
		reports {
			filters {
				excludes {
					classes("*.LomeoneApplication*")
					for (pattern in 'A' .. 'Z') {
						classes("*.Q${pattern}*")
					}
					packages("com.lomeone.generated.*")
				}
			}
			total {
				verify {
					rule {
						minBound(0)
					}
				}
			}
		}
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

subprojects {
	java {
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21

		toolchain {
			languageVersion = JavaLanguageVersion.of(21)
		}
	}

	kotlin {
		jvmToolchain(21)
	}


	dependencies {
		// Kotlin
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

		// Spring
		implementation("org.springframework.boot:spring-boot-starter-validation")
		implementation("org.springframework.boot:spring-boot-starter-security")
		implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

		// Jwt
		implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
		runtimeOnly("io.jsonwebtoken:jjwt-impl:$jwtVersion")
		runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jwtVersion")

		// JPA
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta")
		kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta")
		kapt("jakarta.persistence:jakarta.persistence-api")
		kapt("jakarta.annotation:jakarta.annotation-api")

		// Spring Cloud
		implementation("org.springframework.cloud:spring-cloud-starter-openfeign:$springCloudOpenFeignVersion")
		implementation("org.springframework.cloud:spring-cloud-starter-netflix-hystrix:$springCloudHystrixVersion")
		implementation("org.springframework.cloud:spring-cloud-starter-netflix-ribbon:$springCloudRibbonVersion")

		developmentOnly("org.springframework.boot:spring-boot-devtools")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("com.ninja-squad:springmockk:$springMockkVersion")

		// kotest
		testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
		testImplementation("io.kotest:kotest-property:$kotestVersion")
		testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpringVersion")
	}

	tasks.withType<KotlinCompile> {
		compilerOptions {
			freeCompilerArgs.add("-Xjsr305=strict")
			jvmTarget = JvmTarget.JVM_21
		}
	}
}

dependencies {
	kover(project(":application"))
	kover(project(":domain"))
	kover(project(":infrastructure"))
}

fun getGitHash(): String {
	val stdout = ByteArrayOutputStream()
	exec {
		commandLine = listOf("git", "rev-parse", "--short", "HEAD")
		standardOutput = stdout
	}
	return stdout.toString().trim()
}

coveralls {
	jacocoReportPath = "${projectDir}/build/reports/kover/report.xml"
	sourceDirs = subprojects.map { it.sourceSets.main.get().allSource.srcDirs.toList() }
		.toList().flatten().map { relativePath(it) }
}

sonar {
	properties {
		property("sonar.projectKey", "lomeone_lomeone-server")
		property("sonar.organization", "lomeone")
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.jacoco.reportPaths", "${projectDir}/build/reports/kover/report.xml")

	}
}
