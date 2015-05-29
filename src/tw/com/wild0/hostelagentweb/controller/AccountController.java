package tw.com.wild0.hostelagentweb.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.orangice.sf.erp.manager.AccountManager;
import tw.com.orangice.sf.erp.manager.CustomerManager;
import tw.com.orangice.sf.erp.manager.EmployeeManager;
import tw.com.orangice.sf.erp.manager.UserManager;
import tw.com.orangice.sf.erp.model.token.impl.AccessTokenImpl;
import tw.com.orangice.sf.erp.model.user.impl.CustomerImpl;
import tw.com.orangice.sf.erp.model.user.impl.EmployeeImpl;
import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db._interface.DatabaseManagerInterface;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.constant.PostDataConstant;
import tw.com.wild0.hostelagentweb.exception.UserLoginFailException;
import tw.com.wild0.hostelagentweb.utility.FileUtility;
import tw.com.wild0.hostelagentweb.utility.UrlUtility;

public class AccountController {
	static AccountController instance = null;

	AccountManager accountManager = null;
	UserManager userManager = null;
	CustomerManager customerManager = null;
	EmployeeManager employeeManager = null;

	LogService logger = LogService.getInstance();

	public static AccountController getInstance(ServletContext sc)
			throws JSONException, ClassNotFoundException, SQLException {
		if (instance == null) {
			DatabaseManagerInterface dm;
			// try {
			dm = (DatabaseManagerInterface) sc.getAttribute(ContextConstant.DB_MANAGER);
			instance = new AccountController(dm);
			return instance;

		} else {
			return instance;
		}

	}

	public AccountController(DatabaseManagerInterface dm) {

		accountManager = new AccountManager( dm);
		userManager = new UserManager(dm);
		customerManager = new CustomerManager( dm);
		employeeManager = new EmployeeManager( dm);

	}

	public JSONObject forgetPassword(HttpServletRequest req) {
		JSONObject jsonObj = new JSONObject();
		return jsonObj;
	}

	public JSONObject resetPassword(HttpServletRequest req) {
		JSONObject jsonObj = new JSONObject();
		return jsonObj;
	}

	public JSONObject login(HttpServletRequest req) throws JSONException,
			UserLoginFailException {

		JSONObject jsonObj = new JSONObject();

		String username = req
				.getParameter(PostDataConstant.POST_LOGIN_USERNAME);
		String password = req
				.getParameter(PostDataConstant.POST_LOGIN_PASSWORD);

		try {
			// logger.info("[login input]");
			// logger.info("username:" + username);
			// logger.info("password:" + password);
			// logger.info("[login end]");

			JSONObject config = loadConfig(req);
			System.out.println("config:"+config.toString());

			if (config.getString("username").equals(username)
					&& config.getString("password").equals(password)) {
				HttpSession session = req.getSession(true);

				session.setAttribute("login", 1);

				session.setAttribute("token", "xxxxxxxxx");
				session.setAttribute("code", "xxxxxxxxx");
				session.setAttribute("username", "admin");
				session.setAttribute("permission", 999);
				session.setAttribute("realname", "admin");
				// session.setAttribute("ticket", username);

				jsonObj.put("status_code", 201);
			} else {

				AccessTokenImpl token = accountManager
						.login(username, password);
				// logger.info("[login]:ticket:"+token.getTid());

				// AccountImpl account =
				// accountManager.getByAccountId(token.getUid());
				//
				// UserImpl user = userManager

				EmployeeImpl employee = employeeManager.getEmployeeByUid(token
						.getUid());

				CustomerImpl customer = customerManager.getCustomerByUid(token
						.getUid());

				// logger.info("[login]:permission:");
				// logger.info("[login end]");
				HttpSession session = req.getSession(true);

				if (employee != null) {
					session.setAttribute("login", 1);
					session.setAttribute("role", "employee");
					session.setAttribute("code", employee.getEmployeeCode());
					session.setAttribute("token", token.getToken());
					session.setAttribute("permission", 999);
				} else if (customer != null) {
					// logger.info("login code:"+customer.getCustomerCode());
					session.setAttribute("login", 1);
					session.setAttribute("role", "customer");
					session.setAttribute("code", customer.getCustomerCode());
					session.setAttribute("token", token.getToken());
					session.setAttribute("permission", 999);
				} else {
					session.setAttribute("login", 0);
				}
				/*
				 * session.setAttribute("code", employee.getCode());
				 * session.setAttribute("ext_code", employee.getExtCode());
				 * session.setAttribute("username", );
				 * session.setAttribute("permission", employee.getPermission());
				 * session.setAttribute("realname", user.getRealname());
				 */
				// session.setAttribute("ticket", username);

				jsonObj.put("status_code", 200);

				JSONObject dataJson = new JSONObject();
				dataJson.put("token", token.getToken());
				dataJson.put("email", employee.getEmail());

				jsonObj.put("data", dataJson);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			jsonObj.put("status_code", 322);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObj.put("status_code", 406);
		} finally {
			jsonObj.put("command", PostDataConstant.COMMAND_LOGIN_USER);
			jsonObj.put("date", System.currentTimeMillis());
			// logger.info("insertMeeting output:" + jsonObj.toString());
			return jsonObj;
		}

	}

	public JSONObject logout(HttpServletRequest req) {
		JSONObject jsonObj = new JSONObject();
		HttpSession session = req.getSession(true);

		String username = (String) session.getAttribute("username");

		// userManager.logout(username);

		session.removeAttribute("login");
		session.removeAttribute("code");
		session.removeAttribute("username");
		session.removeAttribute("permission");
		session.removeAttribute("realname");
		session.removeAttribute("ext_code");

		return jsonObj;
	}

	public void handler(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String returnUrl = req
					.getParameter(PostDataConstant.POST_RETURN_URL);
			returnUrl = UrlUtility.getUrl(req, returnUrl);
			// logger.info("returnUrl:" + returnUrl);
			if (req.getPathInfo().startsWith("/logout")) {
				JSONObject response = logout(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);
					/*
					 * int statusCode = response.getInt("status_code");
					 * if(statusCode==200){ resp.sendRedirect(returnUrl); }
					 * else{
					 * resp.sendRedirect("../login.jsp?status_code="+statusCode
					 * ); }
					 */
				}
			} else if (req.getPathInfo().startsWith("/login")) {
				JSONObject response = login(req);

				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					int statusCode = response.getInt("status_code");
					if (statusCode == 200 || statusCode == 201) {
						resp.sendRedirect(returnUrl);
					} else {
						resp.sendRedirect("../login.jsp?status_code="
								+ statusCode);
					}
				}
			}
			/*
			 * else if (req.getPathInfo().startsWith("/forget_password")) {
			 * JSONObject response = forgetPassword(req); if (returnUrl == null)
			 * { logger.info("OUTPUT MESSAGE:" + response.toString()); final
			 * PrintWriter output = resp.getWriter();
			 * output.println(response.toString()); output.close(); } else { int
			 * statusCode = response.getInt("status_code"); if (statusCode ==
			 * 200 || statusCode == 201) { resp.sendRedirect(returnUrl); } else
			 * { resp.sendRedirect("../login.jsp?status_code=" + statusCode); }
			 * } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JSONObject loadConfig(HttpServletRequest req) throws IOException,
			JSONException {
		ServletContext sc = req.getServletContext();
		String webAppPath = sc.getRealPath("/");
		File userConfig = new File(webAppPath, "WEB-INF/config.json");
		String configBuffer = FileUtility.read(userConfig);
		JSONObject configJson = new JSONObject(configBuffer);
		return configJson;
	}
}
