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

    public static void main(String[] args){

        OptionsParser parser = new OptionsParser(args, "i", "input", "o", "output", "bukkit");

        String input = parser.get("input", "i");

        if(input == null){
            /* usage */
            System.out.println("\n╔════════════════════ Usage ════════════════════╗");
            System.out.println("║                                               ║ ");
            System.out.println("║  java -jar injector.jar -input file.jar       ║ ");
            System.out.println("║  java -jar injector.jar -input directory      ║ ");
            System.out.println("║  java -jar injector.jar -i a.jar -o b.jar     ║");
            System.out.println("║                                               ║");
            System.out.println("╚═══════════════════════════════════════════════╝");


            return;
        }
        File inputFile = new File(input);
        if(!inputFile.exists()){
            System.out.println("Error : File not found.");
            return;
        }

        if(inputFile.isDirectory()){
            Injector.injectDirectory(inputFile);
            return;
        }


        String output = parser.getDefault(input.substring(0, input.length() - 4) + "-injected.jar", "o", "output");
        Injector.inject(input, output);



    }

}
