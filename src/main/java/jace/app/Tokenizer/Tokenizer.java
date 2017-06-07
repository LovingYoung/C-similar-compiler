package jace.app.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaceliu on 07/06/2017.
 */
public class Tokenizer {
    /**
     * Store the content of code
     */
    private String code = "";

    /**
     * Change the code to a new one
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * return the tokens
     * @return tokens
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * set the tokens
     * @param tokens the tokens to be set
     */
    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * The tokens, storing the result of tokenizations
     */
    private List<Token> tokens;

    /**
     * Construct: create a new Tokenizer
     */
    public Tokenizer(){
        //TODO: implement
    }

    /**
     * Construct: create a new Tokenizer with existing code
     * @param code the code to be tokenized
     */
    public Tokenizer(String code){
        this.code = code;
    }

    /**
     * The function at the top of the class, process all work and return a list of Tokens
     * @return the tokenized tokens
     */
    public List<Token> tokenize(){
        tokens = new ArrayList<Token>();
        Token token = new Token("", Token.Type.INIT);
        for(int i = 0; i < code.length(); i++){
            char ch = code.charAt(i);
            token = rule(token, ch);
        }
        return tokens;
    }

    /**
     * The rule of the tokenizer
     * @param token the token now
     * @param ch the current character
     * @return current state expressed in a token
     */
    private Token rule(Token token, char ch) {
        String content = token.getContent();
        Token.Type type = token.getType();
        if(type == Token.Type.COMMENT_LINES){
            if(content.charAt(content.length() - 1) == '*' && ch == '/'){
                //Comment over
                addToken(new Token(content+'/', Token.Type.COMMENT_LINES));
                return new Token("", Token.Type.INIT);
            } else {
                //Comment continue
                return new Token(content + ch, type);
            }
        } else if(ch == '\n'){
            addToken(token);
            return new Token("", Token.Type.INIT);
        } else if(type == Token.Type.COMMENT_LINE) {
            token.setContent(content + ch);
            return token;
        } else if(ch == ' ') {
            addToken(token);
            return new Token("", Token.Type.INIT);
        } else if(ch == '='){
            if(type == Token.Type.ASSIGNMENT) {
                addToken(new Token("==", Token.Type.OPERATOR));
                return new Token("", Token.Type.INIT);
            } else if((type == Token.Type.OPERATOR) && (content.equals("<") || content.equals(">") || content.equals("!"))) {
                addToken(new Token(content + ch, Token.Type.OPERATOR));
                return new Token("", Token.Type.INIT);
            } else {
                addToken(token);
                return new Token("=", Token.Type.ASSIGNMENT);
            }
        } else if(ch == '+'){
            addToken(token);
            addToken(new Token("+", Token.Type.OPERATOR));
            return new Token("", Token.Type.INIT);
        } else if(ch == '-'){
            addToken(token);
            addToken(new Token("-", Token.Type.OPERATOR));
            return new Token("", Token.Type.INIT);
        } else if(ch == '*'){
            if(type == Token.Type.OPERATOR && token.getContent().equals("/")){
                //Enter comment
                return new Token("/*", Token.Type.COMMENT_LINES);
            } else {
                addToken(token);
                return new Token("*", Token.Type.OPERATOR);
            }
        } else if(ch == '/'){
            if(type == Token.Type.OPERATOR && token.getContent().equals("/")){
                //Enter comment
                return new Token("//", Token.Type.COMMENT_LINE);
            } else {
                return new Token(String.valueOf(ch), Token.Type.OPERATOR);
            }
        } else if(ch == '!'){
            addToken(token);
            return new Token("!", Token.Type.OPERATOR);
        } else if(ch == '>' || ch == '<'){
            addToken(token);
            return new Token(String.valueOf(ch), Token.Type.OPERATOR);
        } else if(ch == ';'){
            addToken(token);
            addToken(new Token(String.valueOf(ch), Token.Type.FIELD_OP));
            return new Token("", Token.Type.INIT);
        } else if(ch == ','){
            addToken(token);
            addToken(new Token(String.valueOf(ch), Token.Type.DIVIDE_OP));
            return new Token("", Token.Type.INIT);
        } else if(ch == '('){
            addToken(token);
            addToken(new Token(String.valueOf(ch), Token.Type.LEFT_PARENTHESIS));
            return new Token("", Token.Type.INIT);
        } else if(ch == ')'){
            addToken(token);
            addToken(new Token(String.valueOf(ch), Token.Type.RIGHT_PARENTHESIS));
            return new Token("", Token.Type.INIT);
        } else if(ch == '{'){
            addToken(token);
            addToken(new Token(String.valueOf(ch), Token.Type.LEFT_BRACE));
            return new Token("", Token.Type.INIT);
        } else if(ch == '}'){
            addToken(token);
            addToken(new Token(String.valueOf(ch), Token.Type.RIGHT_BRACE));
            return new Token("", Token.Type.INIT);
        } else if(ch == '#'){
            addToken(token);
            addToken(new Token(String.valueOf(ch), Token.Type.END_OP));
            return new Token("", Token.Type.INIT);
        } else if(ch >= '0' && ch <= '9'){
            if(type == Token.Type.VALUE || type == Token.Type.DIGIT){
                return new Token(content + ch, Token.Type.VALUE);
            } else if(type == Token.Type.IDENTIFIER || type == Token.Type.CHARACTER){
                return new Token(content + ch, Token.Type.IDENTIFIER);
            } else {
                addToken(token);
                return new Token(String.valueOf(ch), Token.Type.DIGIT);
            }
        } else if((ch >= 'a' || ch <='z') || (ch >= 'A' || ch <= 'Z')){
            if(type == Token.Type.IDENTIFIER || type == Token.Type.CHARACTER){
                return new Token(content + ch, Token.Type.IDENTIFIER);
            } else {
                addToken(token);
                return new Token(String.valueOf(ch), Token.Type.CHARACTER);
            }
        }
        return null;
    }

    /**
     * add a new token to the result
     * @param token the token to be added
     */
    private void addToken(Token token){
        Token.Type type = token.getType();
        String content = token.getContent();
        if(token.getType() == Token.Type.INIT) return;
        else if(token.getType() == Token.Type.IDENTIFIER && Token.KEYWORDS.contains(token.getContent())){
            token.setType(Token.Type.KEYWORD);
            tokens.add(token);
        } else if(type == Token.Type.CHARACTER) {
            token.setType(Token.Type.IDENTIFIER);
            tokens.add(token);
        } else{
            tokens.add(token);
        }
    }
}
