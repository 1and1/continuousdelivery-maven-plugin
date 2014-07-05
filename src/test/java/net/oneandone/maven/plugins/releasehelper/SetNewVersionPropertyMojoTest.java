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
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;

/**
 * @author Mirko Friedenhagen
 */
public class SetNewVersionPropertyMojoTest {

    MavenSession mockedSession = mock(MavenSession.class);
    File mockedBasedir = mock(File.class);
    private final MavenProject mavenProject = new MavenProject();

    @Before
    public void setupMocks() {
        when(mockedSession.getExecutionRootDirectory()).thenReturn(".");
    }

    /**
     * Test of execute method, of class SetNewVersionPropertyMojo.
     */
    @Test
    public void testExecute() throws MojoExecutionException {
        when(mockedBasedir.toString()).thenReturn(".");
        mavenProject.setVersion("1.0-SNAPSHOT");
        SetNewVersionPropertyMojo sut = new SetNewVersionPropertyMojo(mavenProject, mockedSession, mockedBasedir) {
            @Override
            String getBuildNumber() {
                return "123";
            }
        };
        sut.execute();
        assertEquals("1.0.123", mavenProject.getProperties().getProperty("newVersion"));
    }

    /**
     * Test of execute method, of class SetNewVersionPropertyMojo.
     */
    @Test
    public void testExecuteNotInRootDir() throws MojoExecutionException {
        when(mockedBasedir.toString()).thenReturn("target");
        mavenProject.setVersion("1.0-SNAPSHOT");
        SetNewVersionPropertyMojo sut = new SetNewVersionPropertyMojo(mavenProject, mockedSession, mockedBasedir) {
            @Override
            String getBuildNumber() {
                return "123";
            }
        };
        sut.execute();
        assertNull("Not in root dir, should be null", mavenProject.getProperties().getProperty("newVersion"));
    }
    /**
     * Test of execute method, of class SetNewVersionPropertyMojo.
     */
    @Test(expected = MojoExecutionException.class)
    public void testExecuteNotASnapshot() throws MojoExecutionException {
        when(mockedBasedir.toString()).thenReturn(".");
        mavenProject.setVersion("1.0");
        SetNewVersionPropertyMojo sut = new SetNewVersionPropertyMojo(mavenProject, mockedSession, mockedBasedir);
        sut.execute();
    }

    /**
     * Test of execute method, of class SetNewVersionPropertyMojo.
     */
    @Test(expected = MojoExecutionException.class)
    public void testExecuteNoBuildNumber() throws MojoExecutionException {
        when(mockedBasedir.toString()).thenReturn(".");
        mavenProject.setVersion("1.0-SNAPSHOT");
        // need to set this explicitely, otherwise this will always fail in a Jenkins build.
        SetNewVersionPropertyMojo sut = new SetNewVersionPropertyMojo(mavenProject, mockedSession, mockedBasedir) {
            @Override
            String getBuildNumber() {
                return null;
            }
        };
        sut.execute();
    }

    @Test
    public void testGetBuildNumberAndParameterLessConstructor() {
        new SetNewVersionPropertyMojo();
        SetNewVersionPropertyMojo sut = new SetNewVersionPropertyMojo(mavenProject, mockedSession, mockedBasedir);
        sut.getBuildNumber();
    }
}
