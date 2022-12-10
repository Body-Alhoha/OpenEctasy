package fr.bodyalhoha.ectasy.utils;

import java.util.*;

public class OptionsParser {
    // A map of command-line arguments and their values
    public HashMap<String, String> args = new HashMap<String, String>();
    // A list of boolean command-line arguments
    public List<String> bools = new ArrayList<String>();

    /**
     * Creates an instance of the OptionsParser class
     * @param args the command-line arguments to parse
     * @param has a list of argument names that take a value
     * @param contains a list of boolean argument names
     */
    public OptionsParser(String[] args, String[] has, String[] contains){
        // Iterate over the list of command-line arguments
        for(int i = 0; i < args.length; i++){
            // Normalize the argument name to lowercase
            String name = args[i].toLowerCase();
            // Remove leading "--" if present
            if(name.startsWith("--"))
                name = name.substring(2);
            // Check if the argument is a boolean argument
            if(Arrays.asList(contains).contains(name)){
                bools.add(name);
                continue;
            }
            // Check if the argument is a known argument
            if(!Arrays.asList(has).contains(name))
                continue;
            // Add the argument and its value to the args map
            this.args.put(name, args[++i]);
        }

    }

    /**
     * Returns the value of the first provided argument that is present in the args map
     * @param aliases the list of argument names to look for
     * @return the value of the argument, or null if the argument is not present
     */
    public String get(String... aliases) {
        // Iterate over the provided argument names
        for (String name : aliases) {
            // If the args map contains the argument, return its value
            if (args.containsKey(name))
                return args.get(name);
        }
        return null;
    }
    /**
     * Returns the value of the first provided argument that is present in the args map,
     * or the default value if the argument is not present
     * @param def the default value to return if the argument is not present
     * @param aliases the list of argument names to look for
     * @return the value of the argument, or the default value if the argument is not present
     */
    public String getDefault(String def, String... aliases){
        // Iterate over the provided argument names
        for(String name : aliases){
            // If the args map contains the argument, return its value
            if(args.containsKey(name))
                return args.get(name);
        }
        // If the argument is not present, return the default value
        return def;
    }

    /**
     * Returns true if the provided argument is present in the bools list,
     * false otherwise
     * @param arg the argument to look for
     * @return true if the argument is present, false otherwise
     */
    public boolean getBool(String arg){
        // Check if the provided argument is present in the bools list
        return bools.contains(arg);
    }

}
