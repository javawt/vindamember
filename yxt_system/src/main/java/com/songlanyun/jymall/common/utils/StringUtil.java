package com.songlanyun.jymall.common.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: StringUtil
 * @Description: 字符处理工具
 * @author renlc
 * @date 2017年11月1日
 *
 */
public class StringUtil {

    /**
     * 
     * @Description 获取以非数字开头的32位uuid
     * @return String
     * @author xiaww
     * @date 2017年12月8日
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        if (Pattern.matches("^[0-9].*", uuid)) {
            return getUUID();
        } else {
            return uuid.replace("-", "");
        }

    }

    /**
     * @Description 将集合转为 ('a','b') 的形式，可用于sql in查询
     * @param items
     *            集合
     * @return
     */
    public static String surround(Collection<String> items) {
        return CollectionUtils.isEmpty(items) ? null
                : items.stream().map(e -> StringUtil.surround(e, "'")).collect(Collectors.joining(",", "(", ")"));
    }

    /**
     * @Title: surround
     * @Description: 字符串拼接上符号
     * @param text
     * @param symbol
     * @return String 返回类型
     */
    public static String surround(String text, String symbol) {
        return symbol + text + symbol;
    }

    /**
     * 字符两边加括号
     */
    public static Function<String, String> addBracket = (str) -> {
        return "(" + str + ")";
    };

    /**
     * @Title: buildInsertStatment
     * @Description: 生成mysql插入语句
     * @param tableName
     * @param header
     * @return String 返回类型
     */
    public static String buildInsertStatment(String tableName, String[] header) {
        StringJoiner insertJoiner = new StringJoiner(", ", "INSERT INTO `" + tableName + "` VALUES (", ");");
        IntStream.range(0, header.length).forEach(i -> insertJoiner.add("?"));
        return insertJoiner.toString();
    }

    /**
     * @Title: getDecodeJSONStr
     * @Description: 将奇数个转义字符变为偶数个
     * @param @param
     *            s
     * @param @return
     *            参数
     * @return String 返回类型
     */
    public static String getDecodeJSONStr(String s) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            switch (c) {
            case '\"':
                sb.append("\\\"");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @Title: getSubstr
     * @Description: 截取字段名称最后括号的内容，如果没有括号则返回null
     * @param @param
     *            str
     * @param @return
     *            参数
     * @return String 返回类型
     */
    public static String getSubstr(String str) {
        if (str.contains("(")) {
            if (str.lastIndexOf(")") == str.length() - 1) {
                String result = str.substring(str.lastIndexOf("(") + 1, str.length() - 1);
                return result;
            }
        }
        return null;
    }

    /**
     * @description 过滤查询参数
     * @param oldString
     * @return
     */
    public static String toSqlString(String oldString){
        if(!StringUtils.isEmpty(oldString)&&oldString.contains("_")){
            oldString = oldString.replaceAll("_", "*_");
            return oldString;
        }else if(!StringUtils.isEmpty(oldString)&&oldString.contains("%")){
            oldString = oldString.replaceAll("%", "*%");
            return oldString;
        }else{
            return oldString;
        }
    }

    /**
     * 正则获取指定字符之间的数据
     *
     * @param string 需要截取的字符串
     * @param regex  指定字符
     * @return
     */
    public static List<Object> accordingTheregularByRegular(String string, String regex) {
        List<Object> lists = new ArrayList<>();
        Pattern compile = Pattern.compile(regex + "(.*?)" + regex);
        Matcher matcher = compile.matcher(string);
        while (matcher.find()) {
            int i = 1;
            lists.add(matcher.group(i));
            i++;
        }
        return lists;
    }

    public static void main(String[] args) {
        System.out.println(getUUID());
    }
}
