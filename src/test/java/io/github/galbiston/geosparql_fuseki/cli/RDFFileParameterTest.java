/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.galbiston.geosparql_fuseki.cli;

import com.beust.jcommander.ParameterException;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.jena.riot.RDFFormat;
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
public class RDFFileParameterTest {

    public RDFFileParameterTest() {
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
     * Test of convert method, of class RDFFileParameter.
     */
    @Test
    public void testConvert() {
        System.out.println("convert");
        String value = "test.rdf#test>xml,test2.rdf";
        RDFFileParameter instance = new RDFFileParameter();
        List<FileGraphFormat> expResult = Arrays.asList(new FileGraphFormat(new File("test.rdf"), "test", RDFFormat.RDFXML), new FileGraphFormat(new File("test2.rdf"), "", RDFFormat.TTL));
        List<FileGraphFormat> result = instance.convert(value);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of build method, of class RDFFileParameter.
     */
    @Test
    public void testBuild() {
        System.out.println("build");
        String value = "test.rdf>xml";
        RDFFileParameter instance = new RDFFileParameter();
        FileGraphFormat expResult = new FileGraphFormat(new File("test.rdf"), "", RDFFormat.RDFXML);
        FileGraphFormat result = instance.build(value);

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of validate method, of class RDFFileParameter.
     */
    @Test(expected = ParameterException.class)
    public void testValidate() {
        System.out.println("validate");
        String name = "--rdf_file";
        String value = "test.rdf>xml#test";
        RDFFileParameter instance = new RDFFileParameter();
        instance.validate(name, value);
    }

}
