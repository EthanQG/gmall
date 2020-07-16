package com.wqg.gmall.service;

import com.wqg.gmall.bean.UmsMember;
import com.wqg.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
