package com.bs.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bs.entity.User;
import com.bs.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController
{
	@Autowired
	private UserService userService;

	/**
	 * ��¼
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userName = request.getParameter("username");
		User user = userService.findPwdByUserName(userName);
		String pwd = request.getParameter("password");
		if (user == null)
		{
			response.getWriter().print("<script language='javascript'>alert('�û��������ڣ����������룡')</script>");
			response.setHeader("refresh", "1;" + request.getContextPath() + "/jsp/login.jsp");
		} else if (!pwd.equals(user.getPassWord()))
		{
			response.getWriter().print("<script language='javascript'>alert('����������������룡')</script>");
			response.setHeader("refresh", "1;" + request.getContextPath() + "/jsp/login.jsp");
		} else
		{
			request.getSession().setAttribute("user", user);
			response.setHeader("refresh", "1;" + request.getContextPath() + "/jsp/index.jsp");
		}
	}

	/**
	 * ��������
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/resetPwd")
	public void register(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userName = request.getParameter("username");
		boolean isExist = userService.findIsExist(userName);
		if (!isExist)
		{
			response.getWriter().print("<script language='javascript'>alert('�û��������ڣ����������룡')</script>");
			response.setHeader("refresh", "1;" + request.getContextPath() + "/jsp/reset_pwd.jsp");
		} else
		{
			String email = userService.findEmailByUserName(userName);
			String inputEmail = request.getParameter("email");
			if (!email.equals(inputEmail))
			{
				response.getWriter().print("<script language='javascript'>alert('���䲻��ȷ�����������룡')</script>");
				response.setHeader("refresh", "1;" + request.getContextPath() + "/jsp/reset_pwd.jsp");
			} else
			{

				// ��֤���߼�

				String pwd = request.getParameter("password");

				User user = new User();
				user.setUserName(userName);
				user.setPassWord(pwd);
				int result = userService.updatePassword(user);
				if (result == 1)
				{
					response.getWriter().print("<script language='javascript'>alert('�����޸ĳɹ���')</script>");
					response.setHeader("refresh", "1;" + request.getContextPath() + "/jsp/login.jsp");
				}
			}
		}
	}
}
