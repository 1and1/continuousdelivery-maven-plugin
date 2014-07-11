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

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Developer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.apache.maven.shared.utils.StringUtils.join;

/**
 * Created by mirko on 11.07.14.
 */
class DependencyDecorator extends Dependency {

    private final Dependency decorated;

    DependencyDecorator(Dependency decorated) {
        this.decorated = decorated;
    }

    static String fromDependencies(List<Dependency> dependencies) {
        final ArrayList<DependencyDecorator> developerDecorators = new ArrayList<DependencyDecorator>();
        for (Dependency dependency : dependencies) {
            developerDecorators.add(new DependencyDecorator(dependency));
        }
        return join(developerDecorators.iterator(), ",");
    }

    @Override
    public String toString() {
        if (decorated.getClassifier() == null) {
            return String.format(Locale.ENGLISH, "%s-%s", decorated.getArtifactId(), decorated.getVersion());
        } else {
            return String.format(Locale.ENGLISH, "%s-%s-%s", decorated.getArtifactId(), decorated.getClassifier(), decorated.getVersion());
        }

    }
}
