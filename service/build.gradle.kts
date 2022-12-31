group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":domain"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
