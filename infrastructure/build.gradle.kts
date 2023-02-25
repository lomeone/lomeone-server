group = rootProject.group
version = rootProject.version

val springCloudAWSVersion: String by rootProject
val awsSecretsMangerJDBCVersion: String by rootProject
val postgresqlVersion: String by rootProject

dependencies {
    implementation(project(":domain"))

    // AWS
    implementation("org.springframework.cloud:spring-cloud-starter-aws:$springCloudAWSVersion")
    implementation("org.springframework.cloud:spring-cloud-starter-aws-secrets-manager-config:$springCloudAWSVersion")
    implementation("com.amazonaws.secretsmanager:aws-secretsmanager-jdbc:$awsSecretsMangerJDBCVersion")

    // JDBC Driver
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
