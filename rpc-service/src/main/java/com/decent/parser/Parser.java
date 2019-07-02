package com.decent.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.util.Logger;
import reactor.util.Loggers;

/* This program illustrates recursive descent parsing using a
   pure procedural approach.

   The grammar:

   statement = { expression  ";" } "."
   expression = term { ( "+" | "-" ) term }
   term      = factor { ( "*" | "/" ) factor }
   factor    = number | "(" expression ")"

*/
@Component
public class Parser {
    private static final Logger logger = Loggers.getLogger(Parser.class);

    @Autowired
    private Scanner scanner;

    public String parse(String parseString) {

        if (isAlpha(parseString)) {
            return "The given input is invalid " + parseString;
        }
        InputStream stream = new ByteArrayInputStream(parseString.getBytes(StandardCharsets.UTF_8));
        DataInputStream dataInputStream = new DataInputStream(stream);
        scanner.buffer = new Buffer(dataInputStream);
        scanner.token = Token.semicolon;
        scanner.getToken();
        return statement();
    } // run

    private String statement() {
        int value = 0;
        // statement = { expression ";" } "."
        while (scanner.token != Token.period) {
            value = expression();
            scanner.getToken(); // flush ";"
        } // while
        return Integer.toString(value);
    } // statement

    private int expression() {
        // expression = term { ( "+" | "-" ) term }
        int left = term();
        while (scanner.token == Token.plusop || scanner.token == Token.minusop) {
            int saveToken = scanner.token;
            scanner.getToken();
            switch (saveToken) {
                case Token.plusop:
                    left += term();
                    break;
                case Token.minusop:
                    left -= term();
                    break;
            } // switch
        } // while
        return left;
    } // expression

    private int term() {
        // term = factor { ( "*" | "/" ) factor }
        int left = factor();
        while (scanner.token == Token.timesop || scanner.token == Token.divideop) {
            int saveToken = scanner.token;
            scanner.getToken();
            switch (saveToken) {
                case Token.timesop:
                    left *= factor();
                    break;
                case Token.divideop:
                    left /= factor();
                    break;
            } // switch
        } // while
        return left;
    } // term

    private int factor() {
        // factor = number | "(" expression ")"
        int value = 0;
        switch (scanner.token) {
            case Token.number:
                value = scanner.number();
                scanner.getToken(); // flush number
                break;
            case Token.lparen:
                scanner.getToken();
                value = expression();
                if (scanner.token != Token.rparen)
                    scanner.error("Missing ')'");
                scanner.getToken(); // flush ")"
                break;
            default:
                scanner.error("Expecting number or (");
                break;
        } // switch
        return value;
    } // factor

    public static boolean isAlpha(String s) {
        return s != null && s.matches(".*[a-zA-Z]+.*");
    }

} 
