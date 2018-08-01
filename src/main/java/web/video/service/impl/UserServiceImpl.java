package web.video.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.video.config.WeChatConfig;
import web.video.domain.User;
import web.video.mapper.UserMapper;
import web.video.service.UserService;
import web.video.utils.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User saveWeChatUser(String code) {
        String accessTokenUrl = String.format(WeChatConfig.getOpenAccessTokenUrl(),
                weChatConfig.getOpenAppid(), weChatConfig.getOpenAppsecret(), code);
        //获取access_token
        Map<String, Object> beseMap = HttpUtils.doGet(accessTokenUrl, 5000);
        if (beseMap == null || beseMap.isEmpty()) {
            return null;
        }
        String accessToekn = (String) beseMap.get("access_token");
        String openId = (String) beseMap.get("openid");
        User dbUser = userMapper.findByopenid(openId);
        if (dbUser != null) {//更新用户，或者直接返回
            return dbUser;
        }

        //获取用户基本信息
        String userInfoUrl = String.format(WeChatConfig.getOpenUserInfoUrl(), accessToekn, openId);
        Map<String, Object> baseUserMap = HttpUtils.doGet(userInfoUrl, 5000);
        if (baseUserMap == null || baseUserMap.isEmpty()) {
            return null;
        }
        String nickname = (String) baseUserMap.get("nickname");
        Double sexTemp = (Double) baseUserMap.get("sex");
        //转换int 类型
        int sex = sexTemp.intValue();
        String city = (String) baseUserMap.get("city");
        String province = (String) baseUserMap.get("province");
        String country = (String) baseUserMap.get("country");
        String headimgurl = (String) baseUserMap.get("headimgurl");
        //临时封装 国 省  城市
        StringBuilder sb = new StringBuilder(country).append("||").append(province).append("||").append(city);
        String finalAddress = sb.toString();
        //如果有乱码编码转换成UTF-8
        try {
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            finalAddress=new String(finalAddress.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //封装用户基本信息
        User user = new User();
        user.setName(nickname);
        user.setHeadImg(headimgurl);
        user.setCity(finalAddress);
        user.setOpenid(openId);
        user.setSex(sex);
        user.setCreateTime(new Date());
        //保存用户到数据库
        userMapper.save(user);
        return user;
    }
}
