import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("kr.entree.spigradle") version "1.2.4"
}
allprojects{
    apply(plugin = "kotlin")
    repositories{
        mavenCentral()
        mavenLocal()
    }
    dependencies{
        implementation(kotlin("stdlib"))
    }
}
group = "me.jantuck"
version = "1.0-SNAPSHOT"

repositories {
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://repo.aikar.co/content/groups/aikar/")
    maven(url = "https://nexus.okkero.com/repository/maven-releases/")
}

dependencies {
    implementation(project(":1_16Compat"))
    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.1-R0.1-SNAPSHOT") // For the bukkit api
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
        minimize()
    }
}