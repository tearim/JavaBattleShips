package commonutils;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple parse for strings, minds quoted parameters.
 * I.e.
 * command param1 param2
 * command param1 param2
 * command param1 "param2 next word"
 */
public class ParamLine {
    public static final byte QUOTES_NOT_TRIMMED_NOT_CONDENSED = 0;
    public static final byte CONDENSE_ALL = 1;
    public static final byte IGNORE_QUOTES = 2;
    public static final byte TRIM_ALL_ANSWERS = 4;

    private static final String quote_UNIQUE_PREFIX = "PARSER_SECRET_";
    private boolean condenseAll = false;
    private boolean ignoreQuotes = false;
    private boolean trimAllQuotedParams = false;

    public ParamLine() {

    }
    public ParamLine(byte flags) {
        if ( (flags & CONDENSE_ALL) == 1 ) condenseAll = true;
        if ( (flags & IGNORE_QUOTES) == 2 ) ignoreQuotes = true;
        if ( (flags & TRIM_ALL_ANSWERS) == 4 ) trimAllQuotedParams = true;
    }

    public ParamLine setCondenseAll (boolean nVal ) {
        condenseAll = nVal;
        return this;
    }

    public ParamLine setIgnoreQuotes (boolean nVal ) {
        ignoreQuotes = nVal;
        return this;
    }

    public ParamLine setTrimAllQuotedParams (boolean nVal ) {
        trimAllQuotedParams = nVal;
        return this;

    }

    /**
     * Produces a string that is guaranteed not to be included in the given string.
     * @param param String that is going to be parsed
     * @return String that is guaranteed not be in the given string
     */
    private String getUniqueStringReplacement(String param) {
        String nonContained = quote_UNIQUE_PREFIX;
        while ( param.contains(nonContained)) {
            nonContained = quote_UNIQUE_PREFIX + ( Math.random() * 100000 ) + '_';
        }
        return nonContained;
    }

    /** Condenses whitespaces so that only " " will be around parameters.
     * Normally it is called after quoted strings, unless CONDENSE_ALL parameter is passed to constructor
     * @param param string to condense
     * @return condensed string
     */
    private String condenseWhitespaces(String param) {
        return param.replaceAll("[\s][\s]+"," ");
    }

    /**
     * Parses the given string to parameters, i.e. splits the string into words.
     * If ignoreQuotes is not set, "quoted text" will be treated as a single parameter.
     * @param param string to parse
     * @return String[] string parsed to parameters
     */
    public String[] parse (String param ) {
        if ( condenseAll) {
            param = condenseWhitespaces(param);
        }
        if ( ignoreQuotes ) {
            return splitString(param);
        }
        Pattern pattern = Pattern.compile("(^|[\s])(\".*?[\\S]+?.*?\")");
        Matcher matcher = pattern.matcher(param);

        String replacer = getUniqueStringReplacement(param);

        String[] matches = matcher.results().map(MatchResult::group).toArray(String[]::new);
        for ( int i = 0; i < matches.length; i++ ) {
            param = param.replace(matches[i], ' '+replacer + i);
        }
        if (!condenseAll) {
            param = condenseWhitespaces(param);
        }
        String[] words = param.trim().split(" ");
        ArrayList<String> workingWords = new ArrayList<>();

        for ( int i = 0; i < words.length; i++ ) {
            if ( words[i].contains(replacer)) {
                int index = Integer.parseInt(words[i].replaceAll(replacer + "([\\d]+).*", "$1"));
                String quotedVal = matches[index].trim().substring(1,matches[index].trim().length()-1);
                quotedVal = trimAllQuotedParams? quotedVal.trim(): quotedVal;
                workingWords.add( quotedVal);
                // For cases "param1 param2"param3 param4
                String trail = words[i].replace(replacer + index, "").trim();
                if ( !trail.isEmpty()) {
                    workingWords.add(trail);
                }
            } else {
                workingWords.add(words[i]);
            }
        }
        return workingWords.toArray(new String[0]);
    }

    private String[] splitString(String param ) {
        return param.trim().split(" ");
    }
}
