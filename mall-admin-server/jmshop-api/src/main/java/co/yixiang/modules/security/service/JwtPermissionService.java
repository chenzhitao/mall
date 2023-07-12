/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.security.service;


import co.yixiang.modules.user.entity.YxUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtPermissionService {


    /**
     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
     * @param user
     * @return
     */

    public Collection<GrantedAuthority> mapToGrantedAuthorities(YxUser user) {

        System.out.println("--------------------loadPermissionByUser:" + user.getUsername() + "---------------------");

        //Set<Role> roles = roleRepository.findByUsers_Id(user.getId());
        List<String> list = new ArrayList<>();
        list.add("jmshop");

        return list.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
