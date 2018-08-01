package video;

import io.jsonwebtoken.Claims;
import org.junit.Test;
import web.video.domain.User;
import web.video.utils.JwtUtils;

public class CommonTest {


    @Test
    public void testGeneJwt() {
        User user = new User();
        user.setId(999);
        user.setHeadImg("wwww");
        user.setName("myToken");
        String token = JwtUtils.genJsonWebToken(user);
        System.out.println(token);
    }

    @Test
    public void testCheck() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWRlb1Rva2VuIiwiaWQiOjk5OSwibmFtZSI6Im15VG9rZW4iLCJpbWciOiJ3d3d3IiwiaWF0IjoxNTMwNjY4Mzg5LCJleHAiOjE1MzEyNzMxODl9.Ckb2yTVnWjlVyA_rDZFJeGU6x7UwqBabW7whM__qVSA";
        Claims claims = JwtUtils.ccheckJWT(token);
        if (claims != null) {
            int id = (Integer) claims.get("id");
            String name = (String) claims.get("name");
            String img = (String) claims.get("img");
            System.out.println(id);
            System.out.println(name);
            System.out.println(img);
        }
    }
}
