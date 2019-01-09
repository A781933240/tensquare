package com.tensquare.spit.qa.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component  //加入容器中
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");

        //前后端约定：前端请求微服务时需要添加头信息Authorization ,内容为Bearer+空格+token
        String authHeader = request.getHeader("Authorization");
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            //获取token
            String token = authHeader.substring(7);
            try {
                //解析获取载荷
                Claims claims = jwtUtil.parseJWT(token);
                if("user".equals(claims.get("roles"))){
                    //如果拦截下来的角色是普通用户,那么把角色设置到Attribute中
                    request.setAttribute("user_claims",claims);
                }
                if("admin".equals(claims.get("roles"))){
                    //如果拦截下来的角色是管理员,那么把角色设置到Attribute中
                    request.setAttribute("admin_claims",claims);
                }
            }catch (Exception e){
                throw new RuntimeException("token过期");
            }
        }
        return true;
    }

}
