/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.security.service;

import co.yixiang.exception.BadRequestException;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.security.security.vo.JwtUser;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hupeng
 * @date 2020/01/12
 */
@Service("userDetailsService")
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final YxUserService userService;
    private final JwtPermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username){
        YxUser user = userService.findByName(username);
        if (user == null) {
            throw new BadRequestException("账号不存在");
        } else {
            if (!user.getStatus()) {
                throw new ErrorRequestException("账号未激活");
            }
            return createJwtUser(user);
        }
    }

    private UserDetails createJwtUser(YxUser user) {
        return new JwtUser(
                Long.valueOf(user.getUid()),
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getAvatar(),
                user.getPhone(),
                permissionService.mapToGrantedAuthorities(user),
                user.getStatus(),
                user.getAddTime()
        );
    }
}
