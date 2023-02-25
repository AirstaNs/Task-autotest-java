package org.parser;

import java.util.Arrays;

public class Utils {
    public static final String JsonDelimiterValue = ": ";
    public static final char openBracket = '{', closeBracket = '}';
    public static final String newLine = "\n";
    public static final String comma = ",";
    public static final char objDelimiterValue = '=';

    public static final String backets = "[{}]";
    public static final String emptyObj = "{\n\n}";
    public static final String emptyArray = "[\n]";
    public static final char openSquareBracket = '[', closeSquareBracket = ']';


    public static String repeatIdent(int count) {
        final char[] ident = {' ', ' '};
        byte[] bytes1 = new byte[ident.length * count];
        Arrays.fill(bytes1, (byte) ident[0]);
        return new String(bytes1);
    }

    public static char[] repeatIdent(int count, int count1) {
        final char[] ident = {' ', ' '};
        char[] chars = new char[ident.length * count];
        Arrays.fill(chars, ident[0]);
        return chars;
    }

    public static String wrapInQuotes(String str) {
        final char quote = '"';
        return quote + str + quote;
    }

    public static StringBuilder dividePairAndObj(StringBuilder obj) {
        obj.append(comma);
        obj.append(newLine);
        return obj;
    }

    public static StringBuilder removeLastComma(StringBuilder obj) {
        int index = obj.lastIndexOf(comma + "");
        obj.deleteCharAt(index);
        return obj;
    }

    public static StringBuilder removeLastNewLine(StringBuilder obj) {
        int index = obj.lastIndexOf(newLine);
        obj.deleteCharAt(index);
        return obj;
    }

    public static String wrapBracketsObj(String obj) {
//        StringBuilder insert = new StringBuilder(emptyObj).insert(2, obj);
//        insert.trimToSize();
        return openBracket + newLine + obj + newLine + closeBracket;
    }


}
