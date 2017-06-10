package jace.app;

import jace.app.SyntacticAnalysis.SyntacticAnalyze;
import jace.app.Tokenizer.Token;
import jace.app.Tokenizer.Tokenizer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class App
{
    private static final String HELP = "Usage: compile <code filename>";
    public static void main( String[] args )
    {
        if(args.length < 1){
            System.out.println(HELP);
            return;
        }
        String filename = args[0];
        String code;
        try {
            code = readFile(filename);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return;
        }
        SyntacticAnalyze.analyze(code);
    }

    public static String readFile(String filename) throws IOException {
        FileReader fr;
        fr = new FileReader(new File(filename));
        char[] code = new char[100];
        StringBuilder stringBuilder = new StringBuilder("");
        while(fr.read(code) != -1){
            stringBuilder.append(code);
        }
        return stringBuilder.toString();
    }
}
