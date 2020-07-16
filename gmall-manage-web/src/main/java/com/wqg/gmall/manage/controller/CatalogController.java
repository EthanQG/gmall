package com.wqg.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wqg.gmall.bean.*;
import com.wqg.gmall.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
@CrossOrigin
public class CatalogController {
    @Reference
    CatalogService catalogService;

    //查询一级分类
    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1()
    {
        List<PmsBaseCatalog1> catalog1s= catalogService.getCatalog1();
        return catalog1s;
    }

    //根据一级分类id查询二级分类
    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(@RequestParam String catalog1Id)
    {
        List<PmsBaseCatalog2> catalog2s= catalogService.getCatalog2(catalog1Id);
        return catalog2s;
    }

    //根据二级分类查询三级分类
    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(@RequestParam String catalog2Id)
    {
        List<PmsBaseCatalog3> catalog3s= catalogService.getCatalog3(catalog2Id);
        return catalog3s;
    }


}
