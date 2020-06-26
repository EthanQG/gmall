package com.wqg.gmall.user.service;

import com.wqg.gmall.user.bean.UmsMember;
import com.wqg.gmall.user.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
