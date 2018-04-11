package hu.karsany.vau.project.datamodel.model.type;

public enum BusinessDataType {
    FLAG("VARCHAR2(1)"),
    ID("NUMBER(20)"),
    SMALLTEXT("VARCHAR2(10)"),
    DATE("DATE"),
    MIDDLETEXT("VARCHAR2(100)"),
    MONEY("NUMBER"),
    INTEGER("NUMBER(20)"),
    CURRENCY("VARCHAR2(3)"),
    SHORTTEXT("VARCHAR2(10)"),
    LARGETEXT("VARCHAR2(1000)"),
    PERCENTAGE("NUMBER(10,8)"),
    NATIVE(null);

    private final String databaseDataType;

    BusinessDataType(String databaseDataType) {
        this.databaseDataType = databaseDataType;
    }

    public String getDatabaseDataType() {
        return databaseDataType;
    }
}
