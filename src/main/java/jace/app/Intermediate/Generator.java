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
        for(ParseTreeNode statement : statements.getChildren()){
            statement = statement.getChildren().get(0);
            try {
                if (statement.getProperty().equals("AssignmentStatements")) {
                    AssignmentStatements(statement, entrance.getQuaternionTable());
                } else if (statement.getProperty().equals("IfStatement")) {
                    IfStatement(statement, entrance.getQuaternionTable());
                } else if (statement.getProperty().equals("WhileStatement")) {
                    WhileStatement(statement, entrance.getQuaternionTable());
                } else if (statement.getProperty().equals("ReturnStatement")) {
                    ReturnStatement(statement, entrance.getQuaternionTable());
                } else
                    throw new IntermediateException("Wrong Statement: " + statement.getProperty());
            } catch (IntermediateException e){
                e.printStackTrace();
            }
        }
        return entrance;
    }

    private static void AssignmentStatements(ParseTreeNode statement, QuaternionTable quaternions) throws IntermediateException {
        String name = Expression(statement.getChildren().get(2), quaternions);
        Quaternion quaternion = quaternions.getLastQuaternion();
        if(quaternion.getResult().equals(name)){
            quaternion.setResult(statement.getChildren().get(0).getCode());
        } else
            throw new IntermediateException("Expression name is not " + name);
    }

    private static void IfStatement(ParseTreeNode statement, QuaternionTable quaternions) {
        QuaternionTable qt = new QuaternionTable();
        String name = Expression(statement.getChildren().get(2), qt);

        boolean haveElse = statement.getChildren().size() >= 6;

        int f_j_index = qt.size(); //index of false and jump
        qt.addQuaternion("j!=", name, "0", "temp_f");

        int t_index = qt.size(); //index of false
        QuaternionTable qt_temp =
    }

    private static void WhileStatement(ParseTreeNode statement, QuaternionTable quaternions) {
    }

    private static void ReturnStatement(ParseTreeNode statement, QuaternionTable quaternions) {
    }

    private static String Expression(ParseTreeNode parseTreeNode, QuaternionTable quaternions) {
    }

}
