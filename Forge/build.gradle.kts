import net.minecraftforge.gradle.userdev.UserDevExtension
import org.gradle.api.file.DuplicatesStrategy.INCLUDE
import org.spongepowered.asm.gradle.plugins.MixinExtension

val modId: String by project
val minecraftVersion: String by project
val forgeVersion: String by project
val mixinVersion: String by project

buildscript {
	repositories {
		maven("https://maven.minecraftforge.net")
		maven("https://repo.spongepowered.org/maven")
		mavenCentral()
	}
	
	dependencies {
		classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "5.1.+") { isChanging = true }
		classpath(group = "org.spongepowered", name = "mixingradle", version = "0.7-SNAPSHOT")
	}
}

plugins {
	java
	eclipse
}

apply {
	plugin("net.minecraftforge.gradle")
	plugin("org.spongepowered.mixin")
}

dependencies {
	"minecraft"("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")
	
	if (System.getProperty("idea.sync.active") != "true") {
		annotationProcessor("org.spongepowered:mixin:$mixinVersion:processor")
	}
}

configure<UserDevExtension> {
	mappings("official", minecraftVersion)
	
	runs {
		create("client") {
			taskName = "Client"
			workingDirectory(rootProject.file("run"))
			ideaModule("${rootProject.name}.${project.name}.main")
			
			property("mixin.env.remapRefMap", "true")
			property("mixin.env.refMapRemappingFile", "$projectDir/build/createSrgToMcp/output.srg")
			arg("-mixin.config=$modId.mixins.json")
			
			mods {
				create(modId) {
					source(sourceSets.main.get())
					source(rootProject.sourceSets.main.get())
				}
			}
		}
	}
}

configure<MixinExtension> {
	add(sourceSets.main.get(), "$modId.refmap.json")
}

tasks.processResources {
	from(sourceSets.main.get().resources.srcDirs) {
		include("META-INF/mods.toml")
		expand(inputs.properties)
		duplicatesStrategy = INCLUDE
	}
}

tasks.jar {
	finalizedBy("reobfJar")
}
