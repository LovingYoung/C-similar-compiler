package jace.app.SyntacticAnalysis;

/**
 * Created by jaceliu on 09/06/2017.
 */
public class ParseException extends Exception{
    public ParseException(String message) {
        super("Parse Exception: Unexpected " + message);
    }
}
