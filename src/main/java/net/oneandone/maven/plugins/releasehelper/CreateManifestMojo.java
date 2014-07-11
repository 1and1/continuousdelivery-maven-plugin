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
package net.oneandone.maven.plugins.releasehelper;

import org.apache.maven.archiver.ManifestConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Developer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by mirko on 10.07.14.
 */
@Mojo(name = "create-manifest", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresProject = true, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class CreateManifestMojo extends AbstractMojo {
    private static final String CREATE_MANIFEST_MOJO_BUILD_REVISION = "createManifestMojo.buildRevision";

    @Parameter(defaultValue = "${project.url}", readonly = true, required = true)
    private String projectUrl;

    @Parameter(defaultValue = "${project.groupId}", readonly = true, required = true)
    private String projectGroupId;

    @Parameter(defaultValue = "${project.artifactId}", readonly = true, required = true)
    private String projectArtifactId;

    @Parameter(defaultValue = "${project.version}", readonly = true, required = true)
    private String projectVersion;

    @Parameter(defaultValue = "${project.scm.developerConnection}", readonly = true, required = true)
    private String scmDeveloperConnection;

    @Parameter(defaultValue = "${project.scm.connection}", readonly = true, required = true)
    private String scmConnection;

    @Parameter(defaultValue = "${project.scm.url}", readonly = true, required = true)
    private String scmUrl;

    @Parameter(defaultValue = "${project.developers}", readonly = true, required = true)
    private List<Developer> developers;

    @Parameter(defaultValue = "${project.issueManagement.url}", readonly = true, required = true)
    private String issueManagement;

    @Parameter(defaultValue = "${basedir}", readonly = true, required = true)
    private File baseDir;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    @Component
    private ScmManager scmManager;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final String revision;
        final List<String> dependencies;
        try {
            dependencies = project.getRuntimeClasspathElements();
            final Properties properties = session.getSystemProperties();
            ;
            if (properties.containsKey(CREATE_MANIFEST_MOJO_BUILD_REVISION)) {
                revision = properties.getProperty(CREATE_MANIFEST_MOJO_BUILD_REVISION);
            } else {
                final ScmRepository repository;
                final InfoScmResult info;
                repository = scmManager.makeScmRepository(StringUtils.isBlank(scmDeveloperConnection) ? scmConnection : scmDeveloperConnection);
                final ScmProvider provider = scmManager.getProviderByRepository(repository);
                info = provider.info(
                        repository.getProviderRepository(), new ScmFileSet(baseDir), new CommandParameters());
                revision = info.getInfoItems().get(0).getRevision();
                properties.setProperty(CREATE_MANIFEST_MOJO_BUILD_REVISION, revision);
            }
            final ManifestConfiguration manifestConfiguration = new ManifestConfiguration();
            manifestConfiguration.setAddDefaultImplementationEntries(true);
            manifestConfiguration.setAddDefaultSpecificationEntries(true);
            final MavenArchiver mavenArchiver = new MavenArchiver();
            final Manifest manifest;
            manifest = mavenArchiver.getManifest(session, project, manifestConfiguration);
            manifest.addConfiguredAttribute(new Manifest.Attribute("Developers", DeveloperDecorator.fromDevelopers(developers)));
            manifest.addConfiguredAttribute(new Manifest.Attribute("Dependencies", dependencies.toString()));
            manifest.write(System.out);
        } catch (ManifestException | IOException | DependencyResolutionRequiredException | ScmException e) {
            throw new MojoExecutionException("Oops", e);
        }
    }
}
