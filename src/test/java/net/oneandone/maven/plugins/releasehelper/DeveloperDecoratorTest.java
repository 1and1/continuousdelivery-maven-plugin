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

import org.apache.maven.model.Developer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DeveloperDecoratorTest {

    @Test
    public void testToString() throws Exception {
        final Developer developer = createDeveloper();
        assertEquals("Foo Bar <foo.bar@example.org>", new DeveloperDecorator(developer).toString());
    }

    @Test
    public void testFromDevelopers() throws Exception {
        final List<Developer> developers = Arrays.asList(createDeveloper(), createDeveloper());
        final String sut = DeveloperDecorator.fromDevelopers(developers);
        assertEquals("Foo Bar <foo.bar@example.org>, Foo Bar <foo.bar@example.org>", sut);
    }

    private Developer createDeveloper() {
        final Developer developer = new Developer();
        developer.setName("Foo Bar");
        developer.setEmail("foo.bar@example.org");
        return developer;
    }
}