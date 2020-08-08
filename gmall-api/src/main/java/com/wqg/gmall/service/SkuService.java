package com.wqg.gmall.service;

import com.wqg.gmall.bean.PmsSkuInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */

public interface SkuService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId);

    List<PmsSkuInfo> getAllSku(String catalog3Id);
}
