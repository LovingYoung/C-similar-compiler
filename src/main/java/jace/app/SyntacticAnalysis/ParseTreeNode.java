package jace.app.SyntacticAnalysis;

import java.util.List;

/**
 * Created by jaceliu on 08/06/2017.
 */
public class ParseTreeNode {
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * the code of the node
     */
    public String code;

    /**
     * the value of the node's property
     */
    private String property;

    /**
     * Store the children of the node
     */
    private List<ParseTreeNode> children;

    /**
     * get Children of the node
     * @return
     */
    public List<ParseTreeNode> getChildren() {
        return children;
    }

    /**
     * Clear all children
     */
    public void clear(){
        children.clear();
    }

    /**
     * Add a new child
     * @param child the child to be added
     */
    public void addChild(ParseTreeNode child){
        children.add(child);
    }

    /**
     * Remove a child
     * @param child the child to be removed
     */
    public void removeChild(ParseTreeNode child){
        children.remove(child);
    }

    public ParseTreeNode(String property, String code){
        this.property = property;
        this.code = code;
    }
}
