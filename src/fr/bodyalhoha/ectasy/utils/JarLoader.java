package fr.bodyalhoha.ectasy.utils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class JarLoader {

    public List<ClassNode> classes = new ArrayList<ClassNode>();
    public List<ClassNode> newClasses = new ArrayList<ClassNode>();
    public List<UnknownFile> files = new ArrayList<UnknownFile>();
    public String input;
    public String output;

    public JarLoader(String input,String output) {
        this.input = input;
        this.output = output;
    }

    public void loadJar() throws Exception{
        File inputFile = new File(input);
        JarFile jar = new JarFile(inputFile);
        try (ZipInputStream jarInputStream = new ZipInputStream(Files.newInputStream(inputFile.toPath()))) {
            ZipEntry zipEntry;
            while ((zipEntry = jarInputStream.getNextEntry()) != null) {
                if(zipEntry.getName().startsWith("META-INF"))
                    continue;
                if (zipEntry.getName().endsWith(".class")) {
                    System.out.println(zipEntry.getName());
                    ClassReader reader = new ClassReader(jarInputStream);
                    ClassNode classNode = new ClassNode();
                    reader.accept(classNode, 0);
                    classes.add(classNode);
                }else {
                    files.add(new UnknownFile(zipEntry.getName(), jarInputStream));
                }
            }
        }

    }

    public void saveJar() throws Exception {
        File outputFile = new File(output);
        try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(outputFile.toPath()))) {
            for (ClassNode classNode : classes) {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classNode.accept(writer);
                out.putNextEntry(new JarEntry(classNode.name + ".class"));
                out.write(writer.toByteArray());
            }
            for (ClassNode classNode : newClasses) {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classNode.accept(writer);
                out.putNextEntry(new JarEntry(classNode.name + ".class"));
                out.write(writer.toByteArray());
            }
            for(UnknownFile file : files) {
                out.putNextEntry(new JarEntry(file.name));
                out.write(file.bytes);
            }
        }

    }

    public void addClass(ClassNode cn) {
        this.classes.add(cn);
    }
}