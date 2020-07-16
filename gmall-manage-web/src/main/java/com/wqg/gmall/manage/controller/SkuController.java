package com.wqg.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wqg.gmall.bean.PmsSkuInfo;
import com.wqg.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
@CrossOrigin
public class SkuController {
    @Reference
    SkuService skuService;
    //保存sku信息
    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo)
    {
        //处理默认图片
        String skuDefaultImg=pmsSkuInfo.getSkuDefaultImg();
        if (StringUtils.isBlank(skuDefaultImg))
        {
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }
        skuService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }
}
