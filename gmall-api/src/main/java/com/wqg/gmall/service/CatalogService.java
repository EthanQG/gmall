package com.wqg.gmall.service;

import com.wqg.gmall.bean.PmsBaseCatalog1;
import com.wqg.gmall.bean.PmsBaseCatalog2;
import com.wqg.gmall.bean.PmsBaseCatalog3;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
public interface CatalogService {

    List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
