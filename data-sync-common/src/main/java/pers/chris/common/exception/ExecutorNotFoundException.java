package pers.chris.common.exception;

import pers.chris.common.type.ExecutorTypeEnum;

public class ExecutorNotFoundException extends Exception{

    public ExecutorNotFoundException(ExecutorTypeEnum executorType) {
        super(executorType + " is not found.");
    }

}
