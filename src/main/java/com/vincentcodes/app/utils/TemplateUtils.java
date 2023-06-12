package com.vincentcodes.app.utils;

import java.util.Map;

public class TemplateUtils {
    /**
     * {@code injectTemplate(["{{asd}}"], Map{"asd": "value2"})} returns {@code ["value2"]}
     */
    public static String[] injectTemplate(String[] command, Map<String, String> variableToValueMap){
        String[] newArr = command.clone();
        for(int i = 0; i < newArr.length; i++){
            if(newArr[i].startsWith("{{") && newArr[i].endsWith("}}")){
                String value = variableToValueMap.get(newArr[i].substring(2, newArr[i].length()-2));
                if(value == null)
                    continue;
                newArr[i] = value;
            }
        }
        return newArr;
    }
}
