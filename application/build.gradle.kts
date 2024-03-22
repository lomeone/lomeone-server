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
    to {
        image = "$imageRegistry/$serviceName"
        tags = setOf(getGitCurrentBranch())
    }
}

fun getGitCurrentBranch(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "rev-parse", "--abbrev-ref", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
