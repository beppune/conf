package it.posteitaliane.dckli

import java.io.InputStream

typealias Token = Pair<CharType, Char?>

fun token(t:CharType, c:Char?=null) = Token(t, c)

enum class CharType{
    WS, EOF, NL, ALPHA, DIGIT, SQ_OPEN, SQ_CLOSE, SEMI_COLON, DOUBLE_QUOTE, EQUALS, CR, CONTROL, PRINTABLE
}

class Lexer(private val stream:InputStream) {

    fun lex(): Token {

            val code = stream.read()
            if( code == -1 ) return token(CharType.EOF)

            val char =  Char(code)

            if( char == '\n' ) return  token(CharType.NL)
            if( char == '\r' ) return  token(CharType.CR)

            if( char == '[' ) return token(CharType.SQ_OPEN)
            if( char == ']' ) return token(CharType.SQ_CLOSE)

            if( char == ';' ) return token(CharType.SEMI_COLON)

            if( char == '"') return token(CharType.DOUBLE_QUOTE)

            if( char == '=') return token(CharType.EQUALS)

            if( char.isWhitespace() ) return token(CharType.WS, char)

            if(char.isLetter()) return token(CharType.ALPHA, char)

            if( char.isDigit() ) return token(CharType.DIGIT, char)

            if( char.isISOControl() ) return token(CharType.CONTROL, char)

            return token(CharType.PRINTABLE, char)
    }

}

class InitParser(stream:InputStream) {
    private val lexer:Lexer

    private val builder:StringBuilder

    private var tok:Token

    init {
        lexer = Lexer(stream)
        builder = StringBuilder()
        tok = lexer.lex()
    }

    private fun match(t:CharType): Boolean {
        if( t == tok.first ) {
            builder.append(tok.second)
            fw()
            return true
        }
        return false
    }

    private fun fw()  { tok = lexer.lex() }

    private fun skipws() {
        while(tok.first == CharType.WS) {
            fw()
        }
    }

    fun key() {
        builder.clear()
        while (match(CharType.ALPHA) || match(CharType.DIGIT)) {}
        println("KEY: \"$builder\"")
    }

    fun value() {
        builder.clear()
        while (match(CharType.ALPHA) || match(CharType.DIGIT) || match(CharType.PRINTABLE)) {}
        println("VALUE: \"$builder\"")
    }

    fun parse() {

        //skip all white spaces
        skipws()

        //key
        key(); match(CharType.EQUALS); value(); match(CharType.CR); match(CharType.NL)

    }
}
