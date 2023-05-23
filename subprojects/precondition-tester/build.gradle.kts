/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("gradlebuild.internal.java")
}

description = "Internal project testing and collecting information about all the test preconditons."

dependencies {
    testImplementation(platform(project(":distributions-dependencies"))) {
        because("Then there will be actual versions for the `lib.*` coming from 'gradlebuild.dependency-modules'")
    }

    testImplementation(project(":internal-testing")) {
        because("Basic predicate classes come from here")
    }
    testRuntimeOnly(project(":distributions-core")) {
        because("Tests instantiate DefaultClassLoaderRegistry which requires a 'gradle-plugins.properties' through DefaultPluginModuleRegistry")
    }

    /**
     * List subprojects, which has their own preconditions.
     * These projects should have their preconditions in the "src/testFixtures" sourceSet
     */
    testRuntimeOnly(testFixtures(project(":plugins")))
    testRuntimeOnly(testFixtures(project(":signing")))
    testRuntimeOnly(testFixtures(project(":test-kit")))
    testRuntimeOnly(testFixtures(project(":smoke-test")))

    testImplementation(libs.junit5JupiterApi) {
        because("Assume API comes from here")
    }
    testImplementation(libs.spock)
}

tasks {
    withType(Test::class) {
        testClassesDirs = sourceSets.test.get().output.classesDirs
        classpath = sourceSets.test.get().runtimeClasspath

        predictiveSelection {
            enabled = false
        }
    }
}
