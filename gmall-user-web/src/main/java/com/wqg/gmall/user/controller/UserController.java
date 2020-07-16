package com.wqg.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wqg.gmall.bean.UmsMember;
import com.wqg.gmall.bean.UmsMemberReceiveAddress;
import com.wqg.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
public class UserController {
    @Reference
    UserService userService;
    //根据用户id获得收货地址
    @RequestMapping("getReceiveAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId)
    {
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses=userService.getReceiveAddressByMemberId(memberId);
        return umsMemberReceiveAddresses;
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){

        List<UmsMember> umsMembers = userService.getAllUser();

        return umsMembers;
    }

    @RequestMapping("index")
    @ResponseBody
    public String index()
    {
        return "hello";
    }
}
