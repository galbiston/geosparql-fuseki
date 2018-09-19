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
import geosparql_jena.implementation.GeoSPARQLSupport;
import org.apache.jena.query.Dataset;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArgsConfig argsConfig = new ArgsConfig();
        JCommander.newBuilder()
                .addObject(argsConfig)
                .build()
                .parse(args);

        //Load from TDB folder or use in-memory dataset.
        Dataset dataset = DatasetOperations.prepare(argsConfig);

        //Load data into dataset.
        DatasetOperations.loadData(argsConfig, dataset);

        //Apply hasDefaultGeometry relations to single Feature hasGeometry Geometry.
        DatasetOperations.applyDefaultGeometry(argsConfig, dataset);

        //Apply GeoSPARQL schema and RDFS inferencing to the dataset.
        DatasetOperations.applyInferencing(argsConfig, dataset);

        //Setup GeoSPARQL
        setupGeoSPARQL(argsConfig);

        //Configure server
        GeosparqlServer server = new GeosparqlServer(argsConfig.getPort(), argsConfig.isLoopback(), argsConfig.getDatsetName(), dataset, argsConfig.isUpdateAllowed());
        server.start();
    }

    private static void setupGeoSPARQL(ArgsConfig argsConfig) {
        if (argsConfig.isIndexEnabled()) {
            GeoSPARQLSupport.setupMemoryIndex(argsConfig.getGeometryIndexSize(), argsConfig.getTransformIndexSize(), argsConfig.getRewriteIndexSize(), argsConfig.getGeometryIndexExpiry(), argsConfig.getTransformIndexExpiry(), argsConfig.getRewriteIndexExpiry(), argsConfig.isQueryRewrite());
        } else {
            GeoSPARQLSupport.setupNoIndex(argsConfig.isQueryRewrite());
        }
    }

}
