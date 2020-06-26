package com.wqg.gmall.user.controller;

import com.wqg.gmall.user.bean.UmsMember;
import com.wqg.gmall.user.bean.UmsMemberReceiveAddress;
import com.wqg.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;
    //根据用户id获得收货地址
    @RequestMapping("getReceiveAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId)
    {
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses=userService.getReceiveAddressByMemberId(memberId);
        return umsMemberReceiveAddresses;
    }

    @RequestMapping("index")
    @ResponseBody
    public String index()
    {
        return "hello";
    }
}
