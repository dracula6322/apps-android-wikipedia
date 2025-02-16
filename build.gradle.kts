buildscript {
    dependencies {
        val r8Jar: ConfigurableFileCollection = files("./libs/r8-8_8_18.jar")
        val file = r8Jar.first()
        println(file.path)
        println(file.exists())
        classpath(files(file))
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp.plugin) apply false
    alias(libs.plugins.gms.plugin) apply false
    alias(libs.plugins.compose.compiler) apply  false
}

println("com.android.tools.r8.Version.getVersionString = " + com.android.tools.r8.Version.getVersionString())