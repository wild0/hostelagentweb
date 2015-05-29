package tw.com.wild0.hostelagentweb.controller;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.orangice.sf.erp.manager.CustomerManager;
import tw.com.orangice.sf.erp.manager.EmployeeManager;
import tw.com.orangice.sf.erp.model.user.impl.EmployeeImpl;
import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db._interface.DatabaseManagerInterface;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.constant.PostDataConstant;
import tw.com.wild0.hostelagentweb.utility.UrlUtility;

public class EmployeeController {
	EmployeeManager employeeManager = null;
	
	
	
	LogService logger = LogService.getInstance();
	static EmployeeController instance = null;

	public static EmployeeController getInstance(ServletContext sc)
			throws JSONException, ClassNotFoundException, SQLException {
		
		
		if (instance == null) {
			DatabaseManagerInterface dm;
			// try {
			dm = (DatabaseManagerInterface) sc.getAttribute(ContextConstant.DB_MANAGER);
			instance = new EmployeeController(dm);
			return instance;

		} else {
			return instance;
		}

	}
	public ArrayList<EmployeeImpl> listEmployees(int start, int limit)
			throws SQLException {

		ArrayList<EmployeeImpl> employees = employeeManager.listEmployees();

		return employees;

	}

	public EmployeeController(DatabaseManagerInterface dm) {

		//accountManager = new AccountManager(dm);
		employeeManager = new EmployeeManager(dm);
		//snManager = new SerialNumberManager(dm);
		
	}
	
	public EmployeeImpl addEmployee(String uname, String email) throws SQLException{
		
		EmployeeImpl employee = employeeManager.create(uname, email, uname);
		employee = employeeManager.addEmployee(employee);
		return employee;
	}
	public JSONObject addEmployee(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String employeeUname = req.getParameter("employee_uname");
			String employeeEmail = req.getParameter("employee_email");
			
			EmployeeImpl employee = addEmployee(employeeUname, employeeEmail);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			return jsonObj;
		}
	}
	public JSONObject removeEmployee(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			return jsonObj;
		}
	}
	
	public void handler(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String returnUrl = req
					.getParameter(PostDataConstant.POST_RETURN_URL);
			returnUrl = UrlUtility.getUrl(req, returnUrl);
			//logger.info("returnUrl:" + returnUrl);
			System.out.println(req.getPathInfo());
			if (req.getPathInfo().startsWith("/add_employee")) {
				JSONObject response = addEmployee(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			}else if (req.getPathInfo().startsWith("/remove_employee")) {
				// logger.info("INPUT COMMAND:"+PostDataConstant.COMMAND_INSERT_ITEM_CATEGORY);
				JSONObject result = removeEmployee(req);
				if (returnUrl != null) {
					// logger.info("REDIRECT URL TO:" + returnUrl);
					resp.sendRedirect(returnUrl);
				} else {
					// logger.info("OUTPUT MESSAGE:" + result.toString());
					final PrintWriter output = resp.getWriter();
					output.println(result.toString());
					output.close();
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
