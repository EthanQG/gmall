package com.wqg.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.wqg.gmall.bean.PmsSkuAttrValue;
import com.wqg.gmall.bean.PmsSkuImage;
import com.wqg.gmall.bean.PmsSkuInfo;
import com.wqg.gmall.bean.PmsSkuSaleAttrValue;
import com.wqg.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.wqg.gmall.manage.mapper.PmsSkuImageMapper;
import com.wqg.gmall.manage.mapper.PmsSkuInfoMapper;
import com.wqg.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.wqg.gmall.service.SkuService;
import com.wqg.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;


import java.util.List;
import java.util.UUID;

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
    @Autowired
    RedisUtil redisUtil;

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

    //通过skuid查询sku信息从数据库中查询
    public PmsSkuInfo getSkuByIdFromDB(String skuId) {
        //sku商品对象
        PmsSkuInfo pmsSkuInfo=new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo=pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        //图片集合
        PmsSkuImage pmsSkuImage=new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages=pmsSkuImageMapper.select(pmsSkuImage);
        skuInfo.setSkuImageList(pmsSkuImages);
        return skuInfo;
    }
    //从缓存中查询
    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        PmsSkuInfo pmsSkuInfo=new PmsSkuInfo();
        //连接缓存
        Jedis jedis=redisUtil.getJedis();
        //查询缓存
        String skuKey="sku:"+skuId+":info";
        String skuJson=jedis.get(skuKey);
        //如果skuJson不为空 就去解析它
        if (StringUtils.isNotBlank(skuJson))
        {
            pmsSkuInfo=JSON.parseObject(skuJson,PmsSkuInfo.class);
        }
        //如果缓存中没有再去查询mysql
        else{
            //设置分布式锁 加入验证token 以免redis锁过期后 过期请求来删除其他线程的锁
            String token= UUID.randomUUID().toString();
            String OK=jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10000);
            if (StringUtils.isNoneBlank(OK)&&OK.equals("OK"))
            {
                //设置成功 有权利在10s的过期时间内访问数据库
                pmsSkuInfo=getSkuByIdFromDB(skuId);
                if (pmsSkuInfo!=null)
                {
                    // mysql查询结果存入redis
                    jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));
                }else
                {
                    //数据库中不存在该sku
                    //为了防止缓存穿透，将null或空字符串设置给redis 设置短暂过期时间
                    jedis.setex("sku:"+skuId+":info",60*3,JSON.toJSONString(""));
                }
                String lockToken=jedis.get("sku:" + skuId + ":lock");
                //不管是给redis中存入数据还是空字符串 都需要释放锁 只有当当前对象的token和redis锁的token一致才进行删锁
                if (StringUtils.isNotBlank(lockToken)&&lockToken.equals(token))
                {
                    jedis.del("sku:" + skuId + ":lock");//用token确认删除的是自己的锁
                }
            }
            else
            {
                //设置失败，自旋几秒后重新访问本方法
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuById(skuId);
            }

        }
        jedis.close();
        return pmsSkuInfo;
    }
    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
        List<PmsSkuInfo> pmsSkuInfos=pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku(String catalog3Id) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String skuId = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfos;
    }
}
