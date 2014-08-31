/*
 * Copyright 1&1 Internet AG, https://github.com/1and1/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.oneandone.maven.plugins.releasehelper;

import org.apache.maven.archiver.ManifestConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;

/**
* Created by mirko on 31.08.14.
*/
class ManifestCreator {
    final Manifest manifest;

    ManifestCreator(MavenSession session, MavenProject project) throws ManifestException, DependencyResolutionRequiredException {
        final ManifestConfiguration manifestConfiguration = new ManifestConfiguration();
        manifestConfiguration.setAddDefaultImplementationEntries(true);
        manifestConfiguration.setAddDefaultSpecificationEntries(true);
        manifestConfiguration.setAddClasspath(true);
        final MavenArchiver mavenArchiver = new MavenArchiver();
        manifest = mavenArchiver.getManifest(session, project, manifestConfiguration);
    }
    void addManifestEntry(String name, String value) throws ManifestException {
        manifest.addConfiguredAttribute(new Manifest.Attribute(name, value));
    }
    Manifest getManifest() {
        return manifest;
    }
}
