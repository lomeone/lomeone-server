import java.io.ByteArrayOutputStream

group = rootProject.group
version = rootProject.version

val dgsVersion: String by project
val springdocOpenapiVersion: String by project
val prometheusVersion: String by project
val micrometerTracingVersion: String by project
val opentelemetryVersion: String by project

val eunoiaSpringWebRestVersion: String by project

val imageRegistry: String by project
val serviceName: String by project

plugins {
    alias(libs.plugins.dgs.codegen)
    alias(libs.plugins.jib)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
//    implementation(project(":util"))

    // Web
    implementation(libs.spring.boot.starter.web) {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation(libs.spring.boot.starter.undertow) {
        exclude(module = "undertow-websockets-jsr")
    }

    // Security
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // DGS
    implementation(libs.bundles.dgs) {
        exclude(module = "spring-boot-starter-tomcat")
    }

    // Swagger
    implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenapiVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springdocOpenapiVersion")

    // Observability
    implementation(libs.spring.boot.starter.actuator)

    implementation("com.lomeone.eunoia:spring-web-rest:$eunoiaSpringWebRestVersion")
}

tasks {
    generateJava {
        schemaPaths = mutableListOf("$projectDir/src/main/resources/schema")
        packageName = "com.lomeone.generated"
        generateClientv2 = true
    }
}

jib {
    from {
        image = "public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-headless"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "$imageRegistry/$serviceName"
        tags = getImageTags()
    }
}

fun getImageTags(): Set<String> {
    val tags = mutableSetOf<String>()
    val branch = getGitCurrentBranch()

    if (branch.isNotBlank()) {
        tags.add(branch)
    }
    tags.add(getGitHash())

    return tags
}

fun getGitCurrentBranch(): String = providers.exec {
    commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
}.standardOutput.asText.get().trim().replace("/","-")

fun getGitHash(): String = providers.exec {
    commandLine("git", "rev-parse", "--short=10", "HEAD")
}.standardOutput.asText.get().trim()
