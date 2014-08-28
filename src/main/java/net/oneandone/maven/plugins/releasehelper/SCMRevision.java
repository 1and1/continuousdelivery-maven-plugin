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

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.info.InfoItem;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.shared.utils.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
* Retrieves the SCM
*/
class SCMRevision {

    public String ROLE = SCMRevision.class.getName();

    static final String CREATE_MANIFEST_MOJO_BUILD_REVISION = "createManifestMojo.scmRevision";
    private final String scmDeveloperConnection;
    private final String scmConnection;
    private final ScmManager scmManager;
    private final File baseDir;
    private final Properties properties;

    SCMRevision(String scmDeveloperConnection, String scmConnection, ScmManager scmManager, File baseDir, Properties properties) {
        this.scmDeveloperConnection = scmDeveloperConnection;
        this.scmConnection = scmConnection;
        this.scmManager = scmManager;
        this.baseDir = baseDir;
        this.properties = properties;
    }

    String getSCMRevision() throws ScmException {
        final String revision;
        if (properties.containsKey(CREATE_MANIFEST_MOJO_BUILD_REVISION)) {
            revision = properties.getProperty(CREATE_MANIFEST_MOJO_BUILD_REVISION);
        } else {
            final ScmRepository repository;
            final InfoScmResult info;
            repository = scmManager.makeScmRepository(StringUtils.isBlank(scmDeveloperConnection) ? scmConnection : scmDeveloperConnection);
            final ScmProvider provider = scmManager.getProviderByRepository(repository);
            info = provider.info(
                    repository.getProviderRepository(), new ScmFileSet(baseDir), new CommandParameters());
            final List<InfoItem> infoItems = info.getInfoItems();
            revision = getRevision(infoItems);
            properties.setProperty(CREATE_MANIFEST_MOJO_BUILD_REVISION, revision);
        }
        return revision;
    }

    String getRevision(List<InfoItem> infoItems) {
        if (!infoItems.isEmpty()) {
            return infoItems.get(0).getRevision();
        } else {
            return "UNKNOWN";
        }
    }
}
