package web.video.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 支付宝支付配置
 */
@Configuration
@PropertySource(value = "classpath:application.properties")
public class AlipayConfig {


    /**
     * 支付宝支付回调url
     */
    private String ALIPAY_CALLBACK_URL="xdclass.tunnel.qydev.com/api/alipay/order/callback";
}
