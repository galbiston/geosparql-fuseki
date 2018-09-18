/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    private final FusekiServer server;
    private Thread shutdownThread = null;
    private String datasetName;
    private Dataset dataset;
    private boolean allowUpdate;

    public GeosparqlServer(int port, boolean loopback, String datasetName, Dataset dataset, boolean allowUpdate) {

        this.port = port;
        Builder builder = FusekiServer.create()
                .setPort(port)
                .setLoopback(loopback);
        builder.add(datasetName, dataset, allowUpdate);

        this.server = builder.build();
    }

    @Override
    public void run() {
        this.server.start();
        addShutdownHook();
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

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public boolean isAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    @Override
    public String toString() {
        return "GeosparqlServer{" + "port=" + port + ", datasetName=" + datasetName + ", allowUpdate=" + allowUpdate + '}';
    }

}
