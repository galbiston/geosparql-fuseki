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
package geosparql_fuseki.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import java.io.File;
import org.apache.jena.riot.RDFFormat;
import rdftables.cli.FormatParameter;
import rdftables.cli.SeparatorValidator;

/**
 *
 *
 */
public class ArgsConfig {

    //0) Port
    @Parameter(names = {"--port", "-p"}, description = "Port number. Default: 3030")
    private int port = 3030;

    //1) Dataset name
    @Parameter(names = {"--dataset", "-d"}, description = "Dataset name. Default: 'default'")
    private String datsetName = "default";

    //2) TDB folder
    @Parameter(names = {"--tdb", "-t"}, description = "TDB folder of dataset. Default: memory dataset", converter = FileConverter.class)
    private File tdbFile = null;

    //3) Loopback only
    @Parameter(names = {"--loopback", "-l"}, description = "Loopback only. Default: true", arity = 1)
    private boolean loopback = true;

    //4) GeoSPARQL RDFS inference
    @Parameter(names = {"--inference", "-i"}, description = "Enable GeoSPARQL RDFS schema and inferencing. Default: true", arity = 1)
    private boolean inference = true;

    //5) Query Rewrite enabled
    @Parameter(names = {"--rewrite", "-r"}, description = "Enable query rewrite. Default: true", arity = 1)
    private boolean queryRewrite = true;

    //6) Update allowed
    @Parameter(names = {"--update", "-u"}, description = "Update allowed. Default: false")
    private boolean updateAllowed = false;

    //7) Indexing enabled
    @Parameter(names = {"--index", "-x"}, description = "Indexing enabled Default: true", arity = 1)
    private boolean indexEnabled = true;

    //8) Geometry Literal Index size
    @Parameter(names = {"--geometry_size", "-gs"}, description = "Geometry Literal index item size. Unlimited: -1, Off: 0, Default: -1")
    private int geometryIndexSize = -1;

    //9) Geometry Transform Index size
    @Parameter(names = {"--transform_size", "-ts"}, description = "Geoemtry Transform index item size. Unlimited: -1, Off: 0, Default: -1")
    private int transformIndexSize = -1;

    //10) Query Rewrite Index size
    @Parameter(names = {"--rewrite_size", "-rs"}, description = "Query Rewrite index item size. Unlimited: -1, Off: 0, Default: -1")
    private int rewriteIndexSize = -1;

    //8) Geometry Literal Expiry milliseconds
    @Parameter(names = {"--geometry_expiry", "-gx"}, description = "Geometry Literal index item expiry. Default: 5000 milliseconds")
    private int geometryIndexExpiry = 5000;

    //9) Geometry Transform Index size
    @Parameter(names = {"--transform_expiry", "-tx"}, description = "Geoemtry Transform index item expiry. Default: 5000 milliseconds")
    private int transformIndexExpiry = 5000;

    //10) Query Rewrite Index size
    @Parameter(names = {"--rewrite_expiry", "-rx"}, description = "Query Rewrite index item expiry. Default: 5000 milliseconds")
    private int rewriteIndexExpiry = 5000;

    //11) Load RDF file into dataset
    @Parameter(names = {"--rdf_file", "-rf"}, description = "RDF file to load into dataset.", converter = FileConverter.class)
    private File rdfFile = null;

    //12) RDF file format
    @Parameter(names = {"--rdf_extension", "-re"}, description = "Dataset name. Default: default", validateWith = FormatParameter.class, converter = FormatParameter.class)
    private RDFFormat rdfFormat = RDFFormat.TTL;

    //13) Load tabular file into dataset
    @Parameter(names = {"--tabular_file", "-tf"}, description = "Tabular file to load into dataset.", converter = FileConverter.class)
    private File tabularFile = null;

    //14) Separator value - COMMA, TAB, SPACE
    @Parameter(names = {"--tabular_sep", "-ts"}, description = "Column separator in the input file. Any character except ':', '^' and '|'. Keywords TAB, SPACE and COMMA are also supported. Default: ',' ", validateWith = SeparatorValidator.class)
    private String tabularSeparator = "COMMA";

    //15) Apply default geometry to single Feature-Geometry
    @Parameter(names = {"--default_geometry", "-dg"}, description = "Apply hasDefaultGeometry to single Feature hasGeometry Geometry statements. Default: false", arity = 1)
    private boolean applyDefaultGeometry = false;

    public int getPort() {
        return port;
    }

    public String getDatsetName() {
        return datsetName;
    }

    public File getTdbFile() {
        return tdbFile;
    }

    public boolean isLoopback() {
        return loopback;
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

    public int getGeometryIndexSize() {
        return geometryIndexSize;
    }

    public int getTransformIndexSize() {
        return transformIndexSize;
    }

    public int getRewriteIndexSize() {
        return rewriteIndexSize;
    }

    public int getGeometryIndexExpiry() {
        return geometryIndexExpiry;
    }

    public int getTransformIndexExpiry() {
        return transformIndexExpiry;
    }

    public int getRewriteIndexExpiry() {
        return rewriteIndexExpiry;
    }

    public File getRdfFile() {
        return rdfFile;
    }

    public void setRdfFile(File rdfFile) {
        this.rdfFile = rdfFile;
    }

    public RDFFormat getRdfFormat() {
        return rdfFormat;
    }

    public void setRdfFormat(RDFFormat rdfFormat) {
        this.rdfFormat = rdfFormat;
    }

    public File getTabularFile() {
        return tabularFile;
    }

    public void setTabularFile(File tabularFile) {
        this.tabularFile = tabularFile;
    }

    public String getTabularSeparator() {
        return tabularSeparator;
    }

    public void setTabularSeparator(String tabularSeparator) {
        this.tabularSeparator = tabularSeparator;
    }

    public boolean isApplyDefaultGeometry() {
        return applyDefaultGeometry;
    }

    public void setApplyDefaultGeometry(boolean applyDefaultGeometry) {
        this.applyDefaultGeometry = applyDefaultGeometry;
    }

    @Override
    public String toString() {
        return "ArgsConfig{" + "port=" + port + ", datsetName=" + datsetName + ", tdbFile=" + tdbFile + ", loopback=" + loopback + ", inference=" + inference + ", queryRewrite=" + queryRewrite + ", updateAllowed=" + updateAllowed + ", indexEnabled=" + indexEnabled + ", geometryIndexSize=" + geometryIndexSize + ", transformIndexSize=" + transformIndexSize + ", rewriteIndexSize=" + rewriteIndexSize + ", geometryIndexExpiry=" + geometryIndexExpiry + ", transformIndexExpiry=" + transformIndexExpiry + ", rewriteIndexExpiry=" + rewriteIndexExpiry + ", rdfFile=" + rdfFile + ", rdfFormat=" + rdfFormat + ", tabularFile=" + tabularFile + ", tabularSeparator=" + tabularSeparator + ", applyDefaultGeometry=" + applyDefaultGeometry + '}';
    }

}