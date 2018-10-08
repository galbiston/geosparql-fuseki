#GeoSPARQL Fuseki

This application provides a HTTP server compliant with the GeoSPARQL standard.
It uses the embedded server Fuseki from Apache Jena and provides additional parameters for dataset loading.

The project uses the GeoSPARQL implementation from the GeoSPARQL Jena project (https://github.com/galbiston/geosparql-jena).
Currently, there is no GUI interface as provided in the Fuseki distribution.

A subset of the EPSG spatial/coordinate reference systems are included by default from the Apache SIS project (http://sis.apache.org).
The full EPSG dataset is not distributed due to the the EPSG terms of use being incompatible with the Apache Licence.
Several options are available to include the EPSG dataset by setting the `SIS_DATA` environment variable (http://sis.apache.org/epsg.html).

## Getting Started

GeoSPARQL Fuskei can be accessed as an embedded server using Maven etc. from Maven Central.

```
<dependency>
    <groupId>io.github.galbiston</groupId>
    <artifactId>geosparql-jena</artifactId>
    <version>1.0.0</version>
</dependency>
```

* Run the embedded server: `GeosparqlServer server = new GeosparqlServer(portNumber, datasetName, isLoopbackOnly, dataset, isUpdate);`

Alternatively, run the distribution from the command line:

* Load file into memory and run server: `.\geosparql-fuseki.bat -rf "test.rdf&xml"`

* Load file into persistent TDB and run server: `.\geosparql-fuseki.bat -rf "test.rdf&xml" -t ".\TestTDB"`

* Load from persistent TDB and run server: `.\geosparql-fuseki.bat -t ".\TestTDB"`

* Load from persistent TDB, change port and run server: `.\geosparql-fuseki.bat -t ".\TestTDB" -p 3030`

## Command Line Arguments

### 1) Port
```
--port, -p
```

The port number of the server. Default: 3030

### 2) Dataset name
```
--dataset, -d
```

The name of the dataset used in the URL. Default: ds

### 3) Loopback only
```
--loopback, -l
```

The server only accepts local host loopback requests. Default: true

### 4) SPARQL update allowed
```
--update, -u
```

The server accepts updates to modify the dataset. Default: false

### 5) TDB folder
```
--tdb, -t
```

An existing or new TDB folder used for the dataset. Default set to memory dataset.

### 6) GeoSPARQL RDFS inference
```
--inference, -i
```

Enable GeoSPARQL RDFS schema and inferencing (class and property hierarchy). Inferences will be applied to the dataset. Updates to dataset may require server restart. Default: true

### 7) Apply hasDefaultGeometry
```
--default_geometry, -dg
```

Apply hasDefaultGeometry to single Feature hasGeometry Geometry statements. Additional properties will be added to the dataset. Default: false

### 8) Load RDF file into dataset
```
--rdf_file, -rf
```

Comma separated list of [RDF file path#graph name&RDF format] to load into dataset. Graph name is optional and will use default graph. RDF format is optional (default: ttl) or select from one of the following: json-ld, json-rdf, nt, nq, thrift, trig, trix, ttl, ttl-pretty, xml, xml-plain, xml-pretty.
e.g. `test.rdf#test&xml,test2.rdf` will load _test.rdf_ file into _test_ graph as _RDF/XML_ and _test2.rdf_ into _default_ graph as _TTL_.

### 9) Load Tabular file into dataset
```
--tabular_file, -tf
```

Comma separated list of [Tabular file path#graph name|delimiter] to load into dataset. See RDF Tables for table formatting. Graph name is optional and will use default graph. Column delimiter is optional and will default to COMMA. Any character except ':', '^' and '|'. Keywords TAB, SPACE and COMMA are also supported.
e.g. `test.rdf#test|TAB,test2.rdf` will load _test.rdf_ file into _test_ graph as _TAB_ delimited and _test2.rdf_ into _default_ graph as _COMMA_ delimited.

See RDF Tables project (https://github.com/galbiston/rdf-tables) for more details on tabular format.

### 10) Query Rewrite enabled
```
--rewrite, -r
```

Enable query rewrite extension of GeoSPARQL standard. Default: true

### 11) Indexing enabled
```
--index, -x
```

Enable caching of re-usable data to improve query performance. Default: true
See GeoSPARQL Jena project (https://github.com/galbiston/geosparql-jena) for more details.

### 12) Geometry Literal Index size
```
--geometry_size, -gs
```

Geometry Literal index item size. Unlimited: -1, Off: 0, Default: -1

### 13) Geometry Transform Index size
```
--transform_size, -ts
```

Geometry Transform index item size. Unlimited: -1, Off: 0, Default: -1

### 14) Query Rewrite Index size
```
--rewrite_size, -rs
```

Query Rewrite index item size. Unlimited: -1, Off: 0, Default: -1

### 15) Geometry Literal expiry milliseconds
```
--geometry_expiry, -gx
```

Geometry Literal index item expiry in milliseconds. Off: 0, Minimum: 1001, Default: 5000

### 16) Geometry Transform expiry milliseconds
```
--transform_expiry, -tx
```

Geometry Literal index item expiry in milliseconds. Off: 0, Minimum: 1001, Default: 5000

### 17) Query Rewrite expiry milliseconds
```
--rewrite_expiry, -rx
```

Query Rewrite index item expiry in milliseconds. Off: 0, Minimum: 1001, Default: 5000

### 18) Properties File
Supply the above parameters as a file:
```console
$ java Main @/tmp/parameters
```

##Future Work
* GUI to assist users when querying a dataset.

Powered by Apache Jena.