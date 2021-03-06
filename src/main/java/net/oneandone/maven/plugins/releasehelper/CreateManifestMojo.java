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

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Developer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.manager.ScmManager;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.apache.maven.shared.utils.io.FileUtils.forceMkdir;

/**
 * Created by mirko on 10.07.14.
 */
@Mojo(name = "create-manifest", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresProject = true, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class CreateManifestMojo extends AbstractMojo {

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

    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    private File targetDir;

    @Component
    private ScmManager scmManager;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            final String revision = new SCMRevision(scmDeveloperConnection, scmConnection, scmManager, baseDir, session.getSystemProperties()).getSCMRevision();
            final ManifestCreator manifestCreator = new ManifestCreator(session, project);
            final String gav = String.format(Locale.ENGLISH, "%s:%s:%s",
                    projectGroupId, projectArtifactId, projectVersion);
            manifestCreator.addManifestEntry("GAV", gav);
            manifestCreator.addManifestEntry("Developers", DeveloperDecorator.fromDevelopers(developers));
            manifestCreator.addManifestEntry("Build-SCM-Revision", revision);
            manifestCreator.addManifestEntry("URL", projectUrl);
            manifestCreator.addManifestEntry("SCM-URL", scmUrl);
            manifestCreator.addManifestEntry("SCM-Connection", scmConnection);
            manifestCreator.addManifestEntry("SCM-Developer-Connection", scmDeveloperConnection);
            manifestCreator.addManifestEntry("Issue-Management-URL", issueManagement);
            final Manifest manifest = manifestCreator.getManifest();
            forceMkdir(targetDir);
            try (FileOutputStream o = new FileOutputStream(new File(targetDir, "MANIFEST.MF"))) {
                manifest.write(o);
            }
        } catch (ManifestException | IOException | DependencyResolutionRequiredException | ScmException e) {
            throw new MojoExecutionException("Oops", e);
        }
    }


}
