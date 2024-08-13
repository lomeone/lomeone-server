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
val springCloudEurekaClientVersion: String by project
val jacocoVersion: String by project

val jacocoBranchCoverageRatio: String by project
val jacocoLineCoverageRatio: String by project

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
	kotlin("kapt")
	kotlin("plugin.jpa")
	jacoco
	id("com.github.kt3k.coveralls")
	id("com.google.cloud.tools.jib")
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
		plugin("jacoco")
		plugin("com.google.cloud.tools.jib")
	}

	jacoco {
		toolVersion = jacocoVersion
	}

	repositories {
		mavenCentral()
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

subprojects {
	java.sourceCompatibility = JavaVersion.VERSION_21
	java.targetCompatibility = JavaVersion.VERSION_21

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

	tasks.withType<Test> {
		useJUnitPlatform()
		finalizedBy(tasks.withType<JacocoReport>())
	}

	tasks.withType<JacocoReport> {
		dependsOn(tasks.withType<Test>())

		reports {
			html.required.set(true)
			xml.required.set(true)
			csv.required.set(false)
		}

		afterEvaluate {
			classDirectories.setFrom(files(classDirectories.files.map {
				fileTree(it).apply {
					exclude("**/LomeoneApplication**")
					for (pattern in 'A' .. 'Z') {
						exclude("**/Q${pattern}**")
					}
				}
			}))
		}

		finalizedBy(tasks.withType<JacocoCoverageVerification>())
	}

	tasks.withType<JacocoCoverageVerification> {
		dependsOn(tasks.withType<JacocoReport>())
		val excludeJacocoClassNamePatterns = mutableListOf<String>().apply {
			for (pattern in 'A'..'Z') {
				add("*.Q${pattern}*")
			}
		}

		violationRules {
			rule {
				element = "CLASS"

				limit {
					counter = "BRANCH"
					value = "COVEREDRATIO"
					minimum = jacocoBranchCoverageRatio.toBigDecimal()
				}

				limit {
					counter = "LINE"
					value = "COVEREDRATIO"
					minimum = jacocoLineCoverageRatio.toBigDecimal()
				}

				excludes = listOf<String>("**.LomeoneApplication**", "**.AuditEntity**") + excludeJacocoClassNamePatterns
			}
		}
	}

	tasks.withType<KotlinCompile> {
		compilerOptions {
			freeCompilerArgs.add("-Xjsr305=strict")
			jvmTarget = JvmTarget.JVM_21
		}
	}
}

fun getGitHash(): String {
	val stdout = ByteArrayOutputStream()
	exec {
		commandLine = listOf("git", "rev-parse", "--short", "HEAD")
		standardOutput = stdout
	}
	return stdout.toString().trim()
}

tasks.register<JacocoReport>("jacocoAllReport") {
	dependsOn(":application:jacocoTestReport", ":domain:jacocoTestReport", ":util:jacocoTestReport")

	val currentTask = this

	val allSourceSrcDirs = mutableListOf<Set<File>>()
	val outputDirectories = mutableListOf<SourceSetOutput>()

	subprojects {
		val subproject = this
		subproject.plugins.withType<JacocoPlugin>().configureEach {
			subproject.tasks.matching { it.extensions.findByType<JacocoTaskExtension>() != null }.forEach {
				currentTask.dependsOn(it)
			}
		}

		allSourceSrcDirs.add(subproject.sourceSets.main.get().allSource.srcDirs)
		outputDirectories.add(subproject.sourceSets.main.get().output)

		afterEvaluate {
			classDirectories.setFrom(files(classDirectories.files.map {
				fileTree(it).apply {
					exclude("**/LomeoneApplication**")
					exclude("**/AuditEntity**")
					for (pattern in 'A' .. 'Z') {
						exclude("**/Q${pattern}**")
					}
				}
			}))
		}
	}

	additionalSourceDirs.setFrom(allSourceSrcDirs)
	sourceDirectories.setFrom(allSourceSrcDirs)
	classDirectories.setFrom(outputDirectories)
	executionData.setFrom(fileTree(projectDir).include("**/jacoco/*.exec"))

	reports {
		xml.required.set(true)
		html.required.set(true)
		csv.required.set(false)
	}
}

coveralls {
	jacocoReportPath = "${projectDir}/build/reports/jacoco/jacocoAllReport/jacocoAllReport.xml"
	sourceDirs = subprojects.map { it.sourceSets.main.get().allSource.srcDirs.toList() }
		.toList().flatten().map { relativePath(it) }
}

tasks.withType<org.kt3k.gradle.plugin.coveralls.CoverallsTask> {
	dependsOn(tasks["jacocoAllReport"])
}
