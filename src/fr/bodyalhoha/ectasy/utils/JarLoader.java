package fr.bodyalhoha.ectasy.utils;

import java.io.File;
import java.io.InputStream;
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

/**
 * This class is a utility for working with JAR files.
 */
public class JarLoader {

    /**
     * A list of ClassNode objects that have been parsed from the input JAR file.
     */
    public List<ClassNode> classes = new ArrayList<ClassNode>();

    /**
     * A list of ClassNode objects that have been added after the input JAR file was loaded.
     */
    public List<ClassNode> newClasses = new ArrayList<ClassNode>();

    /**
     * A list of UnknownFile objects that have been parsed from the input JAR file.
     */
    public List<UnknownFile> files = new ArrayList<UnknownFile>();

    /**
     * The file path of the input JAR file.
     */
    public String input;

    /**
     * The file path of the output JAR file.
     */
    public String output;

    /**
     * Constructs a JarLoader instance with the specified input and output JAR files.
     *
     * @param input The file path of the input JAR file.
     * @param output The file path of the output JAR file.
     */
    public JarLoader(String input,String output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Loads the input JAR file and parses its contents into a list of ClassNode objects and a list of UnknownFile objects.
     *
     * @throws Exception If an error occurs while reading or parsing the input JAR file.
     */
    public void loadJar() throws Exception{
        File inputFile = new File(input);
        try (ZipInputStream jarInputStream = new ZipInputStream(Files.newInputStream(inputFile.toPath()))) {
            ZipEntry zipEntry;
            while ((zipEntry = jarInputStream.getNextEntry()) != null) {
                if(zipEntry.getName().startsWith("META-INF"))
                    continue;
                if (zipEntry.getName().endsWith(".class")) {
                    try{
                        ClassReader reader = new ClassReader(jarInputStream);
                        ClassNode classNode = new ClassNode();
                        reader.accept(classNode, 0);
                        classes.add(classNode);
                    }catch (Exception e){
                        files.add(new UnknownFile(zipEntry.getName(), jarInputStream));

                    }

                }else {
                    files.add(new UnknownFile(zipEntry.getName(), jarInputStream));
                }
            }
        }

    }

    /**
     * Saves the contents of the input JAR file, along with any additional ClassNode objects and UnknownFile objects that have been added, to the output JAR file.
     *
     * @throws Exception If an error occurs while writing to the output JAR file.
     */
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

    /**
     * Adds a ClassNode object to the list of ClassNode objects.
     *
     * @param cn The ClassNode object to add.
     */
    public void addClass(ClassNode cn) {
        this.classes.add(cn);
    }
}

