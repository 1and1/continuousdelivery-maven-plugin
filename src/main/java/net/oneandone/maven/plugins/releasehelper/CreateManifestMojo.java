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

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.manager.NoSuchScmProviderException;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;

/**
 * Created by mirko on 10.07.14.
 */
@Mojo(name = "create-manifest", defaultPhase = LifecyclePhase.INITIALIZE, requiresProject = true)
public class CreateManifestMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project.scm.developerConnection}", readonly = true)
    private String urlScm;

    @Parameter(defaultValue = "${project.scm.connection}", readonly = true)
    private String readUrlScm;

    @Parameter(defaultValue = "${basedir}", readonly = true)
    private File baseDir;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Component
    private MavenSession session;

    @Component
    private ScmManager scmManager;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final ScmRepository repository;
        final InfoScmResult info;
        try {
            repository = scmManager.makeScmRepository(StringUtils.isBlank(urlScm) ? readUrlScm : urlScm);
            final ScmProvider provider = scmManager.getProviderByRepository(repository);
            info = provider.info(
                    repository.getProviderRepository(), new ScmFileSet(baseDir), new CommandParameters());
        } catch (ScmException e) {
            throw new RuntimeException(e);
        }
        getLog().info(info.getInfoItems().get(0).getRevision());
    }
}
