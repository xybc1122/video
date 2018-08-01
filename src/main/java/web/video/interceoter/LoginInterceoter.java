package web.video.interceoter;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;
import web.video.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static web.video.domain.JsonData.buildError;

public class LoginInterceoter implements HandlerInterceptor {
    private static Gson gson = new Gson();

    /**
     * 用户登录进入controller层之前 进行拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null) {
            //尝试去参数里面获取看看
            token = request.getParameter("token");
        }
        if (token != null) {
            Claims claims = JwtUtils.ccheckJWT(token);
            if (claims != null) {
                Integer userId = (Integer) claims.get("id");
                String name = (String) claims.get("name");
                request.setAttribute("userId", userId);
                request.setAttribute("name", name);
                return true;
            }
        }
        sendJsonMessaget(response, buildError("请登录"));
        return false;
    }

    /**
     * 响应数据给前端
     * @param response
     */
    public static void sendJsonMessaget(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(gson.toJson(obj));
        writer.close();
        response.flushBuffer();
    }
}
