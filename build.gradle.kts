plugins {
    java
    application
}

group = "me.morty.bot"
version = "1.0"

application {
    mainClass.set("me.morty.bot.App")
}

java {
    sourceCompatibility = JavaVersion.VERSION_23
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven { url = uri("https://m2.dv8tion.net/releases") }
    maven {url = uri("https://maven.lavalink.dev/releases")}
}

dependencies {
    // Discord API
    implementation("net.dv8tion", "JDA", "5.3.0")
//    implementation("com.jagrosh", "jda-utilities", "3.0.5")

    // Logging
    implementation("ch.qos.logback", "logback-classic", "1.5.17")

    // Player
    implementation("dev.arbjerg", "lavaplayer", "2.2.3")
    implementation("dev.lavalink.youtube:common:1.11.5")
//    implementation("dev.lavalink", "youtube:common","1.11.5")

    // Utils
    implementation("org.reflections", "reflections", "0.10.2")

    // Tests
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.12.0")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.12.0")
}

tasks {
    register<Copy>("copyLibs") {
        from(configurations.runtimeClasspath)
        into("${layout.buildDirectory}/libs")
    }

    compileJava {
        options.encoding = "UTF-8"
    }

//    test {
//        useJUnitPlatform()
//    }

    jar {
        manifest {
            attributes["Main-Class"] = "me.morty.bot.App"
        }
    }

    register("stage") {
        dependsOn("copyLibs", jar, test)
    }
}
