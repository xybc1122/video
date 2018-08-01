package web.video.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import web.video.domain.User;

@Mapper
public interface UserMapper {
    /**
     * 根据主键ID查找
     *
     * @param userId
     * @return
     */
    @Select("select * from user where id =#{userId}")
    User findById(int userId);

    /**
     * 根据openid找用户
     *
     * @param openid
     * @return
     */
    @Select("select * from user where openid =#{openid}")
    User findByopenid(String openid);

    /**
     * 保存用户新
     *
     * @param user
     * @return
     */
    @Insert("INSERT INTO `user` ( `openid`, `name`, `head_img`, `phone`, `sign`, `sex`, `city`, `create_time`)" +
            "VALUES" +
            "(#{openid},#{name},#{headImg},#{phone},#{sign},#{sex},#{city},#{createTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(User user);

}
