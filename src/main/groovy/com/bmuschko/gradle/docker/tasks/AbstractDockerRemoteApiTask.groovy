/*
 * Copyright 2014 the original author or authors.
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
package com.bmuschko.gradle.docker.tasks

import com.bmuschko.gradle.docker.utils.ThreadContextClassLoader
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

abstract class AbstractDockerRemoteApiTask extends DefaultTask {
    /**
     * Classpath for Docker Java libraries.
     */
    @InputFiles
    FileCollection classpath

    /**
     * Docker remote API server URL. Defaults to "http://localhost:2375".
     */
    @Input
    String url = 'http://localhost:2375'

    /**
     * Path to the <a href="https://docs.docker.com/articles/https/">Docker certificate and key</a>.
     */
    @InputDirectory
    @Optional
    File certPath

    ThreadContextClassLoader threadContextClassLoader

    @TaskAction
    void start() {
        threadContextClassLoader.withClasspath(getClasspath().files, createDockerClientConfig()) { dockerClient ->
            runRemoteCommand(dockerClient)
        }
    }

    private DockerClientConfiguration createDockerClientConfig() {
        DockerClientConfiguration dockerClientConfig = new DockerClientConfiguration()
        dockerClientConfig.url = getUrl()
        dockerClientConfig.certPath = getCertPath()
        dockerClientConfig
    }

    abstract void runRemoteCommand(dockerClient)
}

