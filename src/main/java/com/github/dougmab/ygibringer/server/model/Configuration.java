package com.github.dougmab.ygibringer.server.model;

import com.github.dougmab.ygibringer.app.model.Status;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

public class Configuration implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public File inputFile;
    public String outputFileName;
    public String errorFileName;
    public String regexStr;
    public List<Status> customStatus;

    public Configuration(File inputFile, String outputFileName, String errorFileName, String regexStr, List<Status> customStatus) {
        this.inputFile = inputFile;
        Path parent = inputFile.toPath().getParent();
        this.outputFileName = parent.resolve(outputFileName).toString();
        this.errorFileName = parent.resolve(errorFileName).toString();
        this.regexStr = regexStr;
        this.customStatus = customStatus;
    }
}
