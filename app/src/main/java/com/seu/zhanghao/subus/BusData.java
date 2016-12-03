package com.seu.zhanghao.subus;

import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhanghao7 on 2016/12/1.
 */

public class BusData {
    private  static final String URL_LINK ="http://www.szjt.gov.cn/apts/APTSLine.aspx";
    public  static  String getALlLines(String hrefString) throws IOException {
        String htmlString=null;
        URL url = new URL(URL_LINK);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        connection.setRequestMethod("POST");
       return  htmlString;
    }

    //调用queryRequest1(String lineName)后解析List内的链接，并将其传入到queryRequest2(String hrefString)中
    //返回查询结果
    public static List getBusInfo(String hrefString){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        String htmlString = doGet(hrefString);
        list=resolveHtml(htmlString);

        return list;
    }

    //执行一个HTTP GET请求，返回请求响应的HTML
    //url 请求的URL地址
    //queryString 请求的查询参数,可以为null
    //pretty是否美化
    //返回请求响应的HTML
    public static String doGet( String queryString) {
        URL get_url = null;
        String responseHtml =null;
        HttpURLConnection connection = null;
        try {
            get_url = new URL(URL_LINK+queryString);
            connection = (HttpURLConnection) get_url.openConnection();

            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                response.append(line);
            }
//            Message message =  new Message();
//            message.what = SHOW_RESPONSE;
//            message.obj =response.toString();
            responseHtml=response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return responseHtml;
    }

    //用于解析传入的html，返回所需的查询结果
    public static List  resolveHtml(String htmlString) {

        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        org.jsoup.nodes.Document document=Jsoup.parse(htmlString);

        //将以“tr”开头的标签存入trs元素群中
        Elements trs=document.select("tr");

        int totalTrs=trs.size();
        if(totalTrs>2){
            for (int i = 0; i < totalTrs-2; i++) {
                Elements tds=trs.get(i+2).select("td");
                //获取每个tr标签中td的个数
                int totalTds=tds.size();
                //临时存放各个属性的map
                Map<String,Object> map=new HashMap<String, Object>();

                for (int j = 0; j < totalTds; j++) {
                    switch (j) {
                        case 0:
                            map.put("station", tds.get(j).select("a").html().toString());
                            System.out.println(tds.get(j).select("a").html().toString());
                            break;
                        case 1:
                            map.put("id", tds.get(j).html().toString());
                            System.out.println(tds.get(j).html().toString());

                            break;
                        case 2:
                            map.put("carNumber", tds.get(j).html().toString());
                            System.out.println(tds.get(j).html().toString());
                            break;
                        case 3:
                            map.put("time", tds.get(j).html().toString());
                            System.out.println(tds.get(j).html().toString());
                            System.out.println("------------------------------------------------------------------------");
                            break;
                        default:
                            break;
                    }
                }
                list.add(map);
            }
        }
        return list;
      }
    }
