import java.io.ByteArrayOutputStream

group = rootProject.group
version = rootProject.version

val dgsVersion: String by project
val springdocOpenapiVersion: String by project
val prometheusVersion: String by project
val micrometerTracingVersion: String by project
val opentelemetryVersion: String by project

val imageRegistry: String by project
val serviceName: String by project

plugins {
    id("com.netflix.dgs.codegen")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    implementation(project(":util"))

    // Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // DGS
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:$dgsVersion"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars:$dgsVersion")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenapiVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springdocOpenapiVersion")

    // Observability
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus:${prometheusVersion}")
    implementation("io.micrometer:micrometer-tracing:${micrometerTracingVersion}")
    implementation("io.micrometer:micrometer-tracing-bridge-brave:${micrometerTracingVersion}")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:${opentelemetryVersion}")
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
        image = "amazoncorretto:21"
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

fun getGitCurrentBranch(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "rev-parse", "--abbrev-ref", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim().replace("/","-")
}

fun getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
