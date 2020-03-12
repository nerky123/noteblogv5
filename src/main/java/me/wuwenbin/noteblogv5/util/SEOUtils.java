package me.wuwenbin.noteblogv5.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author yangxw
 * @describition
 * @create 2020-03-05 21:12
 */
@Slf4j
public class SEOUtils {

    //推送到百度
    public static void sendToBaiDu(String articleId){

        //调用百度站长平台工具
        try{
            HttpURLConnection conn = (HttpURLConnection)(new URL("http://data.zz.baidu.com/urls?site=https://www.lingpojie.com&token=mBfgUjBOCCQGqk86")).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.getOutputStream().write(("https://www.lingpojie.com/article/"+articleId).getBytes(Charset.forName("UTF-8")));

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;) {
                sb.append((char)c);
            }
            in.close();
            conn.disconnect();

            String responseStr = sb.toString();
            log.error("RequestUtils - responseStr <== " + responseStr);
        }catch (Exception e){
            log.error("调用百度站长接口出错，具体原因："+e.toString());
        }
    }
}
