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
import tw.com.orangice.sf.erp.manager.ItemCategoryManager;
import tw.com.orangice.sf.erp.manager.UserManager;
import tw.com.orangice.sf.erp.model.user.impl.CustomerImpl;
import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db._interface.DatabaseManagerInterface;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.constant.PostDataConstant;
import tw.com.wild0.hostelagentweb.utility.UrlUtility;

public class CustomerController {
	CustomerManager customerManager = null;
	LogService logger = LogService.getInstance();
	static CustomerController instance = null;

	public static CustomerController getInstance(ServletContext sc)
			throws JSONException, ClassNotFoundException, SQLException {
		
		
		if (instance == null) {
			DatabaseManagerInterface dm;
			// try {
			dm = (DatabaseManagerInterface) sc.getAttribute(ContextConstant.DB_MANAGER);
			instance = new CustomerController(dm);
			return instance;

		} else {
			return instance;
		}

	}
	public ArrayList<CustomerImpl> listCustomers(int start, int limit)
			throws SQLException {

		ArrayList<CustomerImpl> customers = customerManager.listCustomers(
				start, limit);

		return customers;

	}

	public CustomerController(DatabaseManagerInterface dm) {

		//accountManager = new AccountManager(dm);
		customerManager = new CustomerManager(dm);
		//snManager = new SerialNumberManager(dm);
		
	}
	
	public JSONObject addCustomer(HttpServletRequest req){
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
	public JSONObject removeCustomer(HttpServletRequest req){
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
			// logger.info("returnUrl:" + returnUrl);
			if (req.getPathInfo().startsWith("/add_customer")) {
				JSONObject response = addCustomer(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			}else if (req.getPathInfo().startsWith("/remove_customer")) {
				// logger.info("INPUT COMMAND:"+PostDataConstant.COMMAND_INSERT_ITEM_CATEGORY);
				JSONObject result = removeCustomer(req);
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
