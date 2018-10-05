/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.github.galbiston.geosparql_fuseki.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import io.github.galbiston.rdf_tables.cli.DelimiterValidator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class FileGraphDelimiter implements IStringConverter<List<FileGraphDelimiter>>, IParameterValidator {

    private static final DelimiterValidator DELIMITER_VALIDATOR = new DelimiterValidator();
    private static final String DELIMITER_SEP = "|";
    private static final String NAME_SEP = "#";
    private final File tabFile;
    private final String graphName;
    private final String delimiter;

    public FileGraphDelimiter(File tabFile, String graphName, String delimiter) {
        this.tabFile = tabFile;
        this.graphName = graphName;
        this.delimiter = delimiter;
    }

    public File getTabFile() {
        return tabFile;
    }

    public String getGraphName() {
        return graphName;
    }

    public String getDelimiter() {
        return delimiter;
    }

    @Override
    public String toString() {
        return "FileGraphDelimiter{" + "tabFile=" + tabFile + ", graphName=" + graphName + ", delimiter=" + delimiter + '}';
    }

    @Override
    public List<FileGraphDelimiter> convert(String value) {
        String[] values = value.split(",");
        List<FileGraphDelimiter> fileList = new ArrayList<>();
        for (String val : values) {
            FileGraphDelimiter file = build(val);
            fileList.add(file);
        }
        return fileList;
    }

    public FileGraphDelimiter build(String value) {
        File file;
        String name = "";
        String delim = "COMMA";

        String target = value;
        if (target.contains(DELIMITER_SEP)) {
            String[] parts = target.split(DELIMITER_SEP);
            delim = parts[1];
            target = parts[0];
        }

        if (target.contains(NAME_SEP)) {
            String[] parts = target.split(NAME_SEP);
            name = parts[1];
            target = parts[0];
        }

        file = new File(target);

        return new FileGraphDelimiter(file, name, delim);

    }

    @Override
    public void validate(String name, String value) throws ParameterException {

        int delimIndex;
        int nameIndex;
        String[] values = value.split(",");
        for (String val : values) {
            delimIndex = val.indexOf(DELIMITER_SEP);
            nameIndex = val.indexOf(NAME_SEP);
            if (delimIndex > -1 && nameIndex > -1) {
                if (delimIndex < nameIndex) {
                    throw new ParameterException("Parameter " + name + " and value " + val + " must have delimiter (" + delimIndex + ") after graph name (" + nameIndex + ").");
                }
            }

            if (delimIndex > -1) {
                DELIMITER_VALIDATOR.validate(name, val.substring(delimIndex));
            }
        }
    }

}
