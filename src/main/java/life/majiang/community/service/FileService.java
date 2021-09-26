package life.majiang.community.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author lpf
 * @description 图片文件上传，图片，视频
 * https://blog.csdn.net/weixin_43481793/article/details/94136517
 * @date 2021/9/21
 */
@Service
public class FileService {

    @Value("${lpf-comment.upload_path}")
    private  String UPLOAD_PATH;

    /**
     * 文件的上传：http://121.40.218.249:8888/upload/+文件名（映射地址）
     * @param file
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile file) throws IOException {
        // 获取文件名
        String fileName = file.getOriginalFilename();

        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UUID.randomUUID().toString().replace("-", "") + suffixName;

        // 返回客户端 文件地址 URL
        String url = "http://121.40.218.249:8888"+"/upload/" + fileName;

//        File dest = new File(UPLOAD_PATH + fileName);

        // 检测是否存在目录
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs();
//        }
//
//        file.transferTo(dest);

        return url;
    }

}
