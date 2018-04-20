package hu.karsany.vau.project.datamodel.parser;

import hu.karsany.vau.common.VauException;
import hu.karsany.vau.project.datamodel.model.DataModel;
import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDataModelParser implements GenericDataModelParser {

    private final List<File> fileList;

    public FileDataModelParser(List<File> fileList) {
        this.fileList = fileList;
    }

    public FileDataModelParser(File file) {
        this.fileList = new ArrayList<>();
        this.fileList.add(file);
    }

    @Override
    public DataModel parse() {
        StringBuilder stringBuilder = new StringBuilder();
        for (File file : fileList) {
            try {
                Logger.info("  Loading: " + file);
                stringBuilder.append(FileUtils.readFileToString(file, "utf-8"));
            } catch (IOException e) {
                throw new VauException(e);
            }
        }

        return new StringDataModelParser(stringBuilder.toString()).parse();
    }
}
