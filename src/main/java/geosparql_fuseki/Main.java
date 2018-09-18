package geosparql_fuseki;

import com.beust.jcommander.JCommander;
import geosparql_fuseki.cli.ArgsConfig;
import geosparql_jena.implementation.GeoSPARQLSupport;
import java.io.File;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.tdb.TDBFactory;

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
        Dataset dataset;
        File tdbFolder = argsConfig.getTdbFile();
        if (tdbFolder != null) {
            dataset = TDBFactory.createDataset(tdbFolder.getAbsolutePath());
        } else {
            dataset = DatasetFactory.create();
        }

        //Apply GeoSPARQL schema and RDFS inferencing to the dataset.
        //TODO - code in benchmarking. Write the inferences to the dataset. Use the updated schema. Warn that future updates will not cause new inferences.
        //Setup GeoSPARQL
        if (argsConfig.isIndexEnabled()) {
            GeoSPARQLSupport.setupMemoryIndex(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.BYTES, Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE, argsConfig.isQueryRewrite());
        } else {
            GeoSPARQLSupport.setupNoIndex(argsConfig.isQueryRewrite());
        }
        GeosparqlServer server = new GeosparqlServer(argsConfig.getPort(), argsConfig.isLoopback(), argsConfig.getDatsetName(), dataset, argsConfig.isUpdateAllowed());
        server.start();
    }
}
