package web.video.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 支付宝支付
 */
@Controller
@RequestMapping("api/alipay")
public class AlipayController {


    /**
     * 支付订单回调
     */
    @RequestMapping("")
    public void callback() {
System.out.println("");

    }
}
