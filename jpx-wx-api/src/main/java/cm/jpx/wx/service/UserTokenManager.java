package cm.jpx.wx.service;

import cm.jpx.wx.util.JwtHelper;

/**
 * 维护用户token
 */
public class UserTokenManager {
    /**
     * 根据用户id产生维持session的token
     * @param id 用户id
     * @return token
     */
	public static String generateToken(Integer id) {
        JwtHelper jwtHelper = new JwtHelper();
        return jwtHelper.createToken(id);
    }

    public static Integer getUserId(String token) {
    	JwtHelper jwtHelper = new JwtHelper();
    	Integer userId = jwtHelper.verifyTokenAndGetUserId(token);
    	if(userId == null || userId == 0){
    		return null;
    	}
        return userId;
    }
}
