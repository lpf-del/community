package life.majiang.community.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.UUID;

/**
 * @author lpf
 * @description 阿里云oss存储对象工具类
 * @date 2022/2/13
 */
@Component
public class OssUtill {
    private static String endPoint = "oss-cn-hangzhou.aliyuncs.com"; //Endpoint（地域节点）
    private static String accessKeyId = "LTAI5t8rKk1B6B6i2Dt3ypCZ";  //AccessKey ID
    private static String accessKeySecret = "lkEprS0oRCkrLeMy0SKX0azIFlKtBg"; //AccessKey Sevret
    private static String bucketName = "oss-test-lpf"; //Bucket名称
    private static String sufferUrl = "http://oss-test-lpf.oss-cn-hangzhou.aliyuncs.com/"; //Bucket 域名
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd"); //日期格式化对象

    /**
     * 获取oss连接对象
     * @return
     */
    public OSSClient getOssClient(){
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        //判断仓库是否存在（不存在就创建）
        if (ossClient.doesBucketExist(bucketName)){
//            System.out.println("bucket存在");
        }else {
            //通过api创建bucket仓库
//            System.out.println("创建bucket仓库" + bucketName);
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(null);
            createBucketRequest.setBucketName(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }
        return ossClient;
    }

    /**
     * oss文件上传
     * @param multipartFile
     * @param bussessType
     * @return
     * @throws IOException
     */
    public String uploadDocument(MultipartFile multipartFile, String bussessType) throws IOException {
        OSSClient ossClient = this.getOssClient();
        String name = multipartFile.getOriginalFilename();
        String suffixName = name.substring(name.lastIndexOf("."));
        String date = simpleDateFormat.format(new Date());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = bussessType + "/" + date + "/" + uuid + suffixName;
        //oss上传文件
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(multipartFile.getBytes()));
        ossClient.shutdown();
        return sufferUrl + fileName;
    }
}
