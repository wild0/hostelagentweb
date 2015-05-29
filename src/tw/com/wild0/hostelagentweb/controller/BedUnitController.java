package tw.com.wild0.hostelagentweb.controller;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.orangice.sf.erp.constant.ItemConstant;
import tw.com.orangice.sf.erp.manager.CustomerManager;
import tw.com.orangice.sf.erp.manager.ItemCategoryManager;
import tw.com.orangice.sf.erp.manager.ItemModelManager;
import tw.com.orangice.sf.erp.manager.UserManager;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemCategoryImpl;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemInstanceImpl;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemModelImpl;
import tw.com.orangice.sf.erp.utility.EncryptUtility;
import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db._interface.DatabaseManagerInterface;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.constant.PostDataConstant;
import tw.com.wild0.hostelagentweb.constant.StatusCodeConstant;
import tw.com.wild0.hostelagentweb.utility.UrlUtility;



public class BedUnitController {

	static BedUnitController instance = null;

	ItemCategoryManager itemCategoryManager = null;
	ItemModelManager itemModelManager = null;
	CustomerManager customerManager = null;
	//SerialNumberManager snManager = null;

	LogService logger = LogService.getInstance();

	public static BedUnitController getInstance(ServletContext sc)
			throws JSONException, ClassNotFoundException, SQLException {
		
		
		if (instance == null) {
			DatabaseManagerInterface dm;
			// try {
			dm = (DatabaseManagerInterface) sc.getAttribute(ContextConstant.DB_MANAGER);
			instance = new BedUnitController(dm);
			return instance;

		} else {
			return instance;
		}

	}
	
	

	public BedUnitController(DatabaseManagerInterface dm) {

		//accountManager = new AccountManager(dm);
		customerManager = new CustomerManager(dm);
		itemCategoryManager = new ItemCategoryManager(dm);
		itemModelManager = new ItemModelManager( dm);
		//snManager = new SerialNumberManager(dm);
		
	}
	public JSONObject addBed(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			
		}
		catch(Exception e){
			
		}
		finally{
			return jsonObj;
		}
	}
	
	public void addBed(ItemCategoryImpl cat, String name){
		
		int countable = ItemConstant.COUNTABLE;
		String unitLabel = "bed";
		int safeStocks = 1000;
		int maxStocks = 1000;
		int type = ItemConstant.ITEM_TYPE_NONE;
		
		ItemModelImpl item = itemModelManager.create(name, unitLabel, unitLabel, cat.getCategoryCode(), safeStocks, maxStocks, 0, 0, countable, type);
	}
	
	
	public JSONObject addBedInstance(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			
		}
		catch(Exception e){
			
		}
		finally{
			return jsonObj;
		}
	}
	
	public ItemInstanceImpl addBedInstance(ItemModelImpl itemModel){
		
		String instanceCode = EncryptUtility.getRandomEncrypt();
		
		ItemInstanceImpl itemInstance = itemModelManager.createInstance(itemModel, instanceCode);
		
		return itemInstance;
		
	}
	public ItemInstanceImpl returnBedInstance(String instanceCode, int targetType, String targetCode) throws SQLException{
		ItemInstanceImpl itemInstance = itemModelManager.getItemInstanceByCode(instanceCode);
		itemInstance.setStatus(ItemConstant.ITEM_INSTANCE_STATUS_IN_RENT);
		itemInstance.setTargetType(targetType);
		itemInstance.setTargetCode(targetCode);
		
		itemModelManager.updateItemInstance(itemInstance);
		return itemInstance;
		
		//Hashtable data = ItemInstanceUtility.convertToHashtable(itemInstance);
		
		//itemModelManager.updateItemInstance(data, criteria)
	}
	
	public ItemInstanceImpl rentBedInstance(String instanceCode, int targetType, String targetCode) throws SQLException{
		ItemInstanceImpl itemInstance = itemModelManager.getItemInstanceByCode(instanceCode);
		itemInstance.setStatus(ItemConstant.ITEM_INSTANCE_STATUS_IN_RENT);
		itemInstance.setTargetType(targetType);
		itemInstance.setTargetCode(targetCode);
		
		itemModelManager.updateItemInstance(itemInstance);
		return itemInstance;
		
		//Hashtable data = ItemInstanceUtility.convertToHashtable(itemInstance);
		
		//itemModelManager.updateItemInstance(data, criteria)
	}
	
	public JSONObject rentBedInstance(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String instanceCode = req.getParameter("item_instance_code");
			//String targetType = req.getParameter("item_instance_target_type");
			String targetCode = req.getParameter("item_instance_target_code");
			int targetType = ItemConstant.ITEM_INSTANCE_TARGET_TYPE_USER;
			ItemInstanceImpl itemInstance = rentBedInstance(instanceCode, targetType, targetCode);
			
			jsonObj.put("status_code", StatusCodeConstant.STATUS_CODE_RENT_ROOM_SUCCESS);
			
		}
		catch(Exception e){
			e.printStackTrace();
			jsonObj.put("status_code", StatusCodeConstant.STATUS_CODE_RENT_ROOM_FAIL);
		}
		finally{
			return jsonObj;
		}
	}
	
	public JSONObject returnBedInstance(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String instanceCode = req.getParameter("item_instance_code");
			//String targetType = req.getParameter("item_instance_target_type");
			String targetCode = req.getParameter("item_instance_target_code");
			int targetType = ItemConstant.ITEM_INSTANCE_TARGET_TYPE_USER;
			ItemInstanceImpl itemInstance = returnBedInstance(instanceCode, targetType, targetCode);
			
			jsonObj.put("status_code", StatusCodeConstant.STATUS_CODE_RENT_ROOM_SUCCESS);
			
		}
		catch(Exception e){
			e.printStackTrace();
			jsonObj.put("status_code", StatusCodeConstant.STATUS_CODE_RENT_ROOM_FAIL);
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
			if (req.getPathInfo().startsWith("/add_bed")) {
				JSONObject response = addBed(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			} else if (req.getPathInfo().startsWith("/add_bed_instance")) {
				JSONObject response = addBedInstance(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			} 
			else if (req.getPathInfo().startsWith("/rent_bed_instance")) {
				JSONObject response = rentBedInstance(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			}
			else if (req.getPathInfo().startsWith("/return_bed_instance")) {
				JSONObject response = returnBedInstance(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
