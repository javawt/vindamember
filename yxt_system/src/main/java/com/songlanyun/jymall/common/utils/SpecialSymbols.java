package com.songlanyun.jymall.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialSymbols {

    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ `~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
    public static boolean isSpecialCharMH(String str) {
        String regEx1 = "%";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 邮箱正则
     *
     * */
    public static boolean isEmail(String email){
        String reg = "^([a-z0-9A-Z_]+[-|\\.]?)+[a-z0-9A-Z_]@([a-z0-9A-Z_]+(-[a-z0-9A-Z_]+)?\\.)+[a-zA-Z]{2,}$";
        if (StringUtils.isBlank(email)){
            return false;
        }else{
            if(email.length()<=50){
                return email.matches(reg);
            }else{
                return false;
            }
        }

    }
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String s2="^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$";// 验证手机号
        if(StringUtils.isNotBlank(str)){
            p = Pattern.compile(s2);
            m = p.matcher(str);
            if(m.matches()){
                b = true;
            }
        }
        return b;
    }


    //处理百分号下划线全局查询bug
    public static String queryString(String temp) {
        if (temp == null || temp == "") {
            return "";
        }
        StringBuilder b = new StringBuilder();
        b.append(temp.trim());
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append(temp.trim());
        for (int i = 0, j = 0; i < b.length(); i++) {
            char x = b.charAt(i);
            if (x == '%' || x == '_') {
                tempBuilder.insert(i + j, '\\');
                j++;
            }
        }
        return tempBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("188322"));
        //System.out.println(isSpecialCharMH("123_@#%"));
        //System.out.println(isEmail("89131_23@q_q.com"));
        //System.out.println(queryString("89_%%"));
        //System.out.println("是正确格式的手机号:"+isEmail("188072631727@163.com"));
    }

}
