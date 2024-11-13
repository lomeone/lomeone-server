group = rootProject.group
version = rootProject.version

val springCloudAWSVersion: String by rootProject
val awsSecretsMangerJDBCVersion: String by rootProject
val awsStsVersion: String by rootProject

dependencies {
    implementation(project(":domain"))

    // AWS
    implementation("io.awspring.cloud:spring-cloud-aws-starter:$springCloudAWSVersion")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-secrets-manager:$springCloudAWSVersion")
    implementation("com.amazonaws.secretsmanager:aws-secretsmanager-jdbc:$awsSecretsMangerJDBCVersion")
    implementation("software.amazon.awssdk:sts:$awsStsVersion")

    // JDBC Driver
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
