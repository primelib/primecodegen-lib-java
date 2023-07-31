pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
	}
}

rootProject.name = "primecodegen-lib-java"

include(
	"bom",
	"common",
	"feign-common",
	"feign-resilience4j",
	"feign-blackbird",
)
