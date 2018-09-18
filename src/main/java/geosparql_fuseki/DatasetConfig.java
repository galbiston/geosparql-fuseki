/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geosparql_fuseki;

import org.apache.jena.query.Dataset;

/**
 *
 *
 */
public class DatasetConfig {

    protected final String name;
    protected final Dataset dataset;
    protected final boolean allowUpdate;

    public DatasetConfig(String name, Dataset dataset, boolean allowUpdate) {
        this.name = name;
        this.dataset = dataset;
        this.allowUpdate = allowUpdate;
    }

}
