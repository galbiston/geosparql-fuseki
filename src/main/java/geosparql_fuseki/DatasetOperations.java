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

import geosparql_jena.configuration.GeoSPARQLConfig;
import geosparql_jena.configuration.GeoSPARQLOperations;
import java.io.File;
import java.lang.invoke.MethodHandles;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rdf_tables.cli.DelimiterValidator;
import rdf_tables.file.FileReader;

/**
 *
 *
 */
public class DatasetOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Dataset setup(ArgsConfig argsConfig) {
        //Load from TDB folder or use in-memory dataset.
        Dataset dataset = prepareDataset(argsConfig);

        //Load data into dataset.
        loadData(argsConfig, dataset);

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
            GeoSPARQLConfig.setupMemoryIndex(argsConfig.getGeometryIndexSize(), argsConfig.getTransformIndexSize(), argsConfig.getRewriteIndexSize(), argsConfig.getGeometryIndexExpiry(), argsConfig.getTransformIndexExpiry(), argsConfig.getRewriteIndexExpiry(), argsConfig.isQueryRewrite());
        } else {
            GeoSPARQLConfig.setupNoIndex(argsConfig.isQueryRewrite());
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

    public static void loadData(ArgsConfig argsConfig, Dataset dataset) {

        if (argsConfig.getRdfFile() != null) {

            try {

                File rdfFile = argsConfig.getRdfFile();
                RDFFormat rdfFormat = argsConfig.getRdfFormat();
                LOGGER.info("Reading RDF - Started - File: {}, RDF Format: {}", rdfFile, rdfFormat);
                //Default Model
                dataset.begin(ReadWrite.WRITE);
                Model defaultModel = dataset.getDefaultModel();
                Model model = RDFDataMgr.loadModel(rdfFile.getAbsolutePath(), rdfFormat.getLang());
                defaultModel.add(model);
                dataset.commit();
                LOGGER.info("Reading RDF - Completed - File: {}, RDF Format: {}", rdfFile, rdfFormat);
            } catch (Exception ex) {
                LOGGER.error("Write Error: {}", ex.getMessage());
                dataset.abort();
            } finally {
                dataset.end();
            }

        }

        if (argsConfig.getTabularFile() != null) {
            try {
                File tabFile = argsConfig.getTabularFile();
                String delimiter = argsConfig.getTabularDelimiter();
                LOGGER.info("Reading Tabular - Started - File: {}, Delimiter: {}", tabFile, delimiter);
                //Default Model
                dataset.begin(ReadWrite.WRITE);
                Model defaultModel = dataset.getDefaultModel();
                Model model = FileReader.convertCSVFile(tabFile, DelimiterValidator.getDelimiterCharacter(delimiter));
                defaultModel.add(model);
                dataset.commit();
                LOGGER.info("Reading Tabular - Completed - File: {}, Delimiter: {}", tabFile, delimiter);
            } catch (Exception ex) {
                LOGGER.error("Write Error: {}", ex.getMessage());
            } finally {
                dataset.end();
            }
        }

    }

}
