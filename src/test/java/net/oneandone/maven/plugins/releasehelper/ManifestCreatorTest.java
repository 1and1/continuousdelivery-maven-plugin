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

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Properties;

import static org.junit.Assert.*;

public class ManifestCreatorTest {

    final MavenSession mockedSession;

    final MavenProject mockedProject;

    public ManifestCreatorTest() {
        mockedSession = Mockito.mock(MavenSession.class);
        mockedProject = new MavenProject();
    }

    @Test
    public void testAddManifestEntry() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("maven.version", "3.2.3");
        Mockito.when(mockedSession.getExecutionProperties()).thenReturn(properties);
        final CreateManifestMojo.ManifestCreator sut = new CreateManifestMojo.ManifestCreator(mockedSession, mockedProject);
        sut.addManifestEntry("Foo", "bar");
        assertTrue(sut.getManifest().toString().contains("Foo: bar"));
    }

    @Test
    public void testGetManifest() throws Exception {

    }
}