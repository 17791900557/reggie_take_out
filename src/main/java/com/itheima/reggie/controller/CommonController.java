package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;//在配配置文件中设置了本地保存图片的路径

    /**
     * 文件上传到本地
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //file是上传的文件临时存储地要进行保存本地

        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //截取文件后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID给文件命名
        String fileName = UUID.randomUUID().toString() + suffix;
        //创建存储目录
        File dir = new File(basePath);
        //判断目录是否存在
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定目录
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * 下载
     * @param response
     * @param name
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response,String name) {
        try {
            //创建字节输入流读取本地文件
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //创建字节输出流给浏览器输出图片
            ServletOutputStream outputStream = response.getOutputStream();

            //设置响应格式
            response.setContentType("imag/jpeg");
            //将文件读取写入到浏览器
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
