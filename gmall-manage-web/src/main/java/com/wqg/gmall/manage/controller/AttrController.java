package com.wqg.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wqg.gmall.bean.PmsBaseAttrInfo;
import com.wqg.gmall.bean.PmsBaseAttrValue;
import com.wqg.gmall.bean.PmsBaseSaleAttr;
import com.wqg.gmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
@CrossOrigin
public class AttrController {

    //对于平台属性操作 比如手机分类下的 尺寸 颜色 大小等
    @Reference
    AttrService attrService;

    //根据三级分类id查询平台属性列表
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(@RequestParam String catalog3Id)
    {
        List<PmsBaseAttrInfo> pmsBaseAttrInfos=attrService.attrInfoList(catalog3Id);
        return pmsBaseAttrInfos;
    }
    //把提交的平台属性保存或修改
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo)
    {

        String success=attrService.saveAttrInfo(pmsBaseAttrInfo);
        return "success";
    }

    //根据平台属性id获取平台属性值列表
    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(@RequestParam String attrId)
    {

        List<PmsBaseAttrValue> pmsBaseAttrValues=attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }

    //获取所有销售属性 销售属性就是平台规定的商品的销售属性 比如颜色尺寸等 然后商家对这些销售属性写属性值
    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList()
    {
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs=attrService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }
}
