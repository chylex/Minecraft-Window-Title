import org.gradle.api.file.DuplicatesStrategy.EXCLUDE
import java.text.SimpleDateFormat
import java.util.Date

val modId: String by project
val modName: String by project
val modDescription: String by project
val modAuthor: String by project
val modVersion: String by project
val modLicense: String by project
val modSourcesURL: String by project
val modIssuesURL: String by project

val minecraftVersion: String by project
val mixinVersion: String by project

val modNameStripped = modName.replace(" ", "")
val jarVersion = "$minecraftVersion+v$modVersion"

buildscript {
	repositories {
		maven("https://repo.spongepowered.org/maven")
	}
}

plugins {
	`java-library`
	idea
	id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

idea {
	module {
		excludeDirs.add(file("gradle"))
		excludeDirs.add(file("run"))
		
		if (findProject(":Forge") == null) {
			excludeDirs.add(file("Forge"))
		}
		
		if (findProject(":Fabric") == null) {
			excludeDirs.add(file("Fabric"))
		}
	}
}

repositories {
	maven("https://repo.spongepowered.org/maven")
	mavenCentral()
}

dependencies {
	implementation("org.spongepowered:mixin:$mixinVersion")
	api("com.google.code.findbugs:jsr305:3.0.2")
}

base {
	archivesName.set("$modNameStripped-Common")
}

minecraft {
	version(minecraftVersion)
	runs.clear()
}

allprojects {
	group = "com.$modAuthor.$modId"
	version = modVersion
	
	apply(plugin = "java")
	
	dependencies {
		implementation("org.jetbrains:annotations:22.0.0")
	}
	
	extensions.getByType<JavaPluginExtension>().apply {
		toolchain.languageVersion.set(JavaLanguageVersion.of(16))
	}
	
	tasks.withType<JavaCompile> {
		options.encoding = "UTF-8"
		options.release.set(16)
	}
}

subprojects {
	repositories {
		maven("https://repo.spongepowered.org/maven")
	}
	
	dependencies {
		implementation(rootProject)
	}
	
	base {
		archivesName.set("$modNameStripped-${project.name}")
	}
	
	tasks.withType<JavaCompile> {
		source({ rootProject.sourceSets.main.get().allSource })
	}
	
	tasks.processResources {
		from(rootProject.sourceSets.main.get().resources)
		
		inputs.property("name", modName)
		inputs.property("description", modDescription)
		inputs.property("version", modVersion)
		inputs.property("author", modAuthor)
		inputs.property("license", modLicense)
		inputs.property("sourcesURL", modSourcesURL)
		inputs.property("issuesURL", modIssuesURL)
	}
	
	tasks.jar {
		archiveVersion.set(jarVersion)
		
		from(rootProject.file("LICENSE"))
		
		manifest {
			attributes(
				"Specification-Title" to modId,
				"Specification-Vendor" to modAuthor,
				"Specification-Version" to "1",
				"Implementation-Title" to "$modNameStripped-${project.name}",
				"Implementation-Vendor" to modAuthor,
				"Implementation-Version" to modVersion,
				"Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
				"MixinConfigs" to "$modId.mixins.json"
			)
		}
	}
}

tasks.register("setupIdea") {
	group = "mod"
	
	dependsOn(tasks.findByName("decompile"))
	
	val forge = findProject(":Forge")
	if (forge != null) {
		dependsOn(forge.tasks.getByName("genIntellijRuns"))
	}
	
	val fabric = findProject(":Fabric")
	if (fabric != null) {
		dependsOn(fabric.tasks.getByName("genSources"))
	}
}

val copyJars = tasks.register<Copy>("copyJars") {
	group = "build"
	duplicatesStrategy = EXCLUDE
	
	for (subproject in subprojects) {
		dependsOn(subproject.tasks.assemble)
		from(subproject.base.libsDirectory.file("${subproject.base.archivesName.get()}-$jarVersion.jar"))
	}
	
	into(file("${project.buildDir}/dist"))
}

tasks.assemble {
	finalizedBy(copyJars)
}
