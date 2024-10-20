plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.0.1"
}

group = "org.oleg.iem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform{
        defaultRepositories()
    }
}

dependencies {

    implementation("org.json:json:20231013")
    implementation("dev.langchain4j:langchain4j-open-ai:0.34.0")
    implementation("dev.langchain4j:langchain4j:0.34.0")
    implementation("dev.langchain4j:langchain4j-embeddings-bge-small-en-v15-q:0.34.0")

    // Testing libraries:
    implementation("org.testng:testng:7.8.0")
    implementation("io.cucumber:cucumber-testng:7.15.0")
    implementation("io.cucumber:cucumber-core:7.15.0")
    implementation("io.cucumber:cucumber-java:7.15.0")

    intellijPlatform {
        intellijIdeaCommunity("2024.2.1")
        bundledPlugin("com.intellij.java")
        // plugin provides additional functions to Gherkin Feature files:
        plugin("gherkin", "242.20224.159")
        instrumentationTools()
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
