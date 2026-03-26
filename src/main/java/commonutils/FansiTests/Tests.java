package commonutils.FansiTests;

import commonutils.Fansi;

public class Tests {
    public static void testBatch1() {
        Fansi out = new Fansi();
        out.setBackground(Fansi.BG_BLUE).setColor(Fansi.FG_RED)
                .render("Hello");
        System.out.println();
        for ( int fCol = 30; fCol < 38; fCol++) {
            for ( int bCol = 40; bCol < 49; bCol++ ) {
                for ( int eCol = 0; eCol < 10; eCol++ ) {
                    out.setColor(Integer.toString(fCol)).setBackground(Integer.toString(bCol)).setEffect(Integer.toString(eCol)).render("HI ");
                }
                System.out.println();
            }
            System.out.println();
        }

        System.out.println(Fansi.create()
                .append("Hello, ").bold("User").normal().append(". How are you?").nl()
                .append("We have something to add ").italic().append("(in italics!)").normal().nl()
                .bold("We have something to add in bold").nl()
                .italic("We have something to add in italics").nl()
                .append("We have something to add").nl()

                .blue().append("Hello, ").bold("User").normal().append(". How are you?").nl()
                .append("We have something to add ").italic().append("(in italics!)").normal().nl()
                .bold("We have something to add in bold").black().nl()
                .italic("We have something to add in italics").nl()
                .append("We have something to add").nl()

                .c256("155").append("Fancy color!").bc256("34").append("on a fancy background!").reset().nl()
                .RGB(0,90,40).append("RGB!").bRGB(200,255,240).append("on an RGB background!").reset().nl()

                .RGB(190, 180, 90).bold("bold RGB").append(" another text?").normal(" hopefully...").nl()


                .append("Just normal text... with a previous color").nl().

                b().append("Some nice shortcuts!").n().nl().
                i().append("Italics").n().nl().
                u().append("Underline").n().nl().
                s().append("Sorry!!!").n().nl().
                bsu().append("REAALLY SORRY!!!").n().nl()
                .gradientFg("My texty text", "30;50;80", "170;70;90").nl()
                .b().gradientBg("My texty text in bold", "200;80;30", "0;200;90").n().nl()
                .bu().gradientBoth("My underlined texty text in bold and in both gradients", "0;255;200", "150;10;55", "0;0;0", "170;180;190").n().nl()
                .append("That's all, folks!").b().append("Really the end!")
                .render());
        Fansi.create().append("Ho ho! Hellooooo again!").italic().println();
        Fansi.noReset(Fansi.NO_RESET).append("Hellooooo again!").println();
        Fansi.create("Last hello").println();
        Fansi.create().tab().append( "Custom").println();
        Fansi.create().append("Custom 1").print();

    }
    public static void main(String[] args) {
        testBatch1();
        String s = Fansi.create().gradientFg("String for a gradient","20;200;160", "200;20;80").reset().render();
        System.out.println(s);
    }

}
