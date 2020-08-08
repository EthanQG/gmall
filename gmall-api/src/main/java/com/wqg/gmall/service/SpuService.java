package com.wqg.gmall.service;

import com.wqg.gmall.bean.PmsProductImage;
import com.wqg.gmall.bean.PmsProductInfo;
import com.wqg.gmall.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
public interface SpuService {
    List<PmsProductInfo> getspuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
