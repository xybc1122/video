package web.video.mapper;

import org.apache.ibatis.annotations.*;

import web.video.domain.Video;
import web.video.provider.VideoProvider;

import java.util.List;

/**
 * video 数据访问层
 */
@Mapper
public interface VideoMapper {

    @Select("select * from video")
    List<Video> findAll();

    /**
     * 查询
     * @param id
     * @return
     */
    @Select("select * from video where id=#{id}")
    Video findById(int id);

    /**
     * type 加载类
     * method 对应类里的方法名称
     * @param video
     * @return
     */
    @UpdateProvider(type = VideoProvider.class,method = "updateVideo")
    int update(Video video);

    /**
     * 新增
     * @param id
     */
    @Delete("delete from video where id =#{id}")
    int delete(int id);

    @Insert("INSERT INTO `video` ( `title`, `summary`, " +
            "`cover_img`, `view_num`, `price`, `create_time`," +
            " `online`, `point`)" +
            "VALUES" +
            "(#{title}, #{summary}, #{coverImg}, #{viewNum}, #{price},#{createTime}" +
            ",#{online},#{point});")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id") //插入数据后第一时间获得自增 id
    int save(Video video);

}
