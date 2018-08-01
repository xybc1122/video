package web.video.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.video.domain.Video;
import web.video.service.impl.VideoServiceImpl;

@RestController
@RequestMapping("admin")
public class VideoAdminController {
    @Autowired
    private VideoServiceImpl videoServiceImpl;

    /**
     * 根据id删除视频
     *
     * @param videoId
     * @return
     */
    @DeleteMapping("del")
    public Object delById(@RequestParam(value = "video_id", required = true) int videoId) {
        return videoServiceImpl.delete(videoId);
    }

    /**
     * 根据id更新视频
     * @return
     */
    @PutMapping("update")
    public Object update(@RequestBody Video video) {
        return videoServiceImpl.update(video);
    }

    /**
     * 保存视频对象
     * @return
     */
    @PostMapping("save")
    public Object save(@RequestBody Video video) {
        return videoServiceImpl.save(video);
    }

}
