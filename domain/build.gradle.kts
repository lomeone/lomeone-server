group = rootProject.group
version = rootProject.version

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    implementation(project(":util"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
