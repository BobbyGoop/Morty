plugins {
    java
}

group = "me.morty.bot.app.Bot"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}


repositories {
    gradlePluginPortal()
    mavenCentral()
    maven {
        url = uri("https://m2.dv8tion.net/releases")
    }
}
dependencies {
    // Discord API
    implementation("net.dv8tion", "JDA", "4.4.0_350")
    implementation("com.jagrosh", "jda-utilities", "3.0.5")

    // Logging
    implementation("ch.qos.logback", "logback-classic", "1.3.0-alpha5")

    // Player
    implementation("com.sedmelluq", "lavaplayer", "1.3.78")

    // Utils
    implementation("org.reflections", "reflections", "0.9.12")

    // Tests
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.7.2")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.7.2")
}

tasks {
    register<Copy>("copyLibs") {
        from(configurations.runtimeClasspath)
        into("$buildDir/libs")
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
    }

    jar {
        manifest {
            attributes["Main-Class"] = "me.morty.bot.app.Bot"
        }
    }

    register("stage") {
        dependsOn("copyLibs", jar, test)
    }
}
