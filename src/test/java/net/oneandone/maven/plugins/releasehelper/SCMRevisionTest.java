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

import org.apache.maven.scm.manager.plexus.DefaultScmManager;
import org.apache.maven.scm.provider.git.gitexe.GitExeScmProvider;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SCMRevisionTest {

    private SCMRevision revision;
    private Properties properties;

    @Before
    public void setUp() {
        final DefaultScmManager scmManager = new DefaultScmManager();
        scmManager.setScmProvider("git", new GitExeScmProvider());
        scmManager.enableLogging(new ConsoleLogger());
        final File baseDir = new File(".");
        final String connection = "scm:git:git://github.com/1and1/releasehelper-maven-plugin.git";
        final String developerConnection = "scm:git:ssh://git@github.com/1and1/releasehelper-maven-plugin.git";
        properties = new Properties();
        revision = new SCMRevision(connection, developerConnection, scmManager, baseDir, properties);
    }

    @Test
    public void testGetSCMRevision() throws Exception {
        revision.getSCMRevision();
        assertTrue(properties.containsKey(SCMRevision.CREATE_MANIFEST_MOJO_BUILD_REVISION));
    }

    @Test
    public void testGetSCMRevisionFromProperty() throws Exception {
        properties.setProperty(SCMRevision.CREATE_MANIFEST_MOJO_BUILD_REVISION, "foobar");
        revision.getSCMRevision();
        assertEquals("foobar", properties.getProperty(SCMRevision.CREATE_MANIFEST_MOJO_BUILD_REVISION));
    }

    @Test
    public void testGetRevision() {
        assertEquals("UNKNOWN", revision.getRevision(Collections.EMPTY_LIST));
    }
}