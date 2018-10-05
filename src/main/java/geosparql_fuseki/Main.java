/**
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geosparql_fuseki;

import com.beust.jcommander.JCommander;
import org.apache.jena.query.Dataset;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArgsConfig argsConfig = new ArgsConfig();

        JCommander jCommander = JCommander.newBuilder()
                .addObject(argsConfig)
                .build();

        jCommander.setProgramName("GeoSPARQL Fuseki");
        jCommander.parse(args);
        if (argsConfig.isHelp()) {
            jCommander.usage();
            return;
        }
        //Setup dataset
        Dataset dataset = DatasetOperations.setup(argsConfig);

        //Configure server
        GeosparqlServer server = new GeosparqlServer(argsConfig.getPort(), argsConfig.getDatsetName(), argsConfig.isLoopbackOnly(), dataset, argsConfig.isUpdateAllowed());
        server.start();
    }

}
