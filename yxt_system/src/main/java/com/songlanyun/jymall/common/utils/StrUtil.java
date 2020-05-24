package com.songlanyun.jymall.common.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//String 工具类
public class StrUtil {
    //判断是否为空
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

    //计算日期+天数并返回string类型时间
    public static String plusDay(int num, String newDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currdate = format.parse(newDate);
        System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        return enddate;
    }

    /**
     * @return
     */
    public static String getNow() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getNowToday() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd");
    }

    /**
     * @return 生成订单号
     */
    public static String getOrderNo() {
        String first = RandomStringUtils.randomNumeric(2);
        String end = RandomStringUtils.randomNumeric(4);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String newDate = sdf.format(new Date());
        return first + newDate + end;
    }

    /**
     * 判断时间先后 time1为新时间为true time2为新为false
     */
    public static boolean timeCompare(String time1, String time2) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime1 = df.parse(time1);
        Date dateTime2 = df.parse(time2);
        if (dateTime1.getTime() >= dateTime2.getTime()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Object转换BigDecimal
     */
    public static BigDecimal getBigDecimal( Object value ) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger ) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = new BigDecimal( ((Number)value).doubleValue() );
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }
    /**
     * 将request里面的参数转换成map
     * @param request
     * @return
     */
    public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }
}
