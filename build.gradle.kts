import java.net.URI

plugins {
    java
    `maven-publish`
    signing

    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

configurations.all {
    attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.8.1")
}

group = "com.intellectualsites.prtree"
version = "2.0.1-SNAPSHOT"

tasks {

    compileJava {
        options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "1000"))
        options.compilerArgs.add("-Xlint:all")
        for (disabledLint in arrayOf("processing", "path", "fallthrough", "serial"))
            options.compilerArgs.add("-Xlint:$disabledLint")
        options.isDeprecation = true
        options.encoding = "UTF-8"
    }

    javadoc {
        val opt = options as StandardJavadocDocletOptions
        opt.addStringOption("Xdoclint:none", "-quiet")
        opt.tags(
            "apiNote:a:API Note:",
            "implSpec:a:Implementation Requirements:",
            "implNote:a:Implementation Note:"
        )
        opt.links("https://docs.oracle.com/javase/8/docs/api/")
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

signing {
    if (!version.toString().endsWith("-SNAPSHOT")) {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
        signing.isRequired
        sign(publishing.publications)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {

                name.set(project.name + " " + project.version)
                description.set("PRTree is a Priority R-Tree, a spatial index.")
                url.set("https://github.com/IntellectualSites/PRTree")

                licenses {
                    license {
                        name.set("BSD-3-Clause")
                        url.set("https://opensource.org/licenses/BSD-3-Clause")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("NotMyFault")
                        name.set("NotMyFault")
                        organization.set("IntellectualSites")
                        email.set("contact(at)notmyfault.dev")
                    }
                }

                scm {
                    url.set("https://github.com/IntellectualSites/PRTree")
                    connection.set("scm:https://IntellectualSites@github.com/IntellectualSites/PRTree.git")
                    developerConnection.set("scm:git://github.com/IntellectualSites/PRTree.git")
                }

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/IntellectualSites/PRTree/issues")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(URI.create("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(URI.create("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
