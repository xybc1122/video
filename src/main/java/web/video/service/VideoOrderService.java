package web.video.service;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import web.video.domain.VideoOrder;
import web.video.dto.VideoOrderDto;

import java.util.List;

/**
 * 订单接口
 */
public interface VideoOrderService {
    /**
     * 下单接口
     *
     * @param videoOrderDto
     * @return
     */
    String save(VideoOrderDto videoOrderDto) throws Exception;

    /**
     * 根据主键查找订单
     *
     * @param id
     * @return
     */
    @Select("select * from video_order where id=#{order_id} and del=0")
    VideoOrder findById(@Param("order_id") int id);


    /**
     * 根据交易订单号获取订单对象
     *
     * @param outTradeNo
     * @return
     */
    @Select("select * from video_order where out_trade_no=#{out_trade_no} and del=0")
    VideoOrder findByOutTradeNo(@Param("out_trade_no") String outTradeNo);


    /**
     * 逻辑删除订单
     *
     * @param id
     * @param userId
     * @return
     */
    @Update("update video_order set del=0 where id=#{id} and user_id =#{userId}")
    int del(@Param("id") int id, @Param("userId") int userId);


    /**
     * 查找我的全部订单
     *
     * @param userId
     * @return
     */
    @Select("select * from video_order where user_id =#{userId}")
    List<VideoOrder> findMyOrderList(int userId);


    /**
     * 根据订单流水号更新
     *
     * @param videoOrder
     * @return
     */
    @Update("update video_order set state=#{state}, notify_time=#{notifyTime}, openid=#{openid}" +
            " where out_trade_no=#{outTradeNo} and state=0 and del=0")
    int updateVideoOderByOutTradeNo(VideoOrder videoOrder);
}
