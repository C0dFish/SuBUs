package com.seu.zhanghao.subus;

/**
 * Created by zhanghao7 on 2016/11/30.
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class WebData {

    static String htmlString;

    //执行一个HTTP GET请求，返回请求响应的HTML
    //url 请求的URL地址
    //queryString 请求的查询参数,可以为null
    //pretty是否美化
    //返回请求响应的HTML
    public static String doGet(String url, String queryString) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        try {

            // 对get请求参数做了http请求默认编码
            method.setQueryString(URIUtil.encodeQuery(queryString));
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(method.getResponseBodyAsStream(),
                                "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append(System.getProperty("line.separator"));
                }
                reader.close();
            }
        } catch (URIException e) {
            System.out.println("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！");
        } catch (IOException e) {
            System.out.println("执行HTTP Get请求" + url + "时，发生异常！");
        } finally {
            method.releaseConnection();
        }

        return response.toString();
    }



    // StreamTool工具类
    public static class StreamTool {
        public static byte[] readInputStream(InputStream inputStream)
                throws Exception
        {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;

            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            inputStream.close();
            return outputStream.toByteArray();

        }

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

    //用户输入线路号lineNumber，返回一个List，List内存放链接（点击该链接）和线路方向,调用doGet()
    //方法后将该链接传入第二个参数返回网页相应的Content，再调用相关方法进行解析
    //用于首界面点击查询按钮后界面设置
    public static List queryRequest1(String lineName) throws UnsupportedEncodingException{
        byte[] buff;
        String html = null;
        HttpClient client = new HttpClient();

        // Stream页面里面有Host地址 端口是80
        client.getHostConfiguration().setHost("http:// www.szjt.gov.cn", 80);

        // 用目标地址 实例一个POST方法
        PostMethod post = new PostMethod("http://www.szjt.gov.cn/apts/APTSLine.aspx");

        // 将需要的键值对写出来
        NameValuePair ctl00$MainContent$LineName = new NameValuePair(
                "ctl00$MainContent$LineName",lineName);

        NameValuePair ctl00$MainContent$SearchLine = new NameValuePair(
                "ctl00$MainContent$SearchLine", "搜索");

        NameValuePair VIEWSTATEGENERATOR = new NameValuePair(
                "__VIEWSTATEGENERATOR", "964EC381");

        NameValuePair VIEWSTATE = new NameValuePair(
                "__VIEWSTATE",
                "/wEPDwUJNDk3MjU2MjgyD2QWAmYPZBYCAgMPZBYCAgEPZBYCAgYPDxYCHgdWaXNpYmxlaGRkZArYd9NZeb6lYhNOScqHVvOmnKWkIejcJ7J2157Nz6l1");

        NameValuePair EVENTVALIDATION = new NameValuePair("__EVENTVALIDATION",
                "/wEWAwL5m9CTDgL88Oh8AqX89aoKFjHWxIvicIW2NoJRKPFu7zDvdWiw74UWlUePz1dAXk4=");


        // 给POST方法加入上述键值对
        post.setRequestBody(new NameValuePair[] { EVENTVALIDATION, VIEWSTATE,
                VIEWSTATEGENERATOR, ctl00$MainContent$LineName,
                ctl00$MainContent$SearchLine });


        // 执行POST方法
        try {
            client.executeMethod(post);

            // 将POST返回的数据以流的形式读入，再把输入流流至一个buff缓冲字节数组

            buff = StreamTool.readInputStream(post.getResponseBodyAsStream());
            // 将返回的内容格式化为String存在html中
            html = new String(buff);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        post.releaseConnection();

        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        org.jsoup.nodes.Document document=Jsoup.parse(html);

        //将以“tr”开头的标签存入trs元素群中
        Elements trs=document.select("tr");

        int totalTrs=trs.size();
        if(totalTrs>2){
            for (int i = 0; i <2; i++) {
                Elements tds=trs.get(i+2).select("td");
                //获取每个tr标签中td的个数
                int totalTds=tds.size();
                //临时存放各个属性的map
                Map<String,Object> map=new HashMap<String, Object>();
                String subHerf;
                String repDirection;
                //	String
                for (int j = 0; j < totalTds; j++) {
                    switch (j) {
                        case 0:
                            subHerf=tds.get(j).select("a").attr("href").toString();
                            subHerf=subHerf.substring(13);
                            map.put("lineLink",subHerf);
                            System.out.println(subHerf);
                            break;
                        case 1:
                            repDirection=tds.get(j).html().toString();
                            repDirection=repDirection.replace("=&gt;", "→");
                            map.put("direction", repDirection);
                            System.out.println(repDirection);
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


    //调用queryRequest1(String lineName)后解析List内的链接，并将其传入到queryRequest2(String hrefString)中
    //返回查询结果
    public static List queryRequest2(String hrefString) {
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        String htmString=doGet("http://www.szjt.gov.cn/apts/APTSLine.aspx",hrefString);
        list=resolveHtml(htmString);

        return list;
    }

    //主函数
    public static void main(String[] args) throws IOException, Exception {

        queryRequest1("156");
        queryRequest2("?LineGuid=83dba052-430b-41c2-bd33-7b1fb3a758b4&LineInfo=156(官渎里立交换乘枢纽站=>车坊首末站)");

    }

}
