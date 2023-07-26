plugins {
    `java-platform`
}

projectConfiguration {
    artifactId.set("bom")
    artifactDisplayName.set("PrimeLib Java - BOM")
    artifactDescription.set("PrimeLib Java - Bill of materials")
    type.set(me.philippheuer.projectcfg.domain.ProjectType.LIBRARY)
}

dependencies {
    constraints {
        api(project(":feign-common"))
        api(project(":feign-resilience4j"))
    }
}
