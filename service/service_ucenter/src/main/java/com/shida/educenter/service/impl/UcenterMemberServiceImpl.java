package com.shida.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shida.commonutils.JwtUtils;
import com.shida.commonutils.MD5;
import com.shida.educenter.entity.UcenterMember;
import com.shida.educenter.entity.vo.RegisterVo;
import com.shida.educenter.mapper.UcenterMemberMapper;
import com.shida.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shida.servicebase.exceptionhandler.LeOndeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-05-07
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(UcenterMember member) {
        //获取登录手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        //手机号和密码非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new LeOndeException(20001, "登录失败,账号或密码不能为空");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        //判断对象是否为空
        if (mobileMember == null) {
            //没有这个手机号
            throw new LeOndeException(20001, "账号不存在");
        }

        //判断密码
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new LeOndeException(20001, "账号或密码错误");
        }

        //是否逻辑删除
        if (mobileMember.getIsDeleted()) {
            throw new LeOndeException(20001, "账号不存在");
        }
        //判断用户是否禁用
        if (mobileMember.getIsDisabled()) {
            throw new LeOndeException(20001, "此账号已被禁用");
        }
        //登录成功
        //生成token字符串使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
        //注册的数据获取
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //非空刷新
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)) {
            throw new LeOndeException(20001, "注册失败,不能为空");
        }

        //判断验证码
        //获取redis验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new LeOndeException(20001, "验证码不一致");
        }
        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new LeOndeException(20001, "此账号已注册");
        }
        //数据添加数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));//密码需要加密的
        member.setIsDisabled(false);//用户不禁用
        member.setAvatar("https://leonde.oss-cn-beijing.aliyuncs.com/2021/05/02/1958467af5a74c309431564416157a96file.png");
        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }
}
