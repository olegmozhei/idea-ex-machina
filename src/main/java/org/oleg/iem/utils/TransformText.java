package org.oleg.iem.utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Convert substring {{ value | value }} into something meaningful
 */
public class TransformText {

    public static String transformString(String input, HashMap<String, String> substitution){

        Pattern p = Pattern.compile("\\{\\{ ([^}]*) }}");
        Matcher m = p.matcher(input);
        while (m.find()){
            for (int i = 0; i < m.groupCount(); i++) {
                String key = m.group(i);
                key = key.substring(3, key.indexOf(" }}"));
                String[] functionToApply = key.split("\\|");
                String convertingResult = "";

                for (String function: functionToApply){
                    function = function.trim();
                    if (substitution.containsKey(function)){
                        String replacement = substitution.get(function);
                        if (replacement == null){
                            String error = "Trying to get convert value '" + function + "'\n";
                            error+= "Context has this value, but converting result is null\n";
                            error+= "Please correct application implementation";
                            throw new RuntimeException(error);
                        }
                    } else {
                        throw new RuntimeException("Can't replace value '" + function + "'");
                    }
                }
                input = input.replaceFirst("\\{\\{ ([^}]*) }}", Matcher.quoteReplacement(convertingResult));
            }
        }
        return input;
    }


}
