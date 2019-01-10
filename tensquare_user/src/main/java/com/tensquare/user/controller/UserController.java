package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除,只有管理员有权限删除用户
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		/*//前后端约定：前端请求微服务时需要添加头信息Authorization ,内容为Bearer+空格+token
		String authHeader = request.getHeader("Authorization");
		//如果为空
		if(authHeader==null){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//如果不是Bearer 开头的
		if(!authHeader.startsWith("Bearer ")){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//截取token
		String token = authHeader.substring(7);
		//如果校验的时候令牌过期,会报错,所以要捕获,
		try {
			//解析tonken
			Claims claims = jwtUtil.parseJWT(token);
			if(claims==null){
				return new Result(false,StatusCode.ACCESSERROR,"权限不足");
			}
			if(!"admin".equals(claims.get("roles"))){
				return new Result(false,StatusCode.ACCESSERROR,"权限不足");
			}
		}catch (Exception e){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}*/


		Claims claims = (Claims) request.getAttribute("admin_claims");
		if(claims==null){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 发送验证码
	 */
	@RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
	public Result sendSms(@PathVariable String mobile){
		userService.sendSms(mobile);
		return new Result(true,StatusCode.OK,"短信发送成功");
	}

	/**
	 * 注册
	 */
	@RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
	public Result register(@PathVariable String code,@RequestBody User user){
		//1.获取redis的验证码
		String checkCode = (String) redisTemplate.opsForValue().get("checkCode_"+user.getMobile());
		//如果redis里面没有验证码的数据,则代表没有发送短信
		if(StringUtils.isEmpty(checkCode)){
			return new Result(false, StatusCode.ERROR, "请先获取手机验证码");
		}
		//验证码不正确
		if(!checkCode.equals(code)){
			return new Result(false, StatusCode.ERROR, "验证码错误");
		}
		userService.register(user);
		return new Result(true,StatusCode.OK,"注册成功");
	}

	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public Result login(@RequestBody User loginUser){
		User user = userService.findByMobileAndPassword(loginUser.getMobile(), loginUser.getPassword());
		if(user==null){
			return new Result(false,StatusCode.LOGINERROR,"登录失败");
		}
		String token = jwtUtil.createJWT(user.getId(), user.getMobile(), "user");
		Map<String,Object> map = new HashMap<>();
		map.put("nickName",user.getNickname());//存储昵称
		map.put("token",token);
		map.put("roles","user");
		return new Result(true,StatusCode.OK,"登陆成功",map);
	}

	/**
	 * 更新好友粉丝数和用户关注数
	 * @return
	 */
	@RequestMapping(value = "/{userid}/{friendid}/{x}",method = RequestMethod.PUT)
	public void updatefanscountandfollowcount(@PathVariable String userid,@PathVariable String friendid,@PathVariable int x){
		userService.updatefanscountandfollowcount(x,userid,friendid);
	}
}
