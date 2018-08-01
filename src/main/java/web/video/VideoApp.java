package web.video;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("web.video.mapper")
public class VideoApp {
	//tis is test demo
    public static  void main(String[] args){
        SpringApplication.run(VideoApp.class, args);
    }
}
