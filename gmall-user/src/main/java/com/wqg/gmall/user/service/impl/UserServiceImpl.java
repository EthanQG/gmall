package com.wqg.gmall.user.service.impl;

import com.wqg.gmall.user.bean.UmsMember;
import com.wqg.gmall.user.bean.UmsMemberReceiveAddress;
import com.wqg.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.wqg.gmall.user.mapper.UserMapper;
import com.wqg.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMemberList=userMapper.selectAll();
        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress=new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses=umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
        return umsMemberReceiveAddresses;
    }
}
