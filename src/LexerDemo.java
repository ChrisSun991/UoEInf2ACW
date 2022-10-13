import java.io.* ;
/*
class LexerDemo {

    public static void main (String[] args)
            throws StateOutOfRange, IOException {
        BufferedReader consoleReader = new BufferedReader (new InputStreamReader (System.in)) ;
        while (0==0) {
            System.out.print ("Lexer> ") ;
            String inputLine = consoleReader.readLine() ;
            Reader lineReader = new BufferedReader (new StringReader (inputLine)) ;
            GenLexer demoLexer = new MH_Lexer (lineReader) ;
            try {
                LexToken currTok = demoLexer.pullProperToken() ;
                while (currTok != null) {
                    System.out.println (currTok.value() + " \t" +
                            currTok.lexClass()) ;
                    currTok = demoLexer.pullProperToken() ;
                }
            } catch (LexError x) {
                System.out.println ("Error: " + x.getMessage()) ;
            }
        }
    }
}
*/