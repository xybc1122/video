package web.video.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.video.config.WeChatConfig;
import web.video.domain.JsonData;
import web.video.mapper.VideoMapper;


@RestController
public class TestController {

	@Autowired
	private WeChatConfig weChatConfig;

	@Autowired
	private VideoMapper videoMapp;


	@RequestMapping("test")
	public String test(){

		return weChatConfig.getAppsecret();
	}



	@RequestMapping("test_db")
	public JsonData testDb(){
		

		return JsonData.buildSuccess(videoMapp.findAll(),"微信查询");
	}

}
