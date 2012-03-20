package com.util;

import java.util.Vector;

public class UserInfo 
{
	//用户昵称
	public String m_sUserName = null;
	//用户邮箱
	public String m_sEmail = null;
	//用户密码
	public String m_sPassWord = null;
	//用户手机号
	public String m_sMobile = null;
	//用户社会状态
	public String m_sWorkState = null;
	//用户所属单位
	public String m_sCompany = null;
	
	//用户已参加的活动信息
	public Vector<ActionInfo> m_vJoinEvents = null;
	//用户已关注的活动信息
	public Vector<ActionInfo> m_vFollowEvents = null;
}
