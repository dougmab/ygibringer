package com.github.dougmab.ygibringer.server.model;

import com.github.dougmab.ygibringer.app.model.Status;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuration implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public File inputFile;
    public String outputFileName;
    public String errorFileName;
    public String regexStr;
    public List<Status> customStatus;
    public Status repeatedStatus;

    public Configuration(File inputFile, String outputFileName, String errorFileName, String regexStr, List<Status> customStatus) {
        this.inputFile = inputFile;
        Path parent = inputFile.toPath().getParent();
        this.outputFileName = parent.resolve(outputFileName).toString();
        this.errorFileName = parent.resolve(errorFileName).toString();
        this.regexStr = regexStr;
        this.customStatus = customStatus;
        Pattern pattern = Pattern.compile("repetid[oa]|\\brepeat(ed)?", Pattern.CASE_INSENSITIVE);

        for (Status status : this.customStatus) {
            Matcher matcher = pattern.matcher(status.getTitle());
            if (matcher.find()) {
                repeatedStatus = status;
                break;
            }
        }
    }
}
