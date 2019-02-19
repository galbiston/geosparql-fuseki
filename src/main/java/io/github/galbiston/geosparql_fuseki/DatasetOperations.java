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
package io.github.galbiston.geosparql_fuseki;

import io.github.galbiston.geosparql_fuseki.cli.ArgsConfig;
import io.github.galbiston.geosparql_fuseki.cli.FileGraphDelimiter;
import io.github.galbiston.geosparql_fuseki.cli.FileGraphFormat;
import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLOperations;
import io.github.galbiston.geosparql_jena.implementation.datatype.GMLDatatype;
import io.github.galbiston.geosparql_jena.implementation.datatype.GeometryDatatype;
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.rdf_tables.cli.DelimiterValidator;
import io.github.galbiston.rdf_tables.datatypes.DatatypeController;
import io.github.galbiston.rdf_tables.file.FileReader;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class DatasetOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String SPATIAL_INDEX_FILE = "spatial.index";

    public static Dataset setup(ArgsConfig argsConfig) {
        //Report the summary of the ArgsConfig.
        LOGGER.info("Server Configuration: {}", argsConfig.getSummary());

        //Register GeometryLiteral datatypes for CSV conversion.
        registerDatatypes();

        //Load from TDB folder or use in-memory dataset.
        Dataset dataset = prepareDataset(argsConfig);

        //Load data into dataset.
        loadData(argsConfig, dataset);

        //Convert Geo predicates to Geometry Literals.
        if (argsConfig.isConvertGeoPredicates()) //Apply validation of Geometry Literal.
        {
            GeoSPARQLOperations.convertGeoPredicates(dataset, argsConfig.isRemoveGeoPredicates());
        }

        //Apply hasDefaultGeometry relations to single Feature hasGeometry Geometry.
        if (argsConfig.isApplyDefaultGeometry()) {
            GeoSPARQLOperations.applyDefaultGeometry(dataset);
        }

        //Apply GeoSPARQL schema and RDFS inferencing to the dataset.
        if (argsConfig.isInference()) {
            GeoSPARQLOperations.applyInferencing(dataset);
        }

        //Setup GeoSPARQL
        if (argsConfig.isIndexEnabled()) {
            List<Integer> indexSizes = argsConfig.getIndexSizes();
            List<Long> indexExpiries = argsConfig.getIndexExpiries();
            GeoSPARQLConfig.setupMemoryIndex(indexSizes.get(0), indexSizes.get(1), indexSizes.get(2), indexExpiries.get(0), indexExpiries.get(1), indexExpiries.get(2), argsConfig.isQueryRewrite());
        } else {
            GeoSPARQLConfig.setupNoIndex(argsConfig.isQueryRewrite());
        }

        //Setup Spatial Extension
        if (argsConfig.getSpatialIndexFile() != null) {
            File spatialIndexFile = argsConfig.getSpatialIndexFile();
            GeoSPARQLConfig.setupSpatialIndex(dataset, spatialIndexFile);
        } else if (argsConfig.isTDBFileSetup()) {
            File spatialIndexFile = new File(argsConfig.getTdbFile(), SPATIAL_INDEX_FILE);
            GeoSPARQLConfig.setupSpatialIndex(dataset, spatialIndexFile);
        } else {
            GeoSPARQLConfig.setupSpatialIndex(dataset);
        }

        return dataset;
    }

    public static Dataset prepareDataset(ArgsConfig argsConfig) {

        Dataset dataset;
        File tdbFolder = argsConfig.getTdbFile();
        if (tdbFolder != null) {
            LOGGER.info("TDB Dataset: {}", tdbFolder);
            dataset = TDBFactory.createDataset(tdbFolder.getAbsolutePath());
        } else {
            LOGGER.info("In-Memory Dataset");
            dataset = DatasetFactory.create();
        }

        return dataset;
    }

    public static final void registerDatatypes() {
        DatatypeController.addPrefixDatatype("wkt", WKTDatatype.INSTANCE);
        DatatypeController.addPrefixDatatype("gml", GMLDatatype.INSTANCE);
        GeometryDatatype.registerDatatypes();
    }

    public static void loadData(ArgsConfig argsConfig, Dataset dataset) {

        if (!argsConfig.getFileGraphFormats().isEmpty()) {

            try {

                List<FileGraphFormat> fileGraphFormats = argsConfig.getFileGraphFormats();

                for (FileGraphFormat fileGraphFormat : fileGraphFormats) {
                    File rdfFile = fileGraphFormat.getRdfFile();
                    String graphName = fileGraphFormat.getGraphName();
                    RDFFormat rdfFormat = fileGraphFormat.getRdfFormat();

                    LOGGER.info("Reading RDF - Started - File: {}, Graph Name: {}, RDF Format: {}", rdfFile, graphName, rdfFormat);
                    dataset.begin(ReadWrite.WRITE);

                    //Obtain the target model
                    Model targetModel;
                    if (graphName.isEmpty()) {
                        targetModel = dataset.getDefaultModel();
                    } else {
                        if (dataset.containsNamedModel(graphName)) {
                            targetModel = dataset.getNamedModel(graphName);
                        } else {
                            targetModel = ModelFactory.createDefaultModel();
                            dataset.addNamedModel(graphName, targetModel);
                        }
                    }

                    //Load file and add to target model.
                    Model model = RDFDataMgr.loadModel(rdfFile.getAbsolutePath(), rdfFormat.getLang());
                    targetModel.add(model);
                    dataset.commit();
                    LOGGER.info("Reading RDF - Completed - File: {}, Graph Name: {}, RDF Format: {}", rdfFile, graphName, rdfFormat);
                }
            } catch (Exception ex) {
                LOGGER.error("Write Error: {}", ex.getMessage());
                dataset.abort();
            } finally {
                dataset.end();
            }

        }

        if (!argsConfig.getFileGraphDelimiters().isEmpty()) {
            try {
                List<FileGraphDelimiter> fileGraphDelimiters = argsConfig.getFileGraphDelimiters();

                for (FileGraphDelimiter fileGraphDelimiter : fileGraphDelimiters) {
                    File tabFile = fileGraphDelimiter.getTabFile();
                    String graphName = fileGraphDelimiter.getGraphName();
                    String delimiter = fileGraphDelimiter.getDelimiter();
                    LOGGER.info("Reading Tabular - Started - File: {}, Graph: {}, Delimiter: {}", tabFile, graphName, delimiter);

                    dataset.begin(ReadWrite.WRITE);

                    //Obtain the target model
                    Model targetModel;
                    if (graphName.isEmpty()) {
                        targetModel = dataset.getDefaultModel();
                    } else {
                        if (dataset.containsNamedModel(graphName)) {
                            targetModel = dataset.getNamedModel(graphName);
                        } else {
                            targetModel = ModelFactory.createDefaultModel();
                            dataset.addNamedModel(graphName, targetModel);
                        }
                    }

                    //Load file and add to target model.
                    Model model = FileReader.convertFile(tabFile, DelimiterValidator.getDelimiterCharacter(delimiter));
                    targetModel.add(model);
                    dataset.commit();
                    LOGGER.info("Reading Tabular - Completed - File: {}, Graph: {},  Delimiter: {}", tabFile, graphName, delimiter);
                }
            } catch (Exception ex) {
                LOGGER.error("Write Error: {}", ex.getMessage());
            } finally {
                dataset.end();
            }
        }

    }

}
