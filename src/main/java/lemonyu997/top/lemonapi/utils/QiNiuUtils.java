package lemonyu997.top.lemonapi.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

//七牛云工具类
@Component
public class QiNiuUtils {
    @Value("${qiniu.url}")
    private String url;
    @Value("${qiniu.accessKey}")
    private String accessKey;       //七牛云ak
    @Value("${qiniu.secretKey}")
    private String secretKey;       //七牛云sk

    //图片上传服务
    public boolean upload(MultipartFile file, String filename) {
        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //空间名
        String bucket = "lemon-blog997";

        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, filename, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
