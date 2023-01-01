import java.io.ByteArrayOutputStream

group = rootProject.group
version = rootProject.version

val springdocOpenapiVersion: String by project

val imageRegistry: String by project
val serviceName: String by project

dependencies {
    implementation(project(":domain"))
    implementation(project(":service"))
    implementation(project(":infrastructure"))

    // Swagger
    implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenapiVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springdocOpenapiVersion")
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
