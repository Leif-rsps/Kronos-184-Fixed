/*
 * Copyright (c) 2019 Owain van Brakel <https://github.com/Owain94>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Plugins.versionsPlugin)
    }
}

plugins {
    id(Plugins.testLogger.first) version Plugins.testLogger.second apply false
    id(Plugins.versions.first) version Plugins.versions.second
    id(Plugins.latestVersion.first) version Plugins.latestVersion.second

    `maven-publish`

    application
}

val localGitCommit = "3.2"
val localGitCommitShort = "3.2"

allprojects {
    group = "com.openosrs"
    version = ProjectVersions.rlVersion

    project.extra["gitCommit"] = localGitCommit
    project.extra["gitCommitShort"] = localGitCommitShort

    project.extra["rootPath"] = rootDir.toString().replace("\\", "/")
    project.extra["injectedClassesPath"] = "${rootDir}/injector-plugin/out/injected-client/"
}

subprojects {
    apply<JavaLibraryPlugin>()

    repositories {
        mavenLocal()

        maven(url = "https://mvnrepository.com/artifact")
        maven(url = "https://repo1.maven.org/maven2")
        maven(url = "https://repo.runelite.net")
        maven(url = "https://repo.maven.apache.org/maven2")
        maven(url = "https://raw.githubusercontent.com/open-osrs/hosting/master")

        if (System.getenv("NEXUS-URL") != null) {
            maven(url = System.getenv("NEXUS-URL"))
        }
    }

    tasks {
        java {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
    }
}

application {
    mainClass.set("net.runelite.client.RuneLite")
}

tasks {
    named<JavaExec>("run") {
        classpath = project(":runelite-client").sourceSets.main.get().runtimeClasspath
    }

}