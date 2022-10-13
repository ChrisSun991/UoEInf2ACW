
// File:   MH_Lexer.java

// Java template file for lexer component of Informatics 2A Assignment 1.
// Concerns lexical classes and lexer for the language MH (`Micro-Haskell').


import java.io.* ;

class MH_Lexer extends GenLexer implements LEX_TOKEN_STREAM {

    static class VarAcceptor extends Acceptor implements DFA {
        // add code here
        // small (#1) (small + large + digit + ')(#2)* (dead(#3))

        public String lexClass() {return "VAR" ;} ;

        public int numberOfStates() {return 3 ;} ;

        int next (int state, char c) {
            switch (state) {
                case 0: if (CharTypes.isSmall(c)) return 1 ; else return 2 ;
                case 1: if (CharTypes.isSmall(c) || CharTypes.isLarge(c) || CharTypes.isDigit(c) ||c == '\'') return 1 ; else return 2 ;
                default: return 2 ; // garbage state, declared "dead" below
            }
        }

        boolean accepting (int state) {return (state == 1) ;}
        int dead () {return 2 ;}
    }


    static class NumAcceptor extends Acceptor implements DFA {
        // add code here
        // 0 (#1)+ (nonZeroDigit(#1) x digit(#3))* (dead (#4))
        public String lexClass() {return "NUM" ;} ;
        public int numberOfStates() {return 4 ;} ;

        int next (int state, char c) {
            switch (state) {
                case 0: if (c == '0') return 1 ;else if('1' <= c && c <= '9') return 2; else return 3 ;
                case 2: if (CharTypes.isDigit(c)) return 2; else return 3 ;
                default: return 3 ; // garbage state, declared "dead" below
            }
        }

        boolean accepting (int state) {return (state == 2 || state == 1) ;}
        int dead () {return 3 ;}

    }

    static class BooleanAcceptor extends Acceptor implements DFA {
        // add code here
        // True (#1-4) + False (#1,5-8) (dead (#10)) (accept (#9))
        public String lexClass() {return "BOOLEAN" ;} ;
        public int numberOfStates() {return 10 ;} ;

        int next (int state, char c) {
            switch (state) {
                case 0: if (c == 'T') return 1 ; else if (c == 'F') return 4; else return 9 ;
                    //TRUE
                case 1: if (c == 'r') return 2 ; else return 9 ;
                case 2: if (c == 'u') return 3 ; else return 9 ;
                case 3: if (c == 'e') return 8 ; else return 9;
                    //FALSE
                case 4: if (c == 'a') return 5 ; else return 9 ;
                case 5: if (c == 'l') return 6 ; else return 9 ;
                case 6: if (c == 's') return 7 ; else return 9 ;
                case 7: if (c == 'e') return 8 ; else return 9 ;

                default: return 9 ; // garbage state, declared "dead" below
            }
        }

        boolean accepting (int state) {return (state == 8) ;}
        int dead () {return 9 ;}
    }

    static class SymAcceptor extends Acceptor implements DFA {
        // add code here
        // symbolic (#1) x symbolic(#2)* (dead(#3))
        public String lexClass() {return "SYM" ;} ;
        public int numberOfStates() {return 3 ;} ;

        int next (int state, char c) {
            switch (state) {
                case 0: if (CharTypes.isSymbolic(c)) return 1 ; else return 2 ;
                case 1: if (CharTypes.isSymbolic(c)) return 1 ; else return 2 ;
                default: return 2 ; // garbage state, declared "dead" below
            }
        }

        boolean accepting (int state) {return (state == 1) ;}
        int dead () {return 2 ;}
    }

    static class WhitespaceAcceptor extends Acceptor implements DFA {
        // add code here
        // Whitespace (#1) x whitespace* (#2) (dead(#3))
        public String lexClass() {return "" ;} ;
        public int numberOfStates() {return 3 ;} ;

        int next (int state, char c) {
            switch (state) {
                case 0: if (CharTypes.isWhitespace(c)) return 1 ; else return 2 ;
                case 1: if (CharTypes.isWhitespace(c)) return 1 ; else return 2 ;
                default: return 2 ; // garbage state, declared "dead" below
            }
        }

        boolean accepting (int state) {return (state == 1) ;}
        int dead () {return 2 ;}
    }

    static class CommentAcceptor extends Acceptor implements DFA {
        // add code here
        // ---*(nonSymbolNewline nonNewline* + epsilon)
        // definition of a new line : (c == '\r' || c == '\n' || c == '\f' )
        // definition of a non Symbol new line : !(isSymbol(c)) || !(isNewLine(c))
        // definition of a nonNewLine : !(isNewLine(c)
        // '\-' (#1) + '\-' )(#2)+ ('\-' (*) + nonSymbolNewLine) (#3) + nonNewLine (#4) + dead (#5) + accept (#6)

        public String lexClass() {return "" ;} ;
        public int numberOfStates() {return 5 ;} ;

        int next (int state, char c) {
            switch (state) {
                case 0: if (c == '-') return 1 ; else return  4;
                case 1: if (c == '-') return 2 ; else return  4;
                case 2: if (c == '-') return 2 ; else if (!(CharTypes.isSymbolic(c)) || !(CharTypes.isNewline(c))) return 3; else return 4;
                case 3: if (!(CharTypes.isNewline(c))) return 3;
                default: return 4 ; // garbage state, declared "dead" below
            }
        }

        boolean accepting (int state) {return (state == 3 || state == 2) ;}
        int dead () {return 4 ;}
    }

    static class TokAcceptor extends Acceptor implements DFA {

        String tok ;
        int tokLen ;

        TokAcceptor (String tok) {
            this.tok = tok ;
            tokLen = tok.length() ;
        }

        public String lexClass () {
            return tok;
        }

        // add code here
        public int numberOfStates() {
            return tokLen + 2; // 2 = accepting state + dead state
        }

        int next (int state, char c) {


            if ((state < tokLen) && (tok.charAt(state) == c)){ // in FSM, not dead or accepting
                return state + 1; //increment state
            } else{
                return tokLen + 1; // dead state
            }

        }

        boolean accepting (int state) {
            return (state == tokLen);
        }
        int dead () {
            return tokLen + 1;
        }
    }

    // add definitions of MH_acceptors here
    // Singleton classes
    static DFA integer = new TokAcceptor("Integer");
    static DFA bool = new TokAcceptor("bool");
    static DFA If = new TokAcceptor("if");
    static DFA Then = new TokAcceptor("then");
    static DFA Else = new TokAcceptor("else");
    //Three Special Symbols
    static DFA OpenBrac = new TokAcceptor("(");
    static DFA CloseBrac = new TokAcceptor(")");
    static DFA SemiColon = new TokAcceptor(";");
    //Acceptors
    static DFA varAcceptor = new VarAcceptor();
    static DFA numAcceptor = new NumAcceptor();
    static DFA boolAcceptor = new BooleanAcceptor();
    static DFA symAcceptor = new SymAcceptor();
    static DFA whiteSpaceAcceptor = new WhitespaceAcceptor();
    static DFA commentsAcceptor = new CommentAcceptor();

    static DFA[] MH_acceptors = new DFA[] {integer, bool, If, Then, Else, OpenBrac, CloseBrac, SemiColon,
            varAcceptor, numAcceptor, boolAcceptor, symAcceptor, whiteSpaceAcceptor, commentsAcceptor};

    MH_Lexer (Reader reader) {
        super(reader,MH_acceptors) ;
    }

}

