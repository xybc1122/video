package web.video.exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import web.video.domain.JsonData;
/**
 * 异常处理控制器
 */
@ControllerAdvice
public class LsExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData Handler(Exception e) {
        if (e instanceof LsException) {
            //自定义异常
            LsException lsException = (LsException) e;
            return JsonData.buildError(lsException.getMsg(),lsException.getCode());
        } else {
            return JsonData.buildError("全局异常，未知错误");
        }
    }
}
