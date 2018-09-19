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

import geosparql_jena.implementation.data_conversion.GeoSPARQLPredicates;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rdf_tables.cli.SeparatorValidator;
import rdf_tables.file.FileReader;

/**
 *
 *
 */
public class DatasetOperations {

    private static final File GEOSPARQL_SCHEMA_FILE = new File(DatasetOperations.class.getClassLoader().getResource("geosparql_vocab_all_v1_0_1_updated.rdf").getFile());

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Dataset prepare(ArgsConfig argsConfig) {

        Dataset dataset;
        File tdbFolder = argsConfig.getTdbFile();
        if (tdbFolder != null) {
            dataset = TDBFactory.createDataset(tdbFolder.getAbsolutePath());
        } else {
            dataset = DatasetFactory.create();
        }

        return dataset;
    }

    public static void loadData(ArgsConfig argsConfig, Dataset dataset) {

        if (argsConfig.getRdfFile() != null) {

            try {
                //Default Model
                dataset.begin(ReadWrite.WRITE);
                Model defaultModel = dataset.getDefaultModel();
                Model model = RDFDataMgr.loadModel(argsConfig.getRdfFile().getAbsolutePath(), argsConfig.getRdfFormat().getLang());
                defaultModel.add(model);
                dataset.commit();

            } catch (Exception ex) {
                LOGGER.error("Write Error: {}", ex.getMessage());
            } finally {
                dataset.end();
            }

        }

        if (argsConfig.getTabularFile() != null) {
            try {
                //Default Model
                dataset.begin(ReadWrite.WRITE);
                Model defaultModel = dataset.getDefaultModel();
                Model model = FileReader.convertCSVFile(argsConfig.getTabularFile(), SeparatorValidator.getSeparatorCharacter(argsConfig.getTabularSeparator()));
                defaultModel.add(model);
                dataset.commit();

            } catch (Exception ex) {
                LOGGER.error("Write Error: {}", ex.getMessage());
            } finally {
                dataset.end();
            }
        }

    }

    public static void applyDefaultGeometry(ArgsConfig argsConfig, Dataset dataset) {
        if (argsConfig.isApplyDefaultGeometry()) {

            try {
                //Default Model
                dataset.begin(ReadWrite.WRITE);

                //Default Model
                dataset.begin(ReadWrite.WRITE);
                Model defaultModel = dataset.getDefaultModel();
                GeoSPARQLPredicates.applyDefaultGeometry(defaultModel);

                //Named Models
                Iterator<String> graphNames = dataset.listNames();
                while (graphNames.hasNext()) {
                    String graphName = graphNames.next();
                    Model namedModel = dataset.getNamedModel(graphName);
                    GeoSPARQLPredicates.applyDefaultGeometry(namedModel);
                }

                dataset.commit();

            } catch (Exception ex) {
                LOGGER.error("Write Error: {}", ex.getMessage());
            } finally {
                dataset.end();
            }
        }
    }

    public static void applyInferencing(ArgsConfig argsConfig, Dataset dataset) {

        if (argsConfig.isInference()) {
            Model geosparqlSchema = RDFDataMgr.loadModel(GEOSPARQL_SCHEMA_FILE.getAbsolutePath());

            try {
                //Default Model
                dataset.begin(ReadWrite.WRITE);
                Model defaultModel = dataset.getDefaultModel();
                applySchema(geosparqlSchema, defaultModel);

                //Named Models
                Iterator<String> graphNames = dataset.listNames();
                while (graphNames.hasNext()) {
                    String graphName = graphNames.next();
                    Model namedModel = dataset.getNamedModel(graphName);
                    applySchema(geosparqlSchema, namedModel);
                }

                dataset.commit();

            } catch (Exception ex) {
                LOGGER.error("Inferencing Error: {}", ex.getMessage());
            } finally {
                dataset.end();
            }
        }
    }

    public static void applySchema(Model model, Model geosparqlSchema) {
        if (!model.isEmpty()) {
            InfModel infModel = ModelFactory.createRDFSModel(geosparqlSchema, model);
            model.add(infModel.getDeductionsModel());
        }
    }

}
