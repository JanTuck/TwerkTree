import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("kr.entree.spigradle") version "1.2.4"
}

group = "me.jantuck"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.esotericsoftware", "reflectasm", "1.11.9") // Used for the "other" compatibility implementation
    compileOnly("com.destroystokyo.paper:paper-api:1.16.1-R0.1-SNAPSHOT")
}

spigot {
    authors = listOf("JanTuck")
    apiVersion = "1.13"
}


tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        val packageName = "${project.group}.${project.name.toLowerCase()}"
        relocate("kotlin", "$packageName.shaded.kotlin")
        relocate("com.esotericsoftware", "$packageName.shaded")
        minimize()
    }
}