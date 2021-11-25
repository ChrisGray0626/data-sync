package pers.chris.common.exception;

public class FieldTypeException extends FieldException{

    public FieldTypeException(String srcField, String dstField) {
        super(srcField + "'s type is not adapted to " + dstField);
    }
}
