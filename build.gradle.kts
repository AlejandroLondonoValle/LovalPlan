// Archivo raiz: misma configuracion que ya funciono en InventarioApp (fase 1).
// AGP 9 trae Kotlin integrado; fijamos el Kotlin Gradle Plugin para que Kotlin,
// el compilador de Compose y Kotlin Serialization usen la misma version.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.20")
    }
}

plugins {
    id("com.android.application") version "9.5.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.20" apply false
    id("com.google.dagger.hilt.android") version "2.60.1" apply false
    id("com.google.gms.google-services") version "4.6.0" apply false
    id("com.google.devtools.ksp") version "2.3.12" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.20" apply false
}
