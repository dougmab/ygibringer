package com.github.dougmab.ygibringer.server.model;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;

public class ConfigurationProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public File inputFile;
    public String outputFileName;
    public String errorFileName;
    public String regexStr;

    public ConfigurationProperties(File inputFile, String outputFileName, String errorFileName, String regexStr) {
        this.inputFile = inputFile;
        Path parent = inputFile.toPath().getParent();
        this.outputFileName = parent.resolve(outputFileName).toString();
        this.errorFileName = parent.resolve(errorFileName).toString();
        this.regexStr = regexStr;
    }
}
