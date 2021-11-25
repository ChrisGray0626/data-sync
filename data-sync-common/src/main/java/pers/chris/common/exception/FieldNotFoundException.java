package pers.chris.common.exception;

public class FieldNotFoundException extends FieldException {

    public FieldNotFoundException(String field) {
        super(field + " doesn't exist.");
    }

}
