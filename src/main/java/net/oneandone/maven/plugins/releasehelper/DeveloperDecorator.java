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

import org.apache.maven.model.Developer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.apache.maven.shared.utils.StringUtils.join;

/**
* Created by mirko on 11.07.14.
*/
class DeveloperDecorator extends Developer {

    private final Developer decorated;

    DeveloperDecorator(Developer decorated) {
        this.decorated = decorated;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s <%s>", decorated.getName(), decorated.getEmail());
    }

    static String fromDevelopers(List<Developer> developers) {
        final ArrayList<DeveloperDecorator> developerDecorators = new ArrayList<DeveloperDecorator>();
        for (Developer developer : developers) {
            developerDecorators.add(new DeveloperDecorator(developer));
        }
        return join(developerDecorators.iterator(), ", ");
    }
}
