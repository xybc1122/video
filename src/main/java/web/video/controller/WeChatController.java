package web.video.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import web.video.config.WeChatConfig;
import web.video.domain.JsonData;
import web.video.domain.User;
import web.video.domain.VideoOrder;
import web.video.service.UserService;
import web.video.service.VideoOrderService;
import web.video.utils.JwtUtils;
import web.video.utils.WXPayUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

@Controller
@RequestMapping("/api/wechat")
public class WeChatController {
    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private UserService userService;

    @Autowired
    private VideoOrderService videoOrderService;

    /**
     * 拼装扫一扫获得登录的扫码url
     * accessPage传入state回调地址 比如http://www.baidu.com
     *
     * @return
     */
    @GetMapping("/login_url")
    @ResponseBody
    public JsonData loginUrl(@RequestParam(value = "access_page", required = true) String accessPage) throws UnsupportedEncodingException {
        String redirectUrl = weChatConfig.getOpenRedirectUrl();//获取开放平台重定向地址
        String callbackUrl = URLEncoder.encode(redirectUrl, "GBK");//进行编码
        String qrcodeUrl = String.format(WeChatConfig.getOpenQrcodeUrl(), weChatConfig.getOpenAppid(), callbackUrl, accessPage);
        return JsonData.buildSuccess(qrcodeUrl);
    }

    /**
     * 扫一扫登录成功后回调的路径获得code 转发到前端url
     *
     * @param code
     * @param state
     * @param response
     */
    @GetMapping("/user/callback")
    public void wechatUserCallback(@RequestParam(value = "code",
            required = true) String code, String state, HttpServletResponse response) throws IOException {
        User user = userService.saveWeChatUser(code);
        if (user != null) {
            //生成jwt token 返回给前端
            String token = JwtUtils.genJsonWebToken(user);
            // 拼装转发给前端
            response.sendRedirect(state+"?token="+token+"&head_img="+user.getHeadImg()+
                    "&name="+URLEncoder.encode(user.getName(),"UTF-8"));
        }
    }


    /**
     * 微信支付回调
     */
    @RequestMapping("/order/callback")
    public void orderCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream = request.getInputStream();
        //buff缓冲  性能高
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        inputStream.close();
        Map<String, String> callbackMap = WXPayUtil.xmlToMap(sb.toString());
        // System.out.println(callbackMap.toString());
        SortedMap<String, String> sortedMap = WXPayUtil.getSortedMap(callbackMap);
        //判断签名是否正确
        if (WXPayUtil.isCorrectSign(sortedMap, weChatConfig.getKey())) {
            if ("SUCCESS".equals(sortedMap.get("result_code"))) {
                //获得订单流水号
                String outTradeNo = sortedMap.get("out_trade_no");
                //更新订单状态
                VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);
                //判断订单是否是未支付状态
                if (dbVideoOrder.getState() == 0){
                    VideoOrder videoOrder = new VideoOrder();
                    videoOrder.setOpenid(sortedMap.get("openid"));
                    videoOrder.setOutTradeNo(outTradeNo);
                    videoOrder.setNotifyTime(new Date());
                    videoOrder.setState(1);
                    //影响函数row==1或者row==0 响应微信成功或失败
                    //更新操作
                    int rows = videoOrderService.updateVideoOderByOutTradeNo(videoOrder);
                    if (rows == 1){//通知微信订单处理成功
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        System.out.println("下单成功 ！");
                    }
                }

            }
        }else{
            //处理失败
            response.setContentType("text/xml");
            response.getWriter().println("fail");
            System.out.println("下单失败！");
        }

    }

}
