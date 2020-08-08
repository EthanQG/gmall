package com.wqg.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.wqg.gmall.bean.UmsMember;
import com.wqg.gmall.bean.UmsMemberReceiveAddress;
import com.wqg.gmall.service.UserService;
import com.wqg.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.wqg.gmall.user.mapper.UserMapper;
import com.wqg.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;


import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMemberList = userMapper.selectAll();
        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
        return umsMemberReceiveAddresses;
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = null;
        try {

            jedis = redisUtil.getJedis();
            if (jedis != null) {
                //从缓存中获取用户信息
                String umsMemberStr = jedis.get("user:" + umsMember.getPassword() + ":info");
                if (StringUtils.isNotBlank(umsMemberStr)) {
                    //从缓存中获取到了用户信息说明密码正确
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                }
            }
            //连接redis失败 /缓存中没有查询到 开启数据库查询
            UmsMember umsMemberFromDB = loginFromDB(umsMember);
            if (umsMemberFromDB != null) {
                jedis.setex("user:" + umsMemberFromDB.getPassword() + ":info", 60 * 60 * 24, JSON.toJSONString(umsMemberFromDB));
            }
            return umsMemberFromDB;

        } finally {
            jedis.close();
        }
    }

    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();

        jedis.setex("user:"+memberId+":token",60*60*2,token);

        jedis.close();
    }

    private UmsMember loginFromDB(UmsMember umsMember) {
        UmsMember user = userMapper.selectOne(umsMember);

        if(user!=null){
            return user;
        }

        return null;

    }

}
