package com.wqg.gmall.user.mapper;

import com.wqg.gmall.user.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
public interface UserMapper extends Mapper<UmsMember> {
    List<UmsMember> selectAllUser();
}
