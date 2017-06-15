package jace.app.Intermediate;

import jace.app.SyntacticAnalysis.ParseTreeNode;
import java.util.List;

/**
 * Created by jaceliu on 12/06/2017.
 */
public class Generator {
    public static EntranceTable generate(ParseTreeNode root){
        //Clear
        Utils.getInstance().tempNameClear();

        //Begin
        EntranceTable entrances = new EntranceTable();
        List<ParseTreeNode> declaratives = root.getChildren().get(0).getChildren();
        for(ParseTreeNode declarative : declaratives){
            if(declarative.getChildren().get(2).getChildren().get(0).getProperty().equals("FIELD_OP")){
                //Variable declarative
                try {
                    entrances.getSignatureTable().addSignature(VariableDeclarative(declarative));
                } catch (IntermediateException e) {
                    e.printStackTrace();
                }
            } else {
                //Function declarative
                try {
                    entrances.addEntrance(FunctionDeclarative(declarative));
                } catch (IntermediateException e) {
                    e.printStackTrace();
                }
            }
        }
        return entrances;
    }

    /**
     * Generate signature for variable declarative
     * @param declarative the variable declarative to generate signature
     * @return the generated signature
     */
    private static Signature VariableDeclarative(ParseTreeNode declarative) {
        Signature signature = new Signature();
        signature.setName(declarative.getChildren().get(1).getCode());
        String type = declarative.getChildren().get(0).getCode();
        if(type.equals("int")){
            signature.setType(Signature.Type.INTEGER);
        } else {
            signature.setType(Signature.Type.INTEGER);
        }
        signature.setParameter(false);
        return signature;
    }

    /**
     * Generate entrance for function declarative
     * @param declarative the function declarative to generate signature
     * @return the generated entrance
     */
    private static Entrance FunctionDeclarative(ParseTreeNode declarative) {
        Entrance entrance = new Entrance();
        ParseTreeNode func = declarative.getChildren().get(2).getChildren().get(0);

        //return type
        String retType = declarative.getChildren().get(0).getCode();
        entrance.setRetType(Utils.getInstance().string2Type(retType));

        //name
        String name = declarative.getChildren().get(1).getCode();
        entrance.setName(name);

        //parameter
        ParseTreeNode parameterList = func.getChildren().get(1).getChildren().get(0);
        SignatureTable signatureTable = entrance.getSignatureTable();
        for(ParseTreeNode parameter: parameterList.getChildren()){
            Signature signature = new Signature();
            signature.setName(parameter.getChildren().get(1).getCode());
            signature.setType(Utils.getInstance().string2Type(parameter.getChildren().get(0).getCode()));
            signature.setParameter(true);
            try {
                signatureTable.addSignature(signature);
            } catch (IntermediateException e) {
                e.printStackTrace();
            }
        }

        //inner declarative
        ParseTreeNode inners = func.getChildren().get(3).getChildren().get(1);
        for(ParseTreeNode inner : inners.getChildren()){
            Signature signature = new Signature();
            signature.setName(inner.getChildren().get(1).getCode());
            signature.setType(Utils.getInstance().string2Type(inner.getChildren().get(0).getCode()));
            signature.setParameter(false);
            try {
                signatureTable.addSignature(signature);
            } catch (IntermediateException e) {
                e.printStackTrace();
            }
        }

        //statements
        ParseTreeNode statements = func.getChildren().get(3).getChildren().get(2);
        QuaternionTable temp = Statements(statements);
        entrance.getQuaternionTable().merge(temp);
        return entrance;
    }

    /**
     * Generate quaternions for statements
     * @param statements the statements to generate quaternions
     * @return the generated quaternions
     */
    private static QuaternionTable Statements(ParseTreeNode statements){
        //StatementChain
        QuaternionTable qt = new QuaternionTable();
        for(ParseTreeNode statement : statements.getChildren()){
            statement = statement.getChildren().get(0);
            try {
                if (statement.getProperty().equals("AssignmentStatement")) {
                    AssignmentStatements(statement, qt);
                } else if (statement.getProperty().equals("IfStatement")) {
                    IfStatement(statement, qt);
                } else if (statement.getProperty().equals("WhileStatement")) {
                    WhileStatement(statement, qt);
                } else if (statement.getProperty().equals("ReturnStatement")) {
                    ReturnStatement(statement, qt);
                } else
                    throw new IntermediateException("Wrong Statement: " + statement.getProperty());
            } catch (IntermediateException e){
                e.printStackTrace();
            }
        }
        return qt;
    }

    /**
     * Generate quaternions for assignment statements
     * @param statement the assignment statements to generate quaternions
     * @param quaternions the generated quaternions
     * @throws IntermediateException throw if the expression return name is not the last result
     */
    private static void AssignmentStatements(ParseTreeNode statement, QuaternionTable quaternions) throws IntermediateException {
        String name = Expression(statement.getChildren().get(2), quaternions);
        quaternions.addQuaternion(":=", name, "", statement.getChildren().get(0).getCode());
    }

    /**
     * Generate quaternions for if statements
     * @param statement the if statements to generate quaternions
     * @param quaternions the generated quaternions
     */
    private static void IfStatement(ParseTreeNode statement, QuaternionTable quaternions) {
        QuaternionTable qt = new QuaternionTable();
        String name = Expression(statement.getChildren().get(2), qt);

        boolean haveElse = statement.getChildren().size() >= 6;

        int f_j_index = qt.size(); //index of false and jump
        qt.addQuaternion("j=", name, "0", "temp_f");

        int t_index = qt.size(); //index of true
        qt.merge(Statements(statement.getChildren().get(4).getChildren().get(2)));

        int t_j_index = qt.size(); // index of true and jump
        if(haveElse) {
            //True jump
            qt.addQuaternion("j", "", "", "temp_t");
        }

        //False jump here
        qt.fetch(f_j_index).setResult(String.valueOf(qt.size()));

        if(haveElse){
            //False statements
            qt.merge(Statements(statement.getChildren().get(6).getChildren().get(2)));
            qt.fetch(t_j_index).setResult(String.valueOf(qt.size()));
        }

        quaternions.merge(qt);
    }

    /**
     * Generate quaternions for while statements
     * @param statement the while statements to generate quaternions
     * @param quaternions quaternions will merge with the generated quaternions
     */
    private static void WhileStatement(ParseTreeNode statement, QuaternionTable quaternions) {
        QuaternionTable qt = new QuaternionTable();
        String name = Expression(statement.getChildren().get(2), qt);

        int judge_index = qt.size(); //index of judge

        qt.addQuaternion("j=", name, "0", "temp_end");

        int loop_index = qt.size(); // index of start of the loop body

        qt.merge(Statements(statement.getChildren().get(4).getChildren().get(2)));
        qt.addQuaternion("j", "", "", String.valueOf(loop_index));
        qt.fetch(judge_index).setResult(String.valueOf(qt.size()));
    }

    /**
     * Generate quaternions for return statements
     * @param statement the return statements to generate quaternions
     * @param quaternions quaternions will merge with the generated quaternions
     */
    private static void ReturnStatement(ParseTreeNode statement, QuaternionTable quaternions) {
        if(statement.getChildren().size() > 2){
            String name = Expression(statement.getChildren().get(1), quaternions);
            quaternions.addQuaternion("return", "", "", name);
        } else {
            quaternions.addQuaternion("return", "", "", "");
        }
    }

    /**
     * Generate quaternions for expressions
     * @param expression expression node
     * @param quaternions the quaternions to add to
     * @return the final name of the expression
     */
    private static String Expression(ParseTreeNode expression , QuaternionTable quaternions) {
        int size = expression.getChildren().size();
        if(size == 0) expression.show(System.out);
        String n1 = PlusExpression(expression.getChildren().get(0), quaternions);
        if(size == 1){
            return n1;
        }
        String n2 = PlusExpression(expression.getChildren().get(2), quaternions);
        return Compare(n1, n2, expression.getChildren().get(1).getCode(), quaternions);
    }


    private static String PlusExpression(ParseTreeNode plusExpression, QuaternionTable quaternions) {
        int size = plusExpression.getChildren().size();
        String n1 = Item(plusExpression.getChildren().get(0), quaternions);
        if(size == 1){
            return n1;
        }
        String n2;
        for(int i = 3; i <= size; i++){
            n2 = Item(plusExpression.getChildren().get(i-1), quaternions);
            quaternions.addQuaternion(plusExpression.getChildren().get(i-2).getCode(), n1, n2, n1);
        }
        return n1;
    }

    private static String Item(ParseTreeNode item, QuaternionTable quaternions) {
        int size = item.getChildren().size();
        String n1 = Factor(item.getChildren().get(0), quaternions);
        if(size == 1){
            return n1;
        }
        String n2;
        for(int i = 3; i <= size; i++){
            n2 = Factor(item.getChildren().get(i-1), quaternions);
            quaternions.addQuaternion(item.getChildren().get(i-2).getCode(), n1, n2, n1);
        }
        return n1;
    }

    private static String Factor(ParseTreeNode factor, QuaternionTable quaternions) {
        int size = factor.getChildren().size();
        if(size == 1){
            //Value or id
            ParseTreeNode child = factor.getChildren().get(0);
            if(child.getProperty().equals("VALUE")){
                String result = Utils.getInstance().getTempName();
                quaternions.addQuaternion(":=", String.valueOf(child.getCode()), "", result);
                return result;
            } else {
                return child.getCode();
            }
        } else if(size == 2){
            //Function
            String funcId = factor.getChildren().get(0).getCode();
            ParseTreeNode parameters = factor.getChildren().get(1).getChildren().get(1);
            for(ParseTreeNode parameter: parameters.getChildren()){
                //Expression
                if(!parameter.getProperty().equals("Expression")) continue;
                String name = Expression(parameter, quaternions);
                quaternions.addQuaternion("par", name, "", "");
            }
            String resultName = Utils.getInstance().getTempName();
            quaternions.addQuaternion("call", funcId, "", resultName);
            return resultName;
        } else {
            //Expression
           return Expression(factor.getChildren().get(1), quaternions);
        }
    }

    private static String Compare(String n1, String n2, String op, QuaternionTable quaternions){
        if(op.equals("==")) op = "=";
        String result = Utils.getInstance().getTempName();
        quaternions.addQuaternion("j" + op, n1, n2, String.valueOf(quaternions.size() + 3));
        quaternions.addQuaternion(":=", String.valueOf(0), "", result);
        quaternions.addQuaternion("j", "", "", String.valueOf(quaternions.size() + 2));
        quaternions.addQuaternion(":=", String.valueOf(1), "", result);
        return result;
    }
}
