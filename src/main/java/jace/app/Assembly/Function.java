package jace.app.Assembly;

import jace.app.Intermediate.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jaceliu on 15/06/2017.
 */
public class Function {
    private List<Variable> parameters = new ArrayList<Variable>();
    private List<Variable> locals = new ArrayList<Variable>();
    private Map<String, String> name2Code = new HashMap<String, String>();
    private Map<Integer, String> line2Procedure = new HashMap<Integer, String>();
    private static final String prefix = "    ";

    private QuaternionTable qt;
    private SignatureTable st;
    private String name;
    private Signature.Type retType;

    public Function(Entrance entrance){
        qt = entrance.getQuaternionTable();
        st = entrance.getSignatureTable();
        name = entrance.getName();
        retType = entrance.getRetType();
        for(int i = 0; i < st.size(); i++){
            if(st.fetch(i).isParameter())
                parameters.add(new Variable(st.fetch(i)));
            else
                locals.add(new Variable(st.fetch(i)));
        }
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Signature.Type getRetType() {
        return retType;
    }

    private void setRetType(Signature.Type retType) {
        this.retType = retType;
    }

    private int top = 1;

    public String generate() {
        StringBuilder code = new StringBuilder("");

        int vars = parameters.size() + locals.size() + qt.getCountTempVars();
        code.append(prefix + "sub esp, " + String.valueOf(vars * 4) + "\n");


        //parameters
        for(Variable parameter: parameters){
            name2Code.put(parameter.getName(), "DWORD [ebp-" + String.valueOf(top * 4) + "]");
            top += 1;
            code.append(prefix + "pop eax\n");
            code.append(prefix + "mov " + name2Code.get(parameter.getName()) + ", eax\n");
        }

        //local variables
        for(Variable variable: locals){
            name2Code.put(variable.getName(), "DWORD [ebp-" + String.valueOf(top * 4) + "]");
            top += 1;
        }

        for(int i = 0; i < qt.size(); i++){
            Quaternion quaternion = qt.fetch(i);
            String op = quaternion.getOp();
            String arg1 = quaternion.getArg1();
            String arg2 = quaternion.getArg2();
            String result = quaternion.getResult();

            if(line2Procedure.containsKey(i)){
                code.append(line2Procedure.get(i) + ":\n");
            }

            if(op.equals(":=")){
                code.append(prefix + "mov eax, " + transfer(arg1) + "\n");
                code.append(prefix + "mov " + transfer(result) + ", eax\n");
            } else if(op.equals("+")){
                code.append(prefix + "mov eax, " + transfer(arg1) + "\n");
                code.append(prefix + "add eax, " + transfer(arg2) + "\n");
                code.append(prefix + "mov " + transfer(result) + ", eax \n");
            } else if(op.equals("-")){
                code.append(prefix + "mov eax, " + transfer(arg1) + "\n");
                code.append(prefix + "sub eax, " + transfer(arg2) + "\n");
                code.append(prefix + "mov " + transfer(result) + ", eax \n");
            } else if(op.equals("*")){
                code.append(prefix + "mov eax," + transfer(arg1) + "\n");
                code.append(prefix + "imul " + transfer(arg2) + "\n");
                code.append(prefix + "mov " + transfer(result) + ", eax \n");
            } else if(op.equals("return")) {
                if(this.getName().equals("main")) continue;
                if(!result.equals("")){
                    code.append(prefix + "push " + transfer(result) + "\n");
                }
                code.append(prefix + "ret");
            } else if(op.equals("par")) {
                code.append(prefix + "push " + transfer(arg1) + "\n");
            } else if(op.startsWith("j")){
                op = op.substring(1);
                String procedure_name = Utils.getInstance().getTempName();
                line2Procedure.put(Integer.parseInt(result), procedure_name);
                if(op.equals("<")){
                    code.append(prefix + "mov eax, " + transfer(arg1) + "\n");
                    code.append(prefix + "cmp eax, " + transfer(arg2) + "\n");
                    code.append(prefix + "jb " + procedure_name);
                } else if(op.equals(">")){
                    code.append(prefix + "mov eax, " + transfer(arg1) + "\n");
                    code.append(prefix + "cmp eax, " + transfer(arg2) + "\n");
                    code.append(prefix + "ja " + procedure_name);
                } else if(op.equals("=")){
                    code.append(prefix + "mov eax, " + transfer(arg1) + "\n");
                    code.append(prefix + "cmp eax, " + transfer(arg2) + "\n");
                    code.append(prefix + "je " + procedure_name + "\n");
                } else if(op.equals("")){
                    code.append(prefix + "jmp " + procedure_name + "\n");
                } else {
                    code.append(prefix + "Unsupported operator: j" + op + "\n");
                }
            } else if(op.equals("call")){
                code.append(prefix + "call " + transfer(arg1) + "\n");
                if(!result.equals("")){
                    code.append(prefix + "pop eax\n");
                    code.append(prefix + "mov " + transfer(result) + ", eax\n");
                }
            } else
                code.append(prefix + "Unsupported operator: " + op + "\n");
        }

        if(getName().equals("main")){
            return otherWrapper(stackFrame(code.toString()));
        } else {
            return otherWrapper(code.toString());
        }
    }

    private String stackFrame(String code) {
        String head = "    push ebp\n" +
                "    mov ebp, esp\n";
        String tail = "    mov esp, ebp\n" +
                "    pop ebp\n" +
                "    ret\n";
        return head + code + tail;
    }

    private String otherWrapper(String code){
        String funcName = getName() + ":\n";
        return funcName + code;
    }

    /**
     * transfer str into a valid code string
     * @param str the str of quaternion
     * @return valid code string
     */
    private String transfer(String str){
        try{
            Integer.parseInt(str);
            return str;
        } catch (NumberFormatException e){
            if(!name2Code.containsKey(str)) {
                name2Code.put(str, "DWORD [ebp-" + String.valueOf(top * 4) + "]");
                top += 1;
            }
            return name2Code.get(str);
        }
    }
}
