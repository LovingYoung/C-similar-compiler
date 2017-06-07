package jace.app.Tokenizer;

/**
 * Created by jaceliu on 07/06/2017.
 */
public class Token {
    /**
     * Token Types
     */
    public static enum Type {
        STRING, VALUE, IDENTIFIER, SETTER, OPERATOR, DIVIDE_OP,
        COMMENT_OP, LEFT_PARENTHESIS, RIGHT_PARENTHESIS,
        LEFT_BRACE, RIGHT_BRACE, CHARACTER, DIGIT, END_OP
    }

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
}
