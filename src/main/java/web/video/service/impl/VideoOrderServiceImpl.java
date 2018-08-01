package web.video.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import web.video.config.WeChatConfig;
import web.video.domain.User;
import web.video.domain.Video;
import web.video.domain.VideoOrder;
import web.video.dto.VideoOrderDto;
import web.video.mapper.UserMapper;
import web.video.mapper.VideoMapper;
import web.video.mapper.VideoOrderMapper;
import web.video.service.VideoOrderService;
import web.video.utils.CommonUtils;
import web.video.utils.HttpUtils;
import web.video.utils.WXPayUtil;

import java.util.*;


@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * @param videoOrderDto
     * @return
     * @throws Exception Transactional 事物注解
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String save(VideoOrderDto videoOrderDto) throws Exception {
        //查找视频信息
        Video video = videoMapper.findById(videoOrderDto.getVideoId());

        //查找用户信息
        User user = userMapper.findById(videoOrderDto.getUserId());
        //生成订单
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setVideoId(video.getId());
        videoOrder.setState(0);
        videoOrder.setUserId(user.getId());
        videoOrder.setHeadImg(user.getHeadImg());
        videoOrder.setNickname(user.getName());

        videoOrder.setDel(0);
        videoOrder.setIp(videoOrderDto.getIp());
        videoOrder.setOutTradeNo(CommonUtils.generateUUID());

        videoOrderMapper.insert(videoOrder);


        //获取codeurl
        String codeUrl = unifiedOrder(videoOrder);

         return codeUrl;
    }
    /**
     * 生成签名
     *
     * @param videoOrder
     * @return
     */
    public String unifiedOrder(VideoOrder videoOrder) throws Exception {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", weChatConfig.getAppid());
        params.put("mch_id", weChatConfig.getMchId());
        //UUID生成随机订单
        params.put("nonce_str", CommonUtils.generateUUID());
        params.put("body", videoOrder.getVideoTitle());
        params.put("out_trade_no", videoOrder.getOutTradeNo());
        params.put("total_fee", videoOrder.getTotalFee().toString());
        params.put("spbill_create_ip", videoOrder.getIp());
        params.put("notify_url", weChatConfig.getPayCallbackUrl());
        params.put("trade_type", "NATIVE");
        //sign 签名
        String sign = WXPayUtil.createSign(params, weChatConfig.getKey());
        params.put("sign", sign);
        //map 转xml
        String payXml = WXPayUtil.mapToXml(params);
        System.out.println(payXml);
        //统一下单
        String orderStr = HttpUtils.doPost(WeChatConfig.getUnifiedOrderUrl(), payXml, 4000);
        if (null == orderStr) {
            return null;
        }
        //xml转map
        Map<String, String> unifiedOrderMap = WXPayUtil.xmlToMap(orderStr);
        System.out.println(unifiedOrderMap.toString());
        if (unifiedOrderMap != null) {
            return unifiedOrderMap.get("code_url");
        }
        return null;
    }


    @Override
    public VideoOrder findById(int id) {
        return null;
    }

    @Override
    public VideoOrder findByOutTradeNo(String outTradeNo) {
        return videoOrderMapper.findByOutTradeNo(outTradeNo);
    }

    @Override
    public int del(int id, int userId) {
        return 0;
    }

    @Override
    public List<VideoOrder> findMyOrderList(int userId) {
        return null;
    }


    @Override
    public int updateVideoOderByOutTradeNo(VideoOrder videoOrder) {
        return videoOrderMapper.updateVideoOderByOutTradeNo(videoOrder);
    }
}
