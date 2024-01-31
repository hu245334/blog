package org.example.blog.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
public class AliOSSUtil {
    private static String ENDPOINT;
    private static String ACCESS_KEY_ID;
    private static String ACCESS_KEY_SECRET;
    private static String BUCKET_NAME;

    @Value("${ENDPOINT}")
    public void setEndpoint(String endpoint) {
        AliOSSUtil.ENDPOINT = endpoint;
    }

    @Value("${ACCESS_KEY_ID}")
    public void setAccessKeyId(String accessKeyId) {
        AliOSSUtil.ACCESS_KEY_ID = accessKeyId;
    }

    @Value("${ACCESS_KEY_SECRET}")
    public void setAccessKeySecret(String accessKeySecret) {
        AliOSSUtil.ACCESS_KEY_SECRET = accessKeySecret;
    }

    @Value("${BUCKET_NAME}")
    public void setBucketName(String bucketName) {
        AliOSSUtil.BUCKET_NAME = bucketName;
    }

    public static String upload(String objectName, InputStream inputStream) throws Exception {

        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String url = null;

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest
                    (BUCKET_NAME, objectName, inputStream);

            PutObjectResult result = ossClient.putObject(putObjectRequest);
            url = "https://" + BUCKET_NAME + "." + ENDPOINT + "/" + objectName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }
}