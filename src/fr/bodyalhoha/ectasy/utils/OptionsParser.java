package fr.bodyalhoha.ectasy.utils;

import java.util.*;

public class OptionsParser {

    public HashMap<String, String> args = new HashMap<String, String>();
    public List<String> bools = new ArrayList<String>();

    public OptionsParser(String[] args, String[] has, String[] contains){
        for(int i = 0; i < args.length; i++){
            String name = args[i].toLowerCase();
            if(name.startsWith("--"))
                name = name.substring(2);
            if(Arrays.asList(contains).contains(name)){
                bools.add(name);
                continue;
            }
            if(!Arrays.asList(has).contains(name))
                continue;

            this.args.put(name, args[++i]);
        }

    }

    public String get(String... aliases){
        for(String name : aliases){
            if(args.containsKey(name))
                return args.get(name);
        }

        return null;
    }

    public String getDefault(String def, String... aliases){
        for(String name : aliases){
            if(args.containsKey(name))
                return args.get(name);
        }

        return def;
    }

    public boolean getBool(String arg){
        return bools.contains(arg);
    }


}

