@file:Suppress("ConvertLambdaToReference")

import org.gradle.jvm.tasks.Jar

val modId: String by project
val modName: String by project
val modDescription: String by project
val modAuthor: String by project
val modVersion: String by project
val modLicense: String by project
val modSourcesURL: String by project
val modIssuesURL: String by project
val modSides: String by project

val minecraftVersion: String by project
val mixinVersion: String by project

val minimumMinecraftVersion: String by project
val minimumNeoForgeVersion: String by project
val minimumFabricVersion: String by project

val modNameStripped = modName.replace(" ", "")

plugins {
	idea
	`java-library`
	id("fabric-loom")
}

idea {
	module {
		excludeDirs.add(file("gradle"))
		excludeDirs.add(file("run"))
		
		if (findProject(":NeoForge") == null) {
			excludeDirs.add(file("NeoForge"))
		}
		
		if (findProject(":Fabric") == null) {
			excludeDirs.add(file("Fabric"))
		}
	}
}

repositories {
	mavenCentral()
}

base {
	archivesName.set("$modNameStripped-Common")
}

loom {
	runs {
		named("client") {
			ideConfigGenerated(false)
		}
		
		findByName("server")?.let(::remove)
	}
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	mappings(loom.officialMojangMappings())
	
	compileOnly("net.fabricmc:sponge-mixin:$mixinVersion")
	api("com.google.code.findbugs:jsr305:3.0.2")
}

allprojects {
	group = "com.$modAuthor.$modId"
	version = modVersion
	
	apply(plugin = "java-library")
	
	extensions.getByType<JavaPluginExtension>().apply {
		toolchain.languageVersion.set(JavaLanguageVersion.of(21))
	}
	
	tasks.withType<JavaCompile> {
		options.encoding = "UTF-8"
		options.release.set(21)
	}
	
	val runJvmArgs = mutableSetOf<String>().also {
		extra["runJvmArgs"] = it
	}
	
	if (project.javaToolchains.launcherFor(java.toolchain).map { it.metadata.vendor }.orNull == "JetBrains") {
		runJvmArgs.add("-XX:+AllowEnhancedClassRedefinition")
	}
	
	dependencies {
		implementation("org.jetbrains:annotations:24.1.0")
	}
	
	tasks.withType<ProcessResources> {
		val (sidesForNeoForge, sidesForFabric) = when (modSides) {
			"both"   -> Pair("BOTH", "*")
			"client" -> Pair("CLIENT", "client")
			"server" -> Pair("SERVER", "server")
			else     -> error("Invalid modSides value: $modSides")
		}
		
		inputs.property("id", modId)
		inputs.property("name", modName)
		inputs.property("description", modDescription)
		inputs.property("version", modVersion)
		inputs.property("author", modAuthor)
		inputs.property("license", modLicense)
		inputs.property("sourcesURL", modSourcesURL)
		inputs.property("issuesURL", modIssuesURL)
		inputs.property("sidesForNeoForge", sidesForNeoForge)
		inputs.property("sidesForFabric", sidesForFabric)
		inputs.property("minimumMinecraftVersion", minimumMinecraftVersion)
		inputs.property("minimumNeoForgeVersion", minimumNeoForgeVersion)
		inputs.property("minimumFabricVersion", minimumFabricVersion)
	}
	
	tasks.withType<AbstractArchiveTask>().configureEach {
		isPreserveFileTimestamps = false
		isReproducibleFileOrder = true
	}
}

subprojects {
	dependencies {
		implementation(project(rootProject.path, configuration = "namedElements"))
	}
	
	base {
		archivesName.set("$modNameStripped-${project.name}")
	}
	
	listOf("compileJava", "compileTestJava").forEach {
		tasks.named<JavaCompile>(it) {
			source({ rootProject.sourceSets.main.get().allSource })
		}
	}
	
	tasks.processResources {
		from(rootProject.sourceSets.main.get().resources) {
			expand(inputs.properties)
		}
	}
	
	tasks.jar {
		entryCompression = ZipEntryCompression.STORED // Reduces size of multiloader jar.
		
		manifest {
			packageInformation(modId, "$modNameStripped-${project.name}")
		}
	}
	
	tasks.test {
		onlyIf { false }
	}
}

fun Manifest.packageInformation(specificationTitle: String, implementationTitle: String) {
	attributes(
		"Specification-Title" to specificationTitle,
		"Specification-Vendor" to modAuthor,
		"Specification-Version" to "1",
		"Implementation-Title" to implementationTitle,
		"Implementation-Vendor" to modAuthor,
		"Implementation-Version" to modVersion,
	)
}

val multiloaderSources = sourceSets.register("multiloader")

val multiloaderJar = tasks.register<Jar>("multiloaderJar") {
	group = "build"
	
	archiveBaseName.set(modNameStripped)
	archiveVersion.set("$minecraftVersion+v$modVersion")
	
	destinationDirectory = layout.buildDirectory.dir("dist")
	
	fun includeJar(project: Project, jarTaskName: String) {
		from(project.tasks.named(jarTaskName).map { it.outputs }) {
			into("jars")
			rename { "$modNameStripped-${project.name}.jar" }
		}
	}
	
	findProject(":NeoForge")?.let { includeJar(it, "jar") }
	findProject(":Fabric")?.let { includeJar(it, "uncompressedRemapJar") }
	
	from(rootProject.file("LICENSE"))
	from(multiloaderSources.map { it.output })
	
	manifest {
		packageInformation("$modId-multiloader", modNameStripped)
		attributes("FMLModType" to "GAMELIBRARY")
	}
}

tasks.named<ProcessResources>("processMultiloaderResources").configure {
	inputs.property("group", project.group)
	inputs.property("jarPrefix", modNameStripped)
	
	filesMatching(listOf("fabric.mod.json", "META-INF/jarjar/metadata.json")) {
		expand(inputs.properties)
	}
}

tasks.assemble {
	finalizedBy(multiloaderJar)
}
