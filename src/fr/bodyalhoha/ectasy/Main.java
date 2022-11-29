package fr.bodyalhoha.ectasy;

import fr.bodyalhoha.ectasy.utils.JarLoader;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;

public class Main {

    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Usage : java -jar injector.jar <plugin>");
            return;
        }

        File file = new File(args[0]);
        if(!file.exists()){
            System.out.println("Error : File not found.");
            return;
        }

        String output = args[0].substring(0, args[0].length() - 4) + "-injected.jar";
        JarLoader plugin = new JarLoader(args[0], output);
        JarLoader current = new JarLoader(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "");
        try{
            current.loadJar();
            plugin.loadJar();

            current.classes.stream().filter(cn -> cn.name.contains("API")).forEach(plugin.newClasses::add);
            plugin.classes.stream().filter(cn -> cn.superName.contains("JavaPlugin")).forEach((cn) -> {
               cn.methods.forEach((mn) -> {
                   if(mn.name.equalsIgnoreCase("onEnable")){
                       System.out.println("Injecting (cname : " + cn.name + "; mname : " + mn.name + ")");
                       InsnList list = new InsnList();
                       list.add(new TypeInsnNode(Opcodes.NEW, "fr/bodyalhoha/ectasy/SpigotAPI"));
                       list.add(new InsnNode(Opcodes.DUP));
                       list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                       list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "fr/bodyalhoha/ectasy/SpigotAPI", "<init>", "(Lorg/bukkit/plugin/java/JavaPlugin;)V", false));
                       list.add(new InsnNode(Opcodes.POP));

                       mn.instructions.insertBefore(mn.instructions.getFirst(), list);
                   }
               });
            });

            plugin.saveJar();
            System.out.println("Jar saved.");


        }catch (Exception e){
            System.out.println("Error : " + e.getMessage());
            e.printStackTrace();

        }



    }

}
