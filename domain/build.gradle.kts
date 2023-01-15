group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":util"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
