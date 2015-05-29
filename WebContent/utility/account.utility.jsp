<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="tw.com.orangice.loop.constant.*,java.util.*, java.net.*, java.io.*,tw.com.orangice.loop.utility.*"%>
<%!public String getUrlPrefix(HttpServletRequest request)
			throws MalformedURLException {
		URL url = new URL(request.getScheme(), request.getServerName(),
				request.getServerPort(), request.getContextPath());

		return url.toString();
	}

	public int checkUsername(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, JspWriter out,
			int minPermission, String userCode) throws MalformedURLException,
			IOException {
		//檢查是否有初始設定
		ServletContext sc = request.getServletContext();
		//out.println("login:"+session.getAttribute("login"));
		//查看目前情況
		if (session.getAttribute("login") != null
				&& session.getAttribute("login").equals(1)) {
			//已登入
			int permission = (Integer) session.getAttribute("permission");
			if (permission > minPermission) {
				//目前的權限大於最小權限
				return 0;
			} else if (permission == minPermission) {
				//目前的權限等於最小權限
				if (userCode != null
						&& userCode.equals((String) session
								.getAttribute("userCode"))) {
					//使用者不為本人
					response.sendRedirect(getUrlPrefix(request) + "/"
							+ UrlConstant.PAGE_PERMISSION_DENY);
				} else {
					//使用者為本人
					return 0;
				}
			} else {
				//權限不足
				response.sendRedirect(getUrlPrefix(request) + "/"
						+ UrlConstant.PAGE_PERMISSION_DENY);
			}

		} else {
			//未登入
			response.sendRedirect(getUrlPrefix(request) + "/"
					+ UrlConstant.PAGE_LOGIN);
		}
		return -1;
	}

	public int getPermission(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, JspWriter out) {
		//查看目前情況
		if (session.getAttribute("login") != null
				&& session.getAttribute("login").equals(1)) {
			//已登入
			return (Integer) session.getAttribute("permission");
		} else {
			//未登入
			return -1;
		}
	}

	public String getToken(HttpServletRequest request, HttpSession session,
			JspWriter out) {
		if (session.getAttribute("login") != null
				&& session.getAttribute("login").equals(1)) {
			return (String) session.getAttribute("token");
		} else {
			return null;
		}
	}

	public String getUsername(HttpServletRequest request, HttpSession session,
			JspWriter out) {
		if (session.getAttribute("login") != null
				&& session.getAttribute("login").equals(1)) {
			return (String) session.getAttribute("username");
		} else {
			return null;
		}
	}

	public String getCode(HttpServletRequest request, HttpSession session,
			JspWriter out) {
		if (session.getAttribute("login") != null
				&& session.getAttribute("login").equals(1)) {
			return (String) session.getAttribute("code");
		} else {
			return null;
		}
	}

	public void checkAccountStatus(HttpServletRequest request,
			HttpSession session, JspWriter out) {
		try {
			//String path = request.getLocalAddr()+request.getContextPath();
			if (session.getAttribute("login") != null
					&& session.getAttribute("login").equals(1)) {
				

				
				if (session.getAttribute("permission").equals(999)) {
					out.print("<li>hi, " + session.getAttribute("username") + " [<a href=\"./admin/index.jsp\">管理介面</a>]</li>");
				}
				else{
					out.print("<li >hi, " + session.getAttribute("username") + "</li>");
				}
				out.print("<li><a href=\"account_manager/logout?return_url=index.jsp\">登出</a></li>");
			} else {
				out.print("<li><a href=\"./login.jsp\">登入</a></li>");
				out.print("<li><a href=\"./register.jsp\">註冊</a></li>");

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void checkAccountStatusWithAdministrator(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, JspWriter out) {
		try {
			//String path = request.getLocalAddr()+request.getContextPath();
			if (session.getAttribute("login") != null
					&& session.getAttribute("login").equals(1)) {
				out.print("<li>hi, "
						+ session.getAttribute("username") + "</li>");

				out.print("<li><a href=\"../account_manager/logout?return_url=_referer_\">登出</a></li>");
				out.print("<li><a href=\"../index.jsp\">回一般頁面</a></li>");

			} else {
				response.sendRedirect("../index.jsp");

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	public String getAccountTypeLabel(int type){
		if(type==1){
			return "店面 ";
		}
		else if(type==2){
			return "設計師";
		}
		else if(type==3){
			return "散戶";
		}
		else if(type==4){
			return "廠商";
		}
		else{
			return "其他";
		}
	}
	
	
	%>