package jace.app.Assembly;

import jace.app.Intermediate.Entrance;
import jace.app.Intermediate.EntranceTable;
import jace.app.Intermediate.Signature;
import jace.app.Intermediate.SignatureTable;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaceliu on 15/06/2017.
 */
public class AssemblyGenerator {
    private static List<String> data;
    private static List<String> text;

    public static void generate(EntranceTable entrances, PrintStream out){
        data = new ArrayList<String>();
        text = new ArrayList<String>();

        initialText(text);
        initialData(data, entrances.getSignatureTable());

        for(int i = 0; i < entrances.size(); i++){
            Entrance entrance = entrances.fetch(i);
            text.add(new Function(entrance).generate());
        }

        out.println(dataWrapper(data) + "\n" + textWrapper(text));
    }

    private static void initialData(List<String> data, SignatureTable signatureTable) {
        for(int i = 0; i < signatureTable.size(); i++){
            Signature signature = signatureTable.fetch(i);
            data.add("    " + String.valueOf(signature.getName()) + " dd 0");
        }
    }

    private static void initialText(List<String> text) {
        text.add("    global main");
    }

    private static String textWrapper(List<String> texts){
        StringBuilder sb = new StringBuilder("Section .text\n");
        for(String string: texts){
            sb.append(string + "\n");
        }
        return sb.toString();
    }

    private static String dataWrapper(List<String> texts){
        StringBuilder sb = new StringBuilder("Section .data\n");
        for(String string: texts){
            sb.append(string + "\n");
        }
        return sb.toString();
    }
}
