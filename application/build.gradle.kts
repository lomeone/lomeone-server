import java.io.ByteArrayOutputStream

group = rootProject.group
version = rootProject.version

val dgsVersion: String by project
val springdocOpenapiVersion: String by project

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
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // DGS
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:$dgsVersion"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars:$dgsVersion")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenapiVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springdocOpenapiVersion")
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
    val tags = setOf<String>()
    val branch = getGitCurrentBranch()
    if (branch.isNotBlank()) {
        tags.plus(branch)
    }
    tags.plus(getGitHash())

    println(branch)
    println(getGitHash())

    println(tags.toString())
    return tags
}

fun getGitCurrentBranch(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "branch", "--show-current")
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
