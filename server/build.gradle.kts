plugins {
    kotlin("jvm").version("1.5.10")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.websockets)
    implementation(libs.koin.ktor)
    implementation(libs.bundles.log4j)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}
