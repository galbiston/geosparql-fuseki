/*
 * Copyright 2019 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.galbiston.geosparql_fuseki;

import com.beust.jcommander.JCommander;
import io.github.galbiston.geosparql_fuseki.cli.ArgsConfig;
import io.github.galbiston.geosparql_jena.spatial.SpatialIndexException;
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
 *
 */
public class MainTest {

    private static GeosparqlServer SERVER;

    public MainTest() {
    }

    @BeforeClass
    public static void setUpClass() throws DatasetException, SpatialIndexException {
        String[] args = {"-rf", "geosparql_test.rdf>xml", "-i"};

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
