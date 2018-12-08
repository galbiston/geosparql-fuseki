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
package io.github.galbiston.geosparql_fuseki.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 */
public class ArgsConfig {

    //1) Port
    @Parameter(names = {"--port", "-p"}, description = "Port number.", order = 0)
    private int port = 3030;

    //2) Dataset name
    @Parameter(names = {"--dataset", "-d"}, description = "Dataset name.", order = 1)
    private String datsetName = "ds";

    //3) Loopback only
    @Parameter(names = {"--loopback", "-l"}, description = "Local host loopback requests only.", arity = 1, order = 2)
    private boolean loopbackOnly = true;

    //4) SPARQL update allowed
    @Parameter(names = {"--update", "-u"}, description = "SPARQL update allowed.", order = 3)
    private boolean updateAllowed = false;

    //5) TDB folder
    @Parameter(names = {"--tdb", "-t"}, description = "TDB folder of dataset. Default set to memory dataset.", converter = FileConverter.class, order = 4)
    private File tdbFile = null;

    //6) GeoSPARQL RDFS inference
    @Parameter(names = {"--inference", "-i"}, description = "Enable GeoSPARQL RDFS schema and inferencing (class and property hierarchy). Inferences will be applied to the dataset. Updates to dataset may require server restart.", arity = 1, order = 5)
    private boolean inference = false;

    //7) Apply default geometry to single Feature-Geometry
    @Parameter(names = {"--default_geometry", "-dg"}, description = "Apply hasDefaultGeometry to single Feature hasGeometry Geometry statements. Additional properties will be added to the dataset.", arity = 1, order = 6)
    private boolean applyDefaultGeometry = false;

    //7) Validate geometry literals in the data
    @Parameter(names = {"--validate", "-v"}, description = "Validate that the Geometry Literals in the dataset are valid.", arity = 1, order = 6)
    private boolean validateGeometryLiteral = false;

    //9) Load RDF file into dataset
    @Parameter(names = {"--rdf_file", "-rf"}, description = "Comma separated list of [RDF file path#graph name|RDF format] to load into dataset. Graph name is optional and will use default graph. RDF format is optional (default: ttl) or select from one of the following: json-ld, json-rdf, nt, nq, thrift, trig, trix, ttl, ttl-pretty, xml, xml-plain, xml-pretty.", validateWith = RDFFileParameter.class, listConverter = RDFFileParameter.class, order = 8)
    private List<FileGraphFormat> fileGraphFormats = new ArrayList<>();

    //10) Load tabular file into dataset
    @Parameter(names = {"--tabular_file", "-tf"}, description = "Comma separated list of [Tabular file path#graph name|delimiter] to load into dataset. See RDF Tables for table formatting. Graph name is optional and will use default graph. Column delimiter is optional and will default to COMMA. Any character except ':', '^' and '|'. Keywords TAB, SPACE and COMMA are also supported.", validateWith = TabFileParameter.class, listConverter = TabFileParameter.class, order = 9)
    private List<FileGraphDelimiter> fileGraphDelimiters = new ArrayList<>();

    //11) Query Rewrite enabled
    @Parameter(names = {"--rewrite", "-r"}, description = "Enable query rewrite.", arity = 1, order = 10)
    private boolean queryRewrite = true;

    //12) Indexing enabled
    @Parameter(names = {"--index", "-x"}, description = "Indexing enabled.", arity = 1, order = 11)
    private boolean indexEnabled = true;

    //13) Index sizes
    @Parameter(names = {"--index_sizes", "-xs"}, description = "List of Index item sizes: [Geometry Literal, Geometry Transform, Query Rewrite]. Unlimited: -1, Off: 0", listConverter = IntegerListConverter.class, order = 12)
    private List<Integer> indexSizes = Arrays.asList(-1, -1, -1);

    //14) Index sizes
    @Parameter(names = {"--index_expiry", "-xe"}, description = "List of Index item expiry in milliseconds: [Geometry Literal, Geometry Transform, Query Rewrite]. Off: 0, Minimum: 1001", listConverter = LongListConverter.class, order = 13)
    private List<Long> indexExpiries = Arrays.asList(5000l, 5000l, 5000l);

    //15) Help
    @Parameter(names = {"--help", "-h"}, description = "Application help. @path/to/file can be used to submit parameters in a file.", help = true, order = 14)
    private boolean help = false;

    public int getPort() {
        return port;
    }

    public String getDatsetName() {
        return datsetName;
    }

    public File getTdbFile() {
        return tdbFile;
    }

    public boolean isLoopbackOnly() {
        return loopbackOnly;
    }

    public boolean isInference() {
        return inference;
    }

    public boolean isQueryRewrite() {
        return queryRewrite;
    }

    public boolean isUpdateAllowed() {
        return updateAllowed;
    }

    public boolean isIndexEnabled() {
        return indexEnabled;
    }

    public List<Integer> getIndexSizes() {
        return indexSizes;
    }

    public List<Long> getIndexExpiries() {
        return indexExpiries;
    }

    public List<FileGraphFormat> getFileGraphFormats() {
        return fileGraphFormats;
    }

    public void setFileGraphFormats(List<FileGraphFormat> fileGraphFormats) {
        this.fileGraphFormats = fileGraphFormats;
    }

    public List<FileGraphDelimiter> getFileGraphDelimiters() {
        return fileGraphDelimiters;
    }

    public void setFileGraphDelimiters(List<FileGraphDelimiter> fileGraphDelimiters) {
        this.fileGraphDelimiters = fileGraphDelimiters;
    }

    public boolean isApplyDefaultGeometry() {
        return applyDefaultGeometry;
    }

    public void setApplyDefaultGeometry(boolean applyDefaultGeometry) {
        this.applyDefaultGeometry = applyDefaultGeometry;
    }

    public boolean isValidateGeometryLiteral() {
        return validateGeometryLiteral;
    }

    public boolean isHelp() {
        return help;
    }

    public String getSummary() {
        return "port=" + port + ", datsetName=" + datsetName + ", loopbackOnly=" + loopbackOnly + ", updateAllowed=" + updateAllowed + ", tdbFile=" + tdbFile + ", inference=" + inference + ", applyDefaultGeometry=" + applyDefaultGeometry + ", validateGeometryLiteral=" + validateGeometryLiteral + ", fileGraphFormats=" + fileGraphFormats + ", fileGraphDelimiters=" + fileGraphDelimiters + ", queryRewrite=" + queryRewrite + ", indexEnabled=" + indexEnabled + ", indexSizes=" + indexSizes + ", indexExpiries=" + indexExpiries + ", help=" + help;
    }

    @Override
    public String toString() {
        return "ArgsConfig{" + "port=" + port + ", datsetName=" + datsetName + ", loopbackOnly=" + loopbackOnly + ", updateAllowed=" + updateAllowed + ", tdbFile=" + tdbFile + ", inference=" + inference + ", applyDefaultGeometry=" + applyDefaultGeometry + ", validateGeometryLiteral=" + validateGeometryLiteral + ", fileGraphFormats=" + fileGraphFormats + ", fileGraphDelimiters=" + fileGraphDelimiters + ", queryRewrite=" + queryRewrite + ", indexEnabled=" + indexEnabled + ", indexSizes=" + indexSizes + ", indexExpiries=" + indexExpiries + ", help=" + help + '}';
    }

}
