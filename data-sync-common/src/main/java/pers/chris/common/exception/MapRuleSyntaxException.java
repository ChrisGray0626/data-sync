package pers.chris.common.exception;

public class MapRuleSyntaxException extends Exception{

    public MapRuleSyntaxException(String rule) {
        super(rule + " exists syntax error");
    }

}
