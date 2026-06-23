val imageRegistry: String by project
val serviceName: String by project

plugins {
    alias(libs.plugins.dgs.codegen)
    alias(libs.plugins.jib)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure"))

    // Web
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.websocket)

    // GraphQL
    implementation(libs.bundles.dgs)

    // Swagger
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // Observability
    implementation(libs.spring.boot.starter.actuator)

    // Eunoia
    implementation(libs.eunoia.spring.web)
}

tasks.bootJar { enabled = true }

tasks {
    generateJava {
        schemaPaths = mutableListOf("$projectDir/src/main/resources/schema")
        packageName = "com.lomeone.generated"
        generateClientv2 = true
    }
}

jib {
    from {
        image = "396428372646.dkr.ecr.ap-northeast-2.amazonaws.com/ecr-public/amazoncorretto/amazoncorretto:21-al2023-headless"
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "$imageRegistry/$serviceName"
        tags = setOf(version.toString(), "dev")
    }
}
