/*
 * 1&1 Internet AG, https://github.com/1and1/.
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
package net.oneandone.maven.plugins.continuousdelivery;

import java.util.Locale;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

/**
 * Goal which calculates properties {@literal newVersion} and {@literal releaseVersion} from a SNAPSHOT enhanced by Jenkins' build
 * number.
 */
@Mojo(name = "set-new-version", requiresProject = true, requiresDirectInvocation = true)
public class SetNewVersionPropertyMojo extends AbstractMojo {

    private static final String BUILD_NUMBER = "BUILD_NUMBER";
    private static final String SNAPSHOT = "-SNAPSHOT";
    @Component
    private MavenProject project;

    public SetNewVersionPropertyMojo() {
    }

    /**
     * For tests.
     */
    SetNewVersionPropertyMojo(MavenProject project) {
        this.project = project;
    }

    @Override
    public void execute() throws MojoExecutionException {
        final String version = project.getVersion();
        if (!version.endsWith(SNAPSHOT)) {
            throw new MojoExecutionException("Not a SNAPSHOT version!!");
        }
        final String buildNumber = getBuildNumber();
        if (buildNumber == null) {
            throw new MojoExecutionException(String.format(Locale.ENGLISH, "Environment variable '%s' not set!", BUILD_NUMBER));
        }
        final String newVersion = String.format(Locale.ENGLISH, "%s.%s", version.subSequence(0, version.length() - SNAPSHOT.length()), buildNumber);
        getLog().info(String.format(Locale.ENGLISH, "Setting property newVersion and releaseVersion to: %s", newVersion));
        final Properties properties = project.getProperties();
        properties.setProperty("newVersion", newVersion);
        properties.setProperty("releaseVersion", newVersion);
        getLog().info("Setting property generateBackupPoms to false.");
        properties.setProperty("generateBackupPoms", "false");
    }

    String getBuildNumber() {
        return System.getenv(BUILD_NUMBER);
    }
}
