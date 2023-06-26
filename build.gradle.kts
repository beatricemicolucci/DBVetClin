plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javaFXModules = listOf(
        "base",
        "controls",
        "fxml",
        "swing",
        "graphics",
        "media"
)

val supportedPlatforms = listOf("linux", "mac", "win")

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    val javaFxVersion = 19
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}