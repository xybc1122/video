package web.video.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import web.video.domain.Video;
import web.video.domain.VideoOrder;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class VideoOrderMapperTest {
    @Autowired
    private VideoOrderMapper videoOrderMapper;
    @Test
    public void insert() {
        VideoOrder videoOrder =new VideoOrder();
        videoOrder.setDel(0);
        videoOrder.setTotalFee(11);
        videoOrder.setHeadImg("hello");
        videoOrder.setVideoTitle("springboot高级视频教程");
        videoOrderMapper.insert(videoOrder);
        assertNotNull(videoOrder);

    }

    @Test
    public void findById() {
        VideoOrder videoOrder=videoOrderMapper.findById(2);
        assertNotNull(videoOrder);
    }

    @Test
    public void findByOutTradeNo(){

    }

    @Test
    public void del() {

    }

    @Test
    public void findMyOrderList() {
    }

    @Test
    public void updateVideoOderByOutTradeNo() {

    }
}