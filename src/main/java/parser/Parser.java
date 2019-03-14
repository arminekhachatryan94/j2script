package j2script;

import j2script.tokens.*;
import j2script.access.*;
import j2script.declarations.*;
import j2script.expressions.*;
import j2script.names.*;
import j2script.operators.*;
import j2script.statements.*;
import j2script.types.*;
import j2script.ParserException;
import java.util.*;

import java.util.Map;
import java.util.HashMap;

public class Parser {
    // begin static variables
    private static final Map<Token, Op> ADDITIVE_OP_MAP =
      new HashMap<Token, Op>() {{
          put(new AddToken(), new PlusOp());
          put(new MinusToken(), new MinusOp());
      }};
    private static final Map<Token, Op> MULTIPLICATIVE_OP_MAP =
      new HashMap<Token, Op>() {{
          put(new MultiplyToken(), new MultOp());
          put(new DivToken(), new DivOp());
      }};
    // end static variables

    //begin instance variables
    private final Token[] tokens;
    // end instance variables

    public Parser(final Token[] tokens) {
        this.tokens = tokens;
    }

    //The result of a parse.
    private class ParseResult<A> {
        public final A result; // AST node
        public final int tokenPos; // resulting position
        public ParseResult(final A result,
                           final int tokenPos) {
            this.result = result;
            this.tokenPos = tokenPos;
        }
    } // ParseResult

    // handles something (op something)*
    private abstract class ParseBinop {
        private final Map<Token, Op> opMap;
        public ParseBinop(final Map<Token, Op> opMap) {
            this.opMap = opMap;
        }

        public abstract ParseResult<Exp> parseSomething(final int startPos) throws ParserException;
        //
        public ParseResult<Exp> parse(final int startPos) throws ParserException 
        {
            int pos = startPos;
            ParseResult<Exp> finalResult = parseSomething(pos);
            if (finalResult == null)
                return null;            

            ParseResult<Exp> currentResult = null;
            while (finalResult.tokenPos < tokens.length) 
            {
                final Op op = opMap.get(getToken(finalResult.tokenPos));
                if (op != null) 
                {
                    // we have an op.  We MUST have a right; continue parsing.
                    final ParseResult<Exp> right = parseSomething(finalResult.tokenPos + 1);
                    finalResult = new ParseResult<Exp>(new BinopExp(finalResult.result,
                                                                     op,
                                                                     right.result),
                                                                     right.tokenPos);
                }
                else
                    // we don't have an op.  return whatever we have
                    return finalResult;
            }//end while there are tokens

            return finalResult;
        } // parse
    } // ParseBinop

    private class ParseAdditive extends ParseBinop {
        public ParseAdditive() {
            super(ADDITIVE_OP_MAP);
        }

        public ParseResult<Exp> parseSomething(final int startPos) throws ParserException {
            return parseMultiplicative(startPos);
            //=>ParseMultiplicative().parse(startPos)=>parseSomething(0)=>parsePrimary()wtf
        }
    }//Parse Additive

    private class ParseMultiplicative extends ParseBinop {
        public ParseMultiplicative() {
            super(MULTIPLICATIVE_OP_MAP);
        }

        public ParseResult<Exp> parseSomething(final int startPos) throws ParserException {
            return parsePrimary(startPos);
        }
    }//ParseMultiplicative

    private ParseResult<Exp> parseAdditive(final int startPos) throws ParserException {
        return new ParseAdditive().parse(startPos);
    }
    
    private ParseResult<Exp> parseMultiplicative(final int startPos) throws ParserException {
        return new ParseMultiplicative().parse(startPos);
    }

    private ParseResult<Exp> parsePrimary(final int startPos) throws ParserException {
        final Token current = getToken(startPos);
        Exp resultExp;
        int resultPos;

        if (current instanceof NumberToken) {
            resultExp = new NumberExp(((NumberToken)current).number);
            resultPos = startPos + 1;
        } 
        else if (current instanceof VariableToken) {
            resultExp = new VariableExp(((VariableToken)current).name);
            resultPos = startPos + 1;
        } 
        else {
            throw new ParserException("Expected primary at " + startPos);
        }

        return new ParseResult<Exp>(resultExp, resultPos);
    }

    // Gets the token at the given position
    private Token getToken(final int pos) throws ParserException {
        assert(pos >= 0);
        if (pos < tokens.length) {
            return tokens[pos];
        } else {
            throw new ParserException("No token at position " + pos);
        }
    }

    // Asserts that the given token appears at the given position
    private void assertTokenAtPos(final Token token, final int pos) throws ParserException {
        if (!getToken(pos).equals(token)) {
            throw new ParserException("Expected " + token.toString() + " at pos " + pos);
        }
    }
    private void ensureTokenIs(final int position, final Token expected) throws ParserException {
        final Token tokenHere = tokens[position];
        if (!expected.equals(tokenHere)) {
            throw new ParserException(expected.toString() + " expected at position: " + position);
        }
    }
    private boolean ensureToken(final int position, final Token expected) throws ParserException {
        final Token tokenHere = tokens[position];
        if (!expected.equals(tokenHere)) {
            return false;
        }
        else{
            return true;
        }
    }
    private boolean isSingleStmt(final List<Statement> stmt){
        //Single
        if (stmt.size() == 1){
            return true;
        }
        else{
            return false;
        }
    }

    /***************************************************
     Do not modify above lines unless adding static maps 
     ***************************************************/

    private ParseResult<Exp> parseExp(final int startPos) throws ParserException {
        return parseAdditive(startPos);
    }

    private ParseResult<Statement> parseStatement(final int startPos) throws ParserException {
        return null;
    }

    private ParseResult<MethodDef> parseMethodDef(final int startPos) throws ParserException {
        return null;
    }

    private ParseResult<InstanceDec> parseInstanceDec(final int startPos) throws ParserException {
        return null;
    }

    private ParseResult<ClassDef> parseClassDef(final int startPos) throws ParserException {
        return null;
    }

    private ParseResult<Program> parseProgram(final int startPos) throws ParserException {
        final Token tokenhere = tokens[startPos];
        Program resultProgram;
        List<ClassDef> classdefs;
        int resultpos=startPos;
        //If it is a variable token and that token is Class, this is a class def
        if (ensureToken(resultpos, new VariableToken("Class")) ){
            classdefs = new ArrayList<ClassDef>();
            // While there are more classes in the program, keep checking
            while(ensureToken(resultpos, new VariableToken("Class"))){
                final ParseResult<ClassDef> classDef = parseClassDef(resultpos);
                resultpos= classDef.tokenPos;
                classdefs.add(classDef.result);
            }
        }
        //check how many statements there are and create the program
        //Check if statement
        // else if ( tokenhere instanceof BooleanToken || tokenhere instanceof BreakToken
        //         || tokenhere instanceof IfToken || tokenhere instanceof IntToken
        //         || tokenhere instanceof PrintToken || tokenhere instanceof VariableToken 
        //         || tokenhere instanceof WhileToken  ){
        //     final ParseResult Statemnt = parseStatement(startPos);
        //     resultProgram = new Program(Statemnt.result);
        // }
        else {
            throw new ParserException("not a Class at pos: " + startPos);
        }
        final ParseResult Statemnt = parseStatement(resultpos);
        resultProgram = new Program(classdefs, (Statement)Statemnt.result);
        resultpos = Statemnt.tokenPos;
        return new ParseResult<Program>(resultProgram, resultpos);
    }
}