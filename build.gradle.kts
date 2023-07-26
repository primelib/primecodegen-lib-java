// Plugins
plugins {
    `java-library`
    id("me.philippheuer.configuration") version "0.10.7"
}

version = properties["version"] as String

// All-Projects
allprojects {
    apply(plugin = "me.philippheuer.configuration")

    projectConfiguration {
        language.set(me.philippheuer.projectcfg.domain.ProjectLanguage.JAVA)
        type.set(me.philippheuer.projectcfg.domain.ProjectType.LIBRARY)
        javaVersion.set(JavaVersion.VERSION_17)
        artifactGroupId.set("io.github.primelib.primecodegenlib.java")

        pom = { pom ->
            pom.url.set("https://github.com/primelib/primecodegen-lib-java")
            pom.issueManagement {
                system.set("GitHub")
                url.set("https://github.com/primelib/primecodegen-lib-java")
            }
            pom.inceptionYear.set("2023")
            pom.developers {
                developer {
                    id.set("PhilippHeuer")
                    name.set("Philipp Heuer")
                    email.set("git@philippheuer.me")
                    roles.addAll("maintainer")
                }
            }
            pom.licenses {
                license {
                    name.set("MIT Licence")
                    distribution.set("repo")
                    url.set("https://github.com/primelib/primecodegen-lib-java/blob/main/LICENSE")
                }
            }
            pom.scm {
                connection.set("scm:git:https://github.com/primelib/primecodegen-lib-java.git")
                developerConnection.set("scm:git:git@github.com:primelib/primecodegen-lib-java.git")
                url.set("https://github.com/primelib/primecodegen-lib-java")
            }
        }
    }
}

// Subprojects
subprojects {
    if (!name.contains("bom")) {
        apply(plugin = "java-library")

        dependencies {
            // bom
            add("api", platform("io.github.openfeign:feign-bom:12.4"))
            add("api", platform("io.github.resilience4j:resilience4j-bom:2.1.0"))
        }
    }
}
