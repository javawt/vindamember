package com.songlanyun.jymall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfigs {

   /* // 商户ID
    public String appid = "2019110668859000";
    // 私钥
    public String rsa_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYrSFeHqGzgEXRV6yaESzlBCX9QfPVYM/k6xk8fHj4Irf2hoQ00ooH0hDrx1nMStxS3iLIe+5jCtuqFvv1P2nQVYXDshLS1wHErnbV3jp9JQAWFPPPLNufyXD5AiHh/28I6T1oc6QzfYwBT4OhAxzK3b7UVzHI4kgpi5tw7S74YUIC/G81H+WprlAQpk7e5BqNCTrdWbG9L5wZCqDpmJNYnjz9khusefZuoKSREWRKjtr6RW6Et4W8oCbpFSPl6uuLeuvu9LqKd9UfN7YWaz/I8Bhi6gLNycsndzzzu1QmzA7unSHJzTaDi+N1NIPUgOJFpn9MpxOveaMonW8+j0jlAgMBAAECggEAX3VgN4jPcXvyK0G2GV5Dw60kU4S4/+DYGzkjPj5VJFAQ1YKlhqFczCb7jW/HJYRo3ScmeFOEaUiQr3hPcLeFYU0XpZPhURI9eMvcYTvhNf28YBIZnp9dcjpw+D/tD3C168xpKLWlJMyKjQc6NaHMjsq2Ve1uoztYAyHWvnSNuZ2eXeBDdL/6MZJyNUEO72cVyG6bn9iCIgCiJsxYjISI+p/se8koJZtFNj32N0QZxlSFPsjkJzHTkjPmtapEIydpdbKC97vUG9daqj8kLP4K1sWSjRg3erZA7T7YiCEj6JywrgR2j8DauPL9+qBMuIRiPTBC80+KZ/x4Z8eP8dv2AQKBgQDMR08w3I8bvPfEthG/BE6M4+YJgGr4hRP+wcDkoIVenmkgLQnfUuozMXDsCXIchzeatW1mLrha1cRXsDGK7DaQlw9pRmK39jb5xIhsaXtUClPDeMxXKGLcG2YstHAdWGFl4BiBpgID1d173ioZ4MPgSVR6ETOjwyNos/No/dyGHQKBgQC/VSAXIUQGfccDqKEhfnuH90eNovq02u1Ohnqe13AJDLg3h+zfMafit55mVEcbtw5oO48khj2Tb4772FIX14uxPTd5/3GmETueRKHNjFYINA/to2BQ3P6aDveJjmcHKfolcPKN0hRNznuUyYgebS8YkSWJdVAQtp1l6YTKas6zaQKBgQCwTbMpJpJUjEkC+2hhheQNcPuuo6ivCJvzOWGzcF7rBKkSXgyxM6xxtnARJy3XNVRmL8NVd2kg7HHihGydy7RkIhvo7hhxUQytkFN1zavlImycvfsOFgsMkJEkcC57BTY6aKh97Lq8eK4JI/sODpC/WEqSd5or8lTzHqwrpz3WeQKBgHkJrfJT7ge5O9P5sm1Q6W7bJN3aE4Wj1WnOsvWXTl27OTKP/242qIFJLaCIgi9rP2b6/pPZhEynzylM8Kn6hnmKQ3Ue/tNSpYd63Wz98VLhNI5dw0mEdGAbRcc0WcLzcDaNXDQRiRyrmgxG7aBp69g7wiZSG2NnUu0sq/+tT7O5AoGASXZ0oUzqmJ+t9TY8GyT20FtMgy+l++3muGXml64Ic8vt0GYogxVrlvMC1vJ8YEtmBieWA66ZlPHJodEK9iHT/kL0v9gPEzSQpOwPOWCMKjgykXyp8QjxNtNUuiJr158YdVGxFwdvzTKwO/C+tZ9Z4RozSOfWKp60CvnILTjnbKc=";
    // 请求网关地址
    public String gateway_url = "https://openapi.alipay.com/gateway.do";
    // 编码
    public String charset = "UTF-8";
    // 返回格式
    public String format = "json";
    // 支付宝公钥
    public String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs8HJneUp8BcAbf81QVTm1L2xlFOFGwU3P0rCb6XTAw7scSdSYdDX5QFzvH3MAMaJ1rPqzLjUDBAApheuM0Ph3tNwY/SGVuL+rRJHkGam0BPxHzkxMlNx56su1TTZVFmvjFAjFYKrguGCo9rrNljNaxkg/6z6BGj0+P4IfaFgv7AN0rm04EEyifV6mbolBVsYykSdAuJuOqkwd4jTcc2PzZznaknkLTJn59lbd0qdN0nJrGC8gwbRSS/Xlno2Yetk7n207Lur7Uy7KuBoiiPIuNphdCwUZDoJA6oPJj3t2PrwReyBwXg5lB6vOijuJBzTVSixxazdVc8hWoD6lOudYwIDAQAB";
    // RSA2
    public String signtype = "RSA2";

    public String alipay_callback = "http://deve.songlanyun.com/xlg/order/alipay_callback";

    public String agent_success = "http://deve.songlanyun.com/xlg/sysUserAgent/agentSuccess";*/

    // 商户ID ${spring.redis.host}
    @Value("${alipay.ali_appid}" )
    public   String appid = "2019110668859000";
    // 私钥
    @Value("${alipay.ali_rsa_private_key}")
    public   String rsa_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYrSFeHqGzgEXRV6yaESzlBCX9QfPVYM/k6xk8fHj4Irf2hoQ00ooH0hDrx1nMStxS3iLIe+5jCtuqFvv1P2nQVYXDshLS1wHErnbV3jp9JQAWFPPPLNufyXD5AiHh/28I6T1oc6QzfYwBT4OhAxzK3b7UVzHI4kgpi5tw7S74YUIC/G81H+WprlAQpk7e5BqNCTrdWbG9L5wZCqDpmJNYnjz9khusefZuoKSREWRKjtr6RW6Et4W8oCbpFSPl6uuLeuvu9LqKd9UfN7YWaz/I8Bhi6gLNycsndzzzu1QmzA7unSHJzTaDi+N1NIPUgOJFpn9MpxOveaMonW8+j0jlAgMBAAECggEAX3VgN4jPcXvyK0G2GV5Dw60kU4S4/+DYGzkjPj5VJFAQ1YKlhqFczCb7jW/HJYRo3ScmeFOEaUiQr3hPcLeFYU0XpZPhURI9eMvcYTvhNf28YBIZnp9dcjpw+D/tD3C168xpKLWlJMyKjQc6NaHMjsq2Ve1uoztYAyHWvnSNuZ2eXeBDdL/6MZJyNUEO72cVyG6bn9iCIgCiJsxYjISI+p/se8koJZtFNj32N0QZxlSFPsjkJzHTkjPmtapEIydpdbKC97vUG9daqj8kLP4K1sWSjRg3erZA7T7YiCEj6JywrgR2j8DauPL9+qBMuIRiPTBC80+KZ/x4Z8eP8dv2AQKBgQDMR08w3I8bvPfEthG/BE6M4+YJgGr4hRP+wcDkoIVenmkgLQnfUuozMXDsCXIchzeatW1mLrha1cRXsDGK7DaQlw9pRmK39jb5xIhsaXtUClPDeMxXKGLcG2YstHAdWGFl4BiBpgID1d173ioZ4MPgSVR6ETOjwyNos/No/dyGHQKBgQC/VSAXIUQGfccDqKEhfnuH90eNovq02u1Ohnqe13AJDLg3h+zfMafit55mVEcbtw5oO48khj2Tb4772FIX14uxPTd5/3GmETueRKHNjFYINA/to2BQ3P6aDveJjmcHKfolcPKN0hRNznuUyYgebS8YkSWJdVAQtp1l6YTKas6zaQKBgQCwTbMpJpJUjEkC+2hhheQNcPuuo6ivCJvzOWGzcF7rBKkSXgyxM6xxtnARJy3XNVRmL8NVd2kg7HHihGydy7RkIhvo7hhxUQytkFN1zavlImycvfsOFgsMkJEkcC57BTY6aKh97Lq8eK4JI/sODpC/WEqSd5or8lTzHqwrpz3WeQKBgHkJrfJT7ge5O9P5sm1Q6W7bJN3aE4Wj1WnOsvWXTl27OTKP/242qIFJLaCIgi9rP2b6/pPZhEynzylM8Kn6hnmKQ3Ue/tNSpYd63Wz98VLhNI5dw0mEdGAbRcc0WcLzcDaNXDQRiRyrmgxG7aBp69g7wiZSG2NnUu0sq/+tT7O5AoGASXZ0oUzqmJ+t9TY8GyT20FtMgy+l++3muGXml64Ic8vt0GYogxVrlvMC1vJ8YEtmBieWA66ZlPHJodEK9iHT/kL0v9gPEzSQpOwPOWCMKjgykXyp8QjxNtNUuiJr158YdVGxFwdvzTKwO/C+tZ9Z4RozSOfWKp60CvnILTjnbKc=";
    // 请求网关地址
    @Value(("${alipay.ali_gateway_url}"))
    public   String gateway_url="https://openapi.alipay.com/gateway.do";
    // 编码
    @Value("${alipay.ali_charset}")
    public   String charset = "UTF-8";
    // 返回格式
    @Value("${alipay.ali_format}")
    public   String format = "json";
    // 支付宝公钥
    @Value("${alipay.ali_pay_public_key}")
    public   String alipay_public_key ;
    //回调
    @Value("${alipay.alipay_callback}")
    public   String alipay_callback  ;

    @Value("${alipay.ali_signtype}")
    public   String signtype = "RSA2";
}