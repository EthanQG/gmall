package com.wqg.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wqg.gmall.bean.PmsProductImage;
import com.wqg.gmall.bean.PmsProductInfo;
import com.wqg.gmall.bean.PmsProductSaleAttr;
import com.wqg.gmall.manage.util.PmsUploadUtil;
import com.wqg.gmall.service.SpuService;
import org.csource.common.MyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
@CrossOrigin
public class SpuController {

    //对spu进行操作 比如小米手机 手机描述 （这是一个商品的大类）
    @Reference
    SpuService spuService;

    //根据三级分类id 查询平台的spu列表
    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(@RequestParam String catalog3Id)
    {

        List<PmsProductInfo> pmsProductInfos=spuService.getspuList(catalog3Id);
        return pmsProductInfos;
    }

    //保存spu信息
    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody  PmsProductInfo pmsProductInfo){
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    //上传图片或音视频信息
    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile) throws IOException, MyException {
        //将图片或音视频上传到分布式的文件存储系统
        //将图片的存储路径返回给页面
        String imgUrl= PmsUploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        return imgUrl;
    }

    //获取spu图片列表
    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){

        List<PmsProductImage> pmsProductImages = spuService.spuImageList(spuId);
        return pmsProductImages;
    }


    // 根据spuid查询销售属性列表
    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }


}
