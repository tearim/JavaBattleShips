package commonutils.ParamLineTests;

import commonutils.Ansi;
import commonutils.ParamLine;

public class Tests {
    public static void main(String[] args) {
        ParamLine paramLine1 = new ParamLine();
        // same as
        ParamLine paramLine = new ParamLine(ParamLine.QUOTES_NOT_TRIMMED_NOT_CONDENSED);


        String[] strings = new String[8];
        strings[0] = "packet \"quoted parameter\" -parameter \"quoted param-2\" another";
        strings[1] = "packet word\"with_a_quote another_parameter";
        strings[2] = "packet \" parameter \" word2 word3";
        strings[3] = "packet \"parameter param \" some other words";
        strings[4] = "packet \"a whole string of words";
        strings[5] = "packet \"parser secret\" PARSER_SECRET_ ";
        strings[6] = "\"string beginning with quoted text \" secret\" PARSER_SECRET_ ";
        strings[7] = "\"  string  beginning  with  quoted text \" another_word words";

        for ( int h = 0; h < 4; h++) {
            switch ( h ) {
                case 0:
                    System.out.println(Ansi.bBlue("Regular test"));
                    break;
                case 1:
                    System.out.println(Ansi.bBlue("Condense whitespaces within quoted strings"));
                    paramLine.setCondenseAll(true);
                    break;
                case 2:
                    System.out.println(Ansi.bBlue("Ignore quoted params, just break by whitespaces (who'll need that?)"));
                    paramLine.setIgnoreQuotes(true);
                    break;
                case 3:
                    System.out.println(Ansi.bBlue("Trim all quoted parameters"));
                    paramLine.setIgnoreQuotes(false)
                            .setCondenseAll(false)
                            .setTrimAllQuotedParams(true);
                    break;
            }
            for (int i = 0; i < strings.length; i++) {
                System.out.println("String " + i + " " + Ansi.green(strings[i]) + " :");
                String[] words = paramLine.parse(strings[i]);
                for (int j = 0; j < words.length; j++) {
                    System.out.print("PARAM " + (j + 1) + ": " + Ansi.bold(words[j]) + "; ");
                }
                System.out.println();
            }
        }

    }
}
