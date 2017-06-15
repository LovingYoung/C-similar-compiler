package jace.app.SyntacticAnalysis;

import java.io.PrintStream;
import java.util.ArrayList;
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
    private List<ParseTreeNode> children = new ArrayList<ParseTreeNode>();

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

    /**
     * Show the tree
     */
    public void show(PrintStream out){
        this.show(out, 1, true, new ArrayList<Integer>(), -1);
    }

    /**
     * The inner implementation of showing the tree
     * @param out the destination of show
     * @param level current tree level
     * @param last is this node is the last sibling
     * @param count the node before
     * @param parent the parent place
     */
    private void show(PrintStream out, int level, boolean last, ArrayList<Integer> count, int parent){
        StringBuilder prefix;
        String genPrefix;
        if(parent == -1){
            prefix = new StringBuilder("└── ");
        } else{
            if (!count.contains(parent))
                count.add(parent);
            prefix = new StringBuilder(new String(new char[4 * level]).replace("\0", " "));
            for(int i : count) prefix.setCharAt(i, '│');
            if(last){
                prefix.setCharAt(parent, '└');
                count.remove(new Integer(parent));
            } else
                prefix.setCharAt(parent, '├');
            for(int i = parent + 1; i < prefix.length() - 1; i++) prefix.setCharAt(i, '─');
            prefix.setCharAt(prefix.length() - 1, ' ');
        }
        genPrefix = prefix.toString();
        if(this.getCode().equals("")){
            out.println(genPrefix + this.getProperty());
        } else {
            out.println(genPrefix + this.getProperty() + " code: \"" + this.getCode() + "\"");
        }

        if(this.getChildren().size() > 0){
            int length = this.getChildren().size();
            for(int i = 0; i < length - 1; i++){
                getChildren().get(i).show(out, level + 1, false, count, level * 4);
            }
            getChildren().get(length - 1).show(out, level + 1, true, count, level * 4);
        }
    }
}
