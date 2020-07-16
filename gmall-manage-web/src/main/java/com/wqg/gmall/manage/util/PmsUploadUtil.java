package com.wqg.gmall.manage.util;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Auther: wqg
 * @Description:
 */
public class PmsUploadUtil {
    public static String uploadImage(MultipartFile multipartFile) throws IOException, MyException {
        {
            String imgUrl = "http://192.168.159.134";
            // 上传图片到服务器
            // 配置fdfs的全局链接地址
            String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();// 获得配置文件的路径
            ClientGlobal.init(tracker);
            TrackerClient trackerClient = new TrackerClient();
            // 获得一个trackerServer的实例
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            // 通过tracker获得一个Storage链接客户端
            StorageClient storageClient = new StorageClient(trackerServer, null);
            byte[] bytes = multipartFile.getBytes();// 获得上传的二进制对象
            // 获得文件后缀名
            String originalFilename = multipartFile.getOriginalFilename();// a.jpg
            String extName = StringUtils.substringAfterLast(originalFilename, ".");
            String[] uploadInfos = storageClient.upload_file(bytes, extName, null);
            System.out.println(extName);
            for (String uploadInfo : uploadInfos) {
                imgUrl += "/" + uploadInfo;
            }
            return imgUrl;
        }
    }
}
