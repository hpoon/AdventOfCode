package com.aoc.y2015;

import com.aoc.ProblemDay;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProblemDay8 extends ProblemDay<Integer, Integer> {

    @Override
    protected Integer solveA() {
        List<String> literals = parse();
        int answer = 0;
        for (String literal : literals) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < literal.length(); i++) {
                char c1 = literal.charAt(i);
                switch (c1) {
                    case '"':
                        continue;
                    case '\\':
                        char c2 = literal.charAt(i+1);
                        switch (c2) {
                            case '\"':
                                sb.append('\"');
                                i++;
                                break;
                            case '\\':
                                sb.append('\\');
                                i++;
                                break;
                            case 'x':
                                sb.append(replaceHex(literal.substring(i, i+4)));
                                i+=3;
                                break;
                        }
                        break;
                    default:
                        sb.append(c1);
                }
            }
            answer += literal.length() - sb.toString().length();
        }
        return answer;
    }

    @Override
    protected Integer solveB() {
        List<String> literals = parse();
        int answer = 0;
        for (String literal : literals) {
            StringBuilder sb = new StringBuilder();
            sb.append('\"');
            for (int i = 0; i < literal.length(); i++) {
                char c1 = literal.charAt(i);
                switch (c1) {
                    case '\"': sb.append("\\\""); break;
                    case '\\': sb.append("\\\\"); break;
                    default: sb.append(c1); break;
                }
            }
            sb.append('\"');
            answer += sb.toString().length() - literal.length();
        }
        return answer;
    }

    private String replaceHex(String literal) {
        Pattern pattern = Pattern.compile("\\\\x[a-z0-9]{2}");
        Matcher matcher = pattern.matcher(literal);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group();
            int num = Integer.parseInt(hex.replace("\\x", ""), 16);
            char bin = (char) num;
            matcher.appendReplacement(sb, bin+"");   // replace hex with char
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private List<String> parse() {
        List<String> literals = new ArrayList<>();
        while (scanner.hasNextLine()) {
            literals.add(scanner.nextLine());
        }
        return literals;
    }

}
