package web.video.service;

import org.apache.ibatis.annotations.Mapper;
import web.video.domain.User;

/**
 * 用户业务接口类
 */
@Mapper
public interface UserService {
    /**
     * 根据code 去获取access_token  然后保存用户信息
     * @param code
     * @return
     */
     User saveWeChatUser(String code);
}
