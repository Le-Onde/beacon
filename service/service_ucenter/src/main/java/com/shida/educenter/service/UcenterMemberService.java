package com.shida.educenter.service;

import com.shida.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shida.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-05-07
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);
}
