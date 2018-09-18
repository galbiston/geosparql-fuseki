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

/**
 *
 *
 */
public class ArgsConfig {

    //0) Port
    @Parameter(names = {"--port", "-p"}, description = "Port number. Default: 3030")
    private int port = 3030;

    //1) Dataset name
    @Parameter(names = {"--dataset", "-d"}, description = "Dataset name. Default: default")
    private String datsetName = "default";

    //2) TDB folder
    @Parameter(names = {"--tdb", "-t"}, description = "TDB Folder", converter = FileConverter.class)
    private File tdbFile = null;

    //3) Loopback only
    @Parameter(names = {"--loopback", "-l"}, description = "Loopback only. Default: true", arity = 1)
    private boolean loopback = true;

    //4) GeoSPARQL RDFS inference
    @Parameter(names = {"--inference", "-i"}, description = "Enable GeoSPARQL RDFS schema and inferencing. Default: true", arity = 1)
    private boolean inference = true;

    //5) Query Rewrite enabled
    @Parameter(names = {"--rewrite", "-r"}, description = "Enable query rewrite. Default: true", arity = 1)
    private boolean queryRewrite;

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

    @Override
    public String toString() {
        return "ArgsConfig{" + "port=" + port + ", datsetName=" + datsetName + ", tdbFile=" + tdbFile + ", loopback=" + loopback + ", inference=" + inference + ", queryRewrite=" + queryRewrite + ", updateAllowed=" + updateAllowed + ", indexEnabled=" + indexEnabled + ", geometryIndexSize=" + geometryIndexSize + ", transformIndexSize=" + transformIndexSize + ", rewriteIndexSize=" + rewriteIndexSize + ", geometryIndexExpiry=" + geometryIndexExpiry + ", transformIndexExpiry=" + transformIndexExpiry + ", rewriteIndexExpiry=" + rewriteIndexExpiry + '}';
    }

}
