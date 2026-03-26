package commonutils.menudispatcher;

import commonutils.menudispatcher.Message;

public class TestFunctions {

        public Message isInt (String msg) {
            boolean error = false;
            try {
                Integer.parseInt(msg);
            } catch (Exception e) {
                error = true;

            }
            return new Message<String> ( "" + msg,"println", error );
        }
        public Message action(String msg) {
            return new Message( "This is truly a success, as you gave us an integer: " + msg);
        }
        public Message fail (String msg ) {
            return new Message("Unfortunately, '" + msg + "' is not an integer");
        }
}
