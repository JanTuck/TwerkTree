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
    implementation("com.esotericsoftware", "reflectasm", "1.11.9") // Used for the "other" compatibility implementation

    /*
    compileOnly(files("libs/PaperSpigot-1.7.10-R0.1-SNAPSHOT-latest.jar"))
    compileOnly("org.spigotmc", "spigot", "1.8.8-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.9.4-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.10.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.11-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.14.4-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.15.2-R0.1-SNAPSHOT")
     */
    //compileOnly("org.spigotmc", "spigot", "1.16.1-R0.1-SNAPSHOT")

    //compileOnly("org.spigotmc", "spigot", "1.13.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.14.4-R0.1-SNAPSHOT")
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