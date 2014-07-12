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
* Created by mirko on 12.07.14.
*/
class SCMRevision {

    private static final String CREATE_MANIFEST_MOJO_BUILD_REVISION = "createManifestMojo.scmRevision";
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
            final List<InfoItem> infoItems = info.getInfoItems();
            if (!infoItems.isEmpty()) {
                revision = infoItems.get(0).getRevision();
            } else {
                revision = "UNKNOWN";
            }
            properties.setProperty(CREATE_MANIFEST_MOJO_BUILD_REVISION, revision);
        }
        return revision;
    }
}
