package j2script;

import j2script.tokens.*;
import j2script.expressions.*;
import j2script.operators.*;

import java.util.Map;
import java.util.HashMap;

public class Parser 
{
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
    
    // begin instance variables
    private final Token[] tokens;
    // end instance variables

    public Parser(final Token[] tokens) {
        this.tokens = tokens;
    }

    private Token getToken(final int pos) throws ParserException {
        assert(pos >= 0);
        if (pos < tokens.length) {
            return tokens[pos];
        } else {
            throw new ParserException("No token at position " + pos);
        }
    }
    /*******************************************************/
    private class ParseResult<A> //The result of a parse.
    {
        public final A result;
        public final int tokenPos;
        public ParseResult(final A result,
                           final int tokenPos) {
            this.result = result;
            this.tokenPos = tokenPos;
        }
    } // ParseResult

    /*******************************************************/
    // handles something (op something)*
    private abstract class ParseBinop //
    {
        private final Map<Token, Op> opMap;
        public ParseBinop(final Map<Token, Op> opMap) {
            this.opMap = opMap;
        }

        public abstract ParseResult<Exp> parseSomething(final int startPos) throws ParserException;

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
                    finalResult = new ParseResult<Exp>(new BinaryExp(finalResult.result,
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
    /*******************************************************/
    private class ParseAdditive extends ParseBinop {
        public ParseAdditive() {
            super(ADDITIVE_OP_MAP);
        }

        public ParseResult<Exp> parseSomething(final int startPos) throws ParserException {
            return parseMultiplicative(startPos);
            //=>ParseMultiplicative().parse(startPos)=>parseSomething(0)=>parsePrimary()wtf
        }
    }//Parse Additive
    /*******************************************************/
    private class ParseMultiplicative extends ParseBinop {
        public ParseMultiplicative() {
            super(MULTIPLICATIVE_OP_MAP);
        }

        public ParseResult<Exp> parseSomething(final int startPos) throws ParserException {
            return parsePrimary(startPos);
        }
    }//ParseMultiplicative
    /*******************************************************/
    public Exp parseExp() throws ParserException 
    {
        final ParseResult<Exp> result = parseExp(0); //Begin parsing at startPos = 0
        if (result.tokenPos == tokens.length) {
            return result.result;
        } else {
            throw new ParserException("Extra tokens starting at " + result.tokenPos);
        }
    }
    //
    private ParseResult<Exp> parseExp(final int startPos) throws ParserException 
    {
        return parseAdditive(startPos);
    }
    //
    private ParseResult<Exp> parseAdditive(final int startPos) throws ParserException {
        return new ParseAdditive().parse(startPos);
    }
    //
    private ParseResult<Exp> parseMultiplicative(final int startPos) throws ParserException 
    {
        return new ParseMultiplicative().parse(startPos);
    }
    //
    private ParseResult<Exp> parseNumber(final int startPos) throws ParserException 
    {
        final Token current = getToken(startPos);
        if (current instanceof NumberToken) {
            return new ParseResult<Exp>(new NumberExp(((NumberToken)current).number),
                                        startPos + 1);
        } else {
            return null;
        }
    }
    //
    private void assertTokenAtPos(final Token token, final int pos) throws ParserException 
    {
        if (!getToken(pos).equals(token)) {
            throw new ParserException("Expected " + token.toString() + " at pos " + pos);
        }
    }
    //
    private ParseResult<Exp> parsePrimary(final int startPos) throws ParserException {
        final Token current = getToken(startPos);
        Exp resultExp;
        int resultPos;

        if (current instanceof NumberToken) 
        {
            resultExp = new NumberExp(((NumberToken)current).number);
            resultPos = startPos + 1;
        } 
        else if (current instanceof VariableToken) 
        {
            resultExp = new VariableExp(((VariableToken)current).name);
            resultPos = startPos + 1;
        } 
        else if (current instanceof MinusToken) 
        {
            //look for a number 
            final ParseResult<Exp> nested = parsePrimary(startPos + 1);
            resultExp = new UnaryMinusExp(nested.result);
            resultPos = nested.tokenPos;
        } 
        else if (current instanceof LeftParenToken) 
        {
            final ParseResult<Exp> nested = parseExp(startPos + 1);
            assertTokenAtPos(new RightParenToken(), nested.tokenPos);
            resultExp = nested.result;
            resultPos = nested.tokenPos + 1;
        } 
        else if (current instanceof IfToken) 
        {
            assertTokenAtPos(new LeftParenToken(), startPos + 1);
            final ParseResult<Exp> guard = parseExp(startPos + 2);
            assertTokenAtPos(new RightParenToken(), guard.tokenPos);
            assertTokenAtPos(new LeftCurlyToken(), guard.tokenPos + 1);
            final ParseResult<Exp> ifTrue = parseExp(guard.tokenPos + 2);
            assertTokenAtPos(new RightCurlyToken(), ifTrue.tokenPos);
            assertTokenAtPos(new ElseToken(), ifTrue.tokenPos + 1);
            assertTokenAtPos(new LeftCurlyToken(), ifTrue.tokenPos + 2);
            final ParseResult<Exp> ifFalse = parseExp(ifTrue.tokenPos + 3);
            assertTokenAtPos(new RightCurlyToken(), ifFalse.tokenPos);
            resultExp = new IfExp(guard.result, ifTrue.result, ifFalse.result);
            resultPos = ifFalse.tokenPos + 1;
        } 
        else 
        {
            throw new ParserException("Expected primary at " + startPos);
        }

        return new ParseResult<Exp>(resultExp, resultPos);
    } // parsePrimary
} // Parser