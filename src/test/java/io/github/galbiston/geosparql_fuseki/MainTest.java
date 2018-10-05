/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.galbiston.geosparql_fuseki;

import com.beust.jcommander.JCommander;
import io.github.galbiston.geosparql_fuseki.cli.ArgsConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gerg
 */
public class MainTest {

    private static GeosparqlServer SERVER;

    public MainTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        String[] args = {"-rf", "geosparql_test.rdf|xml"};

        ArgsConfig argsConfig = new ArgsConfig();
        JCommander.newBuilder()
                .addObject(argsConfig)
                .build()
                .parse(args);

        //Setup dataset
        Dataset dataset = DatasetOperations.setup(argsConfig);

        //Configure server
        SERVER = new GeosparqlServer(argsConfig.getPort(), argsConfig.getDatsetName(), argsConfig.isLoopbackOnly(), dataset, argsConfig.isUpdateAllowed());
        SERVER.start();

        System.out.println("Server: " + SERVER.getLocalServiceURL());
    }

    @AfterClass
    public static void tearDownClass() {
        SERVER.shutdown();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testMain() {
        System.out.println("main");

        String query = "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n"
                + "\n"
                + "SELECT ?obj\n"
                + "WHERE{\n"
                + "    <http://example.org/Geometry#PolygonH> geo:sfContains ?obj .\n"
                + "}ORDER by ?obj";
        List<Resource> result = new ArrayList<>();
        try (QueryExecution qe = QueryExecutionFactory.sparqlService(SERVER.getLocalServiceURL(), query)) {
            ResultSet rs = qe.execSelect();

            while (rs.hasNext()) {
                QuerySolution qs = rs.nextSolution();
                Resource obj = qs.getResource("obj");
                result.add(obj);
            }

            //ResultSetFormatter.outputAsTSV(rs);
        }

        List<Resource> expResult = new ArrayList<>();
        expResult.add(ResourceFactory.createResource("http://example.org/Feature#A"));
        expResult.add(ResourceFactory.createResource("http://example.org/Feature#D"));
        expResult.add(ResourceFactory.createResource("http://example.org/Feature#H"));
        expResult.add(ResourceFactory.createResource("http://example.org/Feature#K"));
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#LineStringD"));
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#PointA"));
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#PolygonH"));
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#PolygonK"));


        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
