package hu.karsany.vau.project.datamodel.model.type;

public class NativeDataType implements DataType {

    private final String nativeDataType;

    public NativeDataType(String nativeDataType) {
        this.nativeDataType = nativeDataType;
    }

    @Override
    public String getNativeDataType() {
        return nativeDataType;
    }

    @Override
    public BusinessDataType getBusinessDataType() {
        return BusinessDataType.NATIVE;
    }

    @Override
    public String getBusinessDataTypeName() {
        return "NATIVE";
    }
}
