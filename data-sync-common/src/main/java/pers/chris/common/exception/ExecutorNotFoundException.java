package pers.chris.common.exception;

import pers.chris.common.typeEnum.ExecutorTypeEnum;

public class ExecutorNotFoundException extends Exception{

    public ExecutorNotFoundException(ExecutorTypeEnum executorType) {
        super(executorType + " is not found.");
    }

}
