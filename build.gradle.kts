val groupName: String by project

val queryDslVersion: String by project
val jwtVersion: String by project

val kotestVersion: String by project
val kotestSpringVersion: String by project
val springMockkVersion: String by project

val springCloudOpenFeignVersion: String by project
val springCloudHystrixVersion: String by project
val springCloudRibbonVersion: String by project

val eunoiaExceptionVersion: String by project
val eunoiaKotlinUtilVersion: String by project

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.plugin.serialization)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.kover)
	alias(libs.plugins.coveralls)
	alias(libs.plugins.soraqube)
}

val catalog = libs

allprojects {
	group = groupName
	version = getGitHash()

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlinx.kover")

	java {
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}

	kotlin {
		jvmToolchain(21)

		compilerOptions {
			freeCompilerArgs.addAll("-Xjsr305=strict")
		}
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
	apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")

	dependencies {
		// maven bom
		implementation(platform(catalog.kotlinx.serialization.bom))
		implementation(platform(catalog.kotlinx.coroutines.bom))
		implementation(platform(catalog.spring.boot.bom))
		implementation(platform(catalog.spring.cloud.bom))
		implementation(platform(catalog.querydsl.bom))
		implementation(platform(catalog.dgs.bom))
		implementation(platform(catalog.springdoc.openapi.bom))
		implementation(platform(catalog.aws.sdk.bom))
		implementation(platform(catalog.aws.sdk.kotlin.bom))
		implementation(platform(catalog.aws.smithy.kotlin.bom))
		implementation(platform(catalog.opentelemetry.instrumentation.bom))

		// common dependencies
		implementation(catalog.kotlinx.serialization.json)
		implementation(catalog.bundles.kotlinx.coroutines)
		implementation(catalog.kotlin.logging)
		implementation(catalog.opentelemetry.spring.boot.starter)
		implementation(catalog.spring.boot.starter.validation)
		implementation(catalog.eunoia.aws)
		implementation(catalog.eunoia.exception)
		implementation(catalog.eunoia.kotlin.util)
		implementation(catalog.eunoia.security)

		// test implementation maven bom
		testImplementation(platform(catalog.kotest.bom))

		// common test dependencies
		testImplementation(catalog.bundles.kotest.test.suite)
		testImplementation(catalog.spring.boot.starter.test)

//		developmentOnly("org.springframework.boot:spring-boot-devtools")
//		testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
	}
}

dependencies {
	kover(project(":api"))
	kover(project(":domain"))
	kover(project(":infrastructure"))
}

fun getGitHash(): String = providers.exec {
	commandLine("git", "rev-parse", "--short=10", "HEAD")
}.standardOutput.asText.get().trim()

coverallsJacoco {
	reportPath = "${projectDir}/build/reports/kover/report.xml"
	reportSourceSets = subprojects.map { it.sourceSets.main.get().allSource.srcDirs.toList() }
		.toList().flatten()
}

sonar {
	properties {
		property("sonar.projectKey", "lomeone_lomeone-server")
		property("sonar.organization", "lomeone")
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.coverage.jacoco.xmlReportPaths", "${projectDir}/build/reports/kover/report.xml")
	}
}
