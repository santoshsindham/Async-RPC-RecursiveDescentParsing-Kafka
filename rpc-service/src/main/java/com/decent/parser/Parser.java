package com.decent.parser;

import org.springframework.stereotype.Component;

/*
Grammar:
E -> x + T
T -> (E)
T -> x

 Sample Inputs:
- xxx: invalid
- x+x+x: invalid
- x+(x+x): valid
- x+(x+(x+x)): valid
- xxx: invalid
- x+(x+x)+x: invalid
*/

@Component
public class Parser {
    static int ptr;
    static char[] input;

    public String parseInputString(String inputString) {

        String returnString = "";

        input = inputString.toCharArray();
        if (input.length < 2) {
            returnString = "The input string is invalid.";
            System.exit(0);
        }
       ptr = 0;
        boolean isValid = E();
        if ((isValid) & (ptr == input.length)) {
            returnString = "The input string is valid.";
        } else {
            returnString = "The input string is invalid.";
        }

        return returnString;
    }

    static boolean E() {
        // Check if 'ptr' to 'ptr+2' is 'x + T'
        int fallback = ptr;
        if (input[ptr++] != 'x') {
            ptr = fallback;
            return false;
        }
        if (input[ptr++] != '+') {
            ptr = fallback;
            return false;
        }
        if (T() == false) {
            ptr = fallback;
            return false;
        }
        return true;
    }

    static boolean T() {
        // Check if 'ptr' to 'ptr+2' is '(E)' or if 'ptr' is 'x'
        int fallback = ptr;
        if (input[ptr] == 'x') {
            ptr++;
            return true;
        } else {
            if (input[ptr++] != '(') {
                ptr = fallback;
                return false;
            }
            if (E() == false) {
                ptr = fallback;
                return false;
            }
            if (input[ptr++] != ')') {
                ptr = fallback;
                return false;
            }
            return true;
        }
    }
} 
