package com.wqg.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wqg.gmall.bean.PmsSkuAttrValue;
import com.wqg.gmall.bean.PmsSkuImage;
import com.wqg.gmall.bean.PmsSkuInfo;
import com.wqg.gmall.bean.PmsSkuSaleAttrValue;
import com.wqg.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.wqg.gmall.manage.mapper.PmsSkuImageMapper;
import com.wqg.gmall.manage.mapper.PmsSkuInfoMapper;
import com.wqg.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.wqg.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;


    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        //插入sku
        int i=pmsSkuInfoMapper.insert(pmsSkuInfo);
        String productId=pmsSkuInfo.getProductId();
        //插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValues=pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue:skuAttrValues) {
            pmsSkuAttrValue.setSkuId(productId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
        //插入销售属性关联
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValues=pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue:pmsSkuSaleAttrValues)
        {
            pmsSkuSaleAttrValue.setSkuId(productId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }
        //插入图片信息
        List<PmsSkuImage> pmsSkuImages=pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage:pmsSkuImages)
        {
            pmsSkuImage.setSkuId(productId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
    }
}
