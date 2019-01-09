package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;

@ConfigurationProperties("jwt.config")
public class JwtUtil {
    private String key;

    private long ttl;//一个小时

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return 返回tonken
     */
    public String createJWT(String id,String subject,String roles){

        //id : 用户的id
        //Subject : 用户的名字
        //issuedAt : 创建jwt的时间
        //singwith : 签名（签名算法是HS256算法，key是密钥）
        //claim   :  自定义键值对属性
        //Expiration : 设置过期时间
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key)
                .claim("roles", roles);
        if(ttl>0){
            builder .setExpiration(new Date(new Date().getTime()+60000));
        }
        return builder.compact();//返回tonken字符串
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}
