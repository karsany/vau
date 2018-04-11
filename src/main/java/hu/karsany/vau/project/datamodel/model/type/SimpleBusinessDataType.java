package hu.karsany.vau.project.datamodel.model.type;

public class SimpleBusinessDataType implements DataType {

    private final BusinessDataType businessDataType;

    public SimpleBusinessDataType(BusinessDataType businessDataType) {
        this.businessDataType = businessDataType;
    }

    @Override
    public String getNativeDataType() {
        return businessDataType.getDatabaseDataType();
    }

    @Override
    public BusinessDataType getBusinessDataType() {
        return businessDataType;
    }

    @Override
    public String getBusinessDataTypeName() {
        return businessDataType.name();
    }
}
