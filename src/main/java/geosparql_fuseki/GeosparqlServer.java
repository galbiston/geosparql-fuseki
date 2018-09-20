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

import java.lang.invoke.MethodHandles;
import org.apache.jena.fuseki.embedded.FusekiServer;
import org.apache.jena.fuseki.embedded.FusekiServer.Builder;
import org.apache.jena.query.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class GeosparqlServer extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final int port;
    private final String datasetName;
    private final String localServiceURL;
    private final boolean loopback;
    private final boolean allowUpdate;
    private final FusekiServer server;
    private Thread shutdownThread = null;


    public GeosparqlServer(int port, boolean loopback, String datasetName, Dataset dataset, boolean allowUpdate) {

        this.port = port;
        this.loopback = loopback;
        this.datasetName = checkDatasetName(datasetName);
        this.allowUpdate = allowUpdate;
        Builder builder = FusekiServer.create()
                .setPort(port)
                .setLoopback(loopback);
        builder.add(datasetName, dataset, allowUpdate);
        this.server = builder.build();
        this.localServiceURL = "http://localhost:" + port + "/" + datasetName;
    }

    private String checkDatasetName(String dataset) {

        if (dataset.isEmpty()) {
            LOGGER.warn("Empty dataset name. Defaulting to '/ds'.");
            return "/ds";
        }

        if (datasetName.charAt(0) == '/') {
            return datasetName;
        } else {
            return "/" + datasetName;
        }
    }

    @Override
    public void run() {
        LOGGER.info("Server Running - Port: {}, Loopback: {}, Dataset: {}", port, loopback, datasetName, allowUpdate);
        addShutdownHook();
        this.server.start();
    }

    private void addShutdownHook() {
        removeShutdownHook();

        Thread thread = new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        };
        Runtime.getRuntime().addShutdownHook(thread);
        shutdownThread = thread;
    }

    private void removeShutdownHook() {
        if (shutdownThread != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownThread);
            shutdownThread = null;
        }
    }

    public void shutdown() {
        server.stop();
        removeShutdownHook();
        LOGGER.info("GeoSPARQL Server Thread: Shutdown");
    }

    public int getPort() {
        return port;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String getLocalServiceURL() {
        return localServiceURL;
    }
    public boolean isLoopback() {
        return loopback;
    }

    public boolean isAllowUpdate() {
        return allowUpdate;
    }

    @Override
    public String toString() {
        return "GeosparqlServer{" + "port=" + port + ", datasetName=" + datasetName + ", localServiceURL=" + localServiceURL + ", loopback=" + loopback + ", allowUpdate=" + allowUpdate + '}';
    }

}
