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