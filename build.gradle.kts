plugins {
    id("java-library")
}

group = "net.minecraft"
version = "rd-161807"

repositories {
    mavenCentral()
}

val natives: Configuration by configurations.creating
natives.isTransitive = true

dependencies {
    implementation(group = "org.lwjgl.lwjgl", name = "lwjgl", version = "2.9.3")
    implementation(group = "org.lwjgl.lwjgl", name = "lwjgl_util", version = "2.9.3")
    natives(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", classifier = "natives-windows")
    natives(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", classifier = "natives-linux")
    natives(group = "org.lwjgl.lwjgl", name = "lwjgl-platform", version = "2.9.3", classifier = "natives-osx")
}

tasks.register<Copy>("extractNatives") {
    dependsOn(natives)
    from(natives.map { zipTree(it) })
    into(project.file("run/natives"))
}

tasks.register<JavaExec>("run") {
    jvmArgs = listOf("-Dorg.lwjgl.librarypath=${project.file("run/natives").absolutePath}")
    mainClass.set("com.mojang.minecraft.Minecraft")
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = project.file("run")
    dependsOn("extractNatives")
}
