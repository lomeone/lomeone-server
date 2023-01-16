group = rootProject.group
version = rootProject.version

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
