group = rootProject.group
version = rootProject.version

val springCloudAWSVersion: String by rootProject
val awsSecretsMangerJDBCVersion: String by rootProject

dependencies {
    implementation(project(":domain"))

    implementation("org.springframework.cloud:spring-cloud-starter-aws:$springCloudAWSVersion")
    implementation("org.springframework.cloud:spring-cloud-starter-aws-secrets-manager-config:$springCloudAWSVersion")
    implementation("com.amazonaws.secretsmanager:aws-secretsmanager-jdbc:$awsSecretsMangerJDBCVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
