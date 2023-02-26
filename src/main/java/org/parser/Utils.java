package org.parser;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static final String JsonDelimiterValue = ": ";
    public static final char openBracket = '{', closeBracket = '}';
    public static final String newLine = "\n";
    public static final char comma = ',';
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

    public static String addIndent(String str) {
        StringBuilder builder = new StringBuilder(str);
        List<Character> chars = CharBuffer.wrap(str).chars().mapToObj(ch -> (char) ch).collect(Collectors.toList());
        int countOpenBracket = 0;
        int add = 0;
        char[] ident = {};
        for (int i = 0; i < chars.size(); i++) {
            Character character = chars.get(i);
            // Вставлять после открывающих скобок отступы
            if (character == openBracket || character == openSquareBracket) {
                ++countOpenBracket;
                ident = Utils.repeatIdent(countOpenBracket, 1);
                int skip = add + i + 2;
                builder.insert(skip, ident);
                add += ident.length;
                // Всавлять после запятых пробелы
            } else if (character == comma) {
                int skip = add + i + 2;
                builder.insert(skip, ident);
                add += ident.length;
                // Вставлять после закрывающих скобок отступы и уменьшать счетчик отступов
            } else if (character == closeBracket || character == closeSquareBracket) {
                --countOpenBracket;
                ident = Utils.repeatIdent(countOpenBracket, 1);
                int skip = add + i;
                builder.insert(skip, ident);
                add += ident.length;
            }
        }
        builder.trimToSize();
      return  builder.toString();
    }
}
