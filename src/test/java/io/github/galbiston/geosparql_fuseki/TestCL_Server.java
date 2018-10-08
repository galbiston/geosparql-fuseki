/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.galbiston.geosparql_fuseki;

import java.util.ArrayList;
import java.util.List;
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
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 *
 */
public class TestCL_Server {

    public TestCL_Server() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test when running a command line server.<br>
     * .\geosparql-fuseki.bat -rf "test.rdf&xml"
     */
    @Test
    @Ignore
    public void testMain() {
        System.out.println("Test RDF");

        String service = "http://localhost:3030/ds";
        String query = "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n"
                + "\n"
                + "SELECT ?obj\n"
                + "WHERE{\n"
                + "    <http://example.org/Geometry#PolygonH> geo:sfContains ?obj .\n"
                + "}ORDER by ?obj";

        List<Resource> result = new ArrayList<>();
        try (QueryExecution qe = QueryExecutionFactory.sparqlService(service, query)) {
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
        expResult.add(ResourceFactory.createResource("http://example.org/Feature#H"));
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#PointA"));
        expResult.add(ResourceFactory.createResource("http://example.org/Geometry#PolygonH"));

        System.out.println("Exp: " + expResult);
        System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
