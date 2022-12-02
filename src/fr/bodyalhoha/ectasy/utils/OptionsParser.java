package fr.bodyalhoha.ectasy.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OptionsParser {

    public HashMap<String, String> args = new HashMap<String, String>();


    public OptionsParser(String[] args, String... has){
        for(int i = 0; i < args.length; i++){
            String name = args[i];
            if(name.startsWith("--"))
                name = name.substring(2);
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


}

