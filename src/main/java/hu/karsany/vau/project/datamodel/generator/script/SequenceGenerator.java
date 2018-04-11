package hu.karsany.vau.project.datamodel.generator.script;

import hu.karsany.vau.common.Generator;
import hu.karsany.vau.project.datamodel.model.Entity;

public class SequenceGenerator implements Generator {
    private final Entity entity;

    public SequenceGenerator(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String getFileName() {
        return entity.getTableType() + "_" + entity.getEntityName() + "_SEQ.sql";
    }

    @Override
    public String toString() {
        return "CREATE SEQUENCE " + entity.getTableType() + "_" + entity.getEntityName() + "_SEQ START WITH 10000" + "\n" + "/\n";
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.SEQUENCE;
    }
}
