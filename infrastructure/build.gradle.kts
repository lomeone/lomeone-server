import org.jetbrains.kotlin.ir.backend.js.compile

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":domain"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
