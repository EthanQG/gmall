package com.wqg.gmall.manage.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.wqg.gmall.bean.PmsBaseAttrInfo;
import com.wqg.gmall.bean.PmsBaseAttrValue;
import com.wqg.gmall.bean.PmsBaseSaleAttr;
import com.wqg.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.wqg.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.wqg.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.wqg.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;


import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Service
public class AttrServiceImpl implements AttrService {
    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo=new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo>pmsBaseAttrInfos=pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {
        //遍历每一个平台属性 根据平台属性id 查询每一个平台属性的属性值
            List<PmsBaseAttrValue> pmsBaseAttrValues = new ArrayList<>();
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String id = pmsBaseAttrInfo.getId();
        if (StringUtils.isBlank(id))//属性id为空说明是保存操作
        {
            //保存属性
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);//insertSelective把null的不插入 insert全插入
            //获取属性名称的id
            List<PmsBaseAttrValue> pmsBaseAttrValues=pmsBaseAttrInfo.getAttrValueList();
            //为每一个属性值设置外键 即给每个属性设置属性名称的id
            for(PmsBaseAttrValue pmsBaseAttrValue:pmsBaseAttrValues)
            {
                //给每个属性值设置属性id
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                //保存属性值
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }
        //属性id不为空说明是修改操作
        else {
            // 属性修改
            Example example = new Example(PmsBaseAttrInfo.class);
            //根据属性id修改属性
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);
            // 属性值修改
            // 按照属性id删除所有属性值
            PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
            pmsBaseAttrValueDel.setAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel);
            // 删除后，将新的属性值插入
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                //给每个属性值设置属性id
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }
        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue=new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues=pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }

    //获取所有销售属性
    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {

        return pmsBaseSaleAttrMapper.selectAll();
    }


}
