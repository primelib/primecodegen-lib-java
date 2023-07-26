pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
	}
}

rootProject.name = "primecodegen-lib-java"

include(
	"bom",
	"feign-common",
	"feign-resilience4j",
)
