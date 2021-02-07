package com.cdfg.custdept.pojo.until;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HttpClientUtil {

    Logger logger = Logger.getLogger(HttpClientUtil.class);

    public String doPost(TableInfo all){
        String url="http://host:port/rxp/buyer/check";

        HttpPost post = new HttpPost(url);
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse res;

        //提取报文三个参数，方便用于日志报错显示
        String idcardType = null;
        String idcardNo = null;
        String buyerName = null;
        try {
            idcardType = all.getIdcardType();
            idcardNo = all.getIdcardNo();
            buyerName = all.getBuyerName();
            //开始组装报文,第一层json
            JSONObject json = new JSONObject();
            //将jsonlist放到第一层的json中
            json.put("idcardType", all.getBuyerName());
            json.put("idcardNo", all.getIdcardNo());
            json.put("buyerName", all.getIdcardType());

            //报文组装完成，开始发送
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType

            post.setEntity(s);
            res = httpclient.execute(post);

            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());//返回json格式
                logger.info(idcardType+"#"+idcardNo+"#"+buyerName+"上传成功");
                return result;
            }else{
                String err = EntityUtils.toString(res.getEntity());
                logger.error("拉取失败,错误编码为："+res.getStatusLine().getStatusCode());
                logger.info(idcardType+"#"+idcardNo+"#"+buyerName+"上传失败");
                if (!err.isEmpty()) {
                    err="err";
                }else {
                    err="N";
                }
                return err;
            }
        } catch (Exception e) {
            logger.error("json报文发送失败"+"idcardType:"+idcardType+"idcardNo"+idcardNo+"buyerName"+buyerName);
            return "N";
        }finally {
            if (post != null) {
                post.releaseConnection();
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    logger.error("httpclient资源关闭失败");
                }
            }
        }
    }
}