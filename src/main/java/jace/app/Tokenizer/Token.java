package jace.app.Tokenizer;

import java.util.List;

/**
 * Created by jaceliu on 07/06/2017.
 */
public class Token {
    /**
     * Token Types
     */
    public static enum Type {
        KEYWORD, VALUE, IDENTIFIER, ASSIGNMENT, OPERATOR, FIELD_OP, DIVIDE_OP,
        COMMENT_LINES, COMMENT_LINE, LEFT_PARENTHESIS, RIGHT_PARENTHESIS,
        LEFT_BRACE, RIGHT_BRACE, CHARACTER, DIGIT, END_OP, INIT
    }

    private static final String[] KEYWORDS_ARRAY = {"int", "void", "if", "else", "while", "return"};
    public static final List<String> KEYWORDS = java.util.Arrays.asList(KEYWORDS_ARRAY);

    /**
     * The content of the token
     */
    private String content;

    /**
     * The type of the token
     */
    private Type type;

    /**
     * Get the type of the token
     * @return the type of token
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of the token
     * @param content the content to be set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the type of the token
     * @return
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the type of the token
     * @param type the type to be set
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Construtor to create a new token with nothing
     */
    public Token(){ }

    /**
     * Constructor to create a new token with some properties
     * @param content the content of the token
     * @param type the type of the token
     */
    public Token(String content, Type type){
        this.content = content;
        this.type = type;
    }
}
