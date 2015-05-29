package tw.com.wild0.hostelagentweb.controller;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.orangice.sf.erp.constant.ItemConstant;
import tw.com.orangice.sf.erp.manager.CustomerManager;
import tw.com.orangice.sf.erp.manager.ItemCategoryManager;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemCategoryImpl;
import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db._interface.DatabaseManagerInterface;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.constant.PostDataConstant;
import tw.com.wild0.hostelagentweb.utility.UrlUtility;

public class CategoryController {
	ItemCategoryManager itemCategoryManager = null;
	LogService logger = LogService.getInstance();
	static CategoryController instance = null;

	public static CategoryController getInstance(ServletContext sc)
			throws JSONException, ClassNotFoundException, SQLException {
		
		
		if (instance == null) {
			DatabaseManagerInterface dm;
			// try {
			dm = (DatabaseManagerInterface) sc.getAttribute(ContextConstant.DB_MANAGER);
			instance = new CategoryController(dm);
			return instance;

		} else {
			return instance;
		}

	}

	public CategoryController(DatabaseManagerInterface dm) {

		//accountManager = new AccountManager(dm);
		itemCategoryManager = new ItemCategoryManager(dm);
		//snManager = new SerialNumberManager(dm);
		
	}
	
	public ArrayList<ItemCategoryImpl> listCategories() throws SQLException{
		return itemCategoryManager.listItemCategories();
	}
	
	public ItemCategoryImpl addCategory(String categoryName) throws Exception{
		ItemCategoryImpl category = itemCategoryManager.create(-1, categoryName, "", ItemConstant.ITEM_CATEGORY_TYPE_PRIVATE);
		category = itemCategoryManager.addCategory(category);
		return category;
	}
	
	public JSONObject addCategory(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String categoryName = req.getParameter("category_name");
			ItemCategoryImpl category = addCategory(categoryName);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			return jsonObj;
		}
	}
	public JSONObject removeCategory(HttpServletRequest req){
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
			if (req.getPathInfo().startsWith("/add_category")) {
				JSONObject response = addCategory(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			}else if (req.getPathInfo().startsWith("/remove_category")) {
				// logger.info("INPUT COMMAND:"+PostDataConstant.COMMAND_INSERT_ITEM_CATEGORY);
				JSONObject result = removeCategory(req);
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
