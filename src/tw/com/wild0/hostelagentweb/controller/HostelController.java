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
import tw.com.orangice.sf.erp.constant.TransactionConstant;
import tw.com.orangice.sf.erp.manager.ItemCategoryManager;
import tw.com.orangice.sf.erp.manager.ItemModelManager;
import tw.com.orangice.sf.erp.manager.TransactionManager;
import tw.com.orangice.sf.erp.manager.UserManager;
import tw.com.orangice.sf.erp.manager.WarehouseManager;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemCategoryImpl;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemInstanceImpl;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemModelImpl;
import tw.com.orangice.sf.erp.model.transaction_model.impl.TransactionModelImpl;
import tw.com.orangice.sf.erp.model.warehouse_model.impl.WarehouseModelImpl;
import tw.com.orangice.sf.erp.utility.EncryptUtility;
import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db._interface.DatabaseManagerInterface;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.constant.PostDataConstant;
import tw.com.wild0.hostelagentweb.constant.StatusCodeConstant;
import tw.com.wild0.hostelagentweb.utility.UrlUtility;

public class HostelController {
	static HostelController instance = null;

	WarehouseManager warehouseManager = null;
	TransactionManager transactionManager = null;
	ItemModelManager itemModelManager = null;

	LogService logger = LogService.getInstance();

	public static HostelController getInstance(ServletContext sc)
			throws JSONException, ClassNotFoundException, SQLException {
		
		
		if (instance == null) {
			DatabaseManagerInterface dm;
			// try {
			dm = (DatabaseManagerInterface) sc.getAttribute(ContextConstant.DB_MANAGER);
			instance = new HostelController(dm);
			return instance;

		} else {
			return instance;
		}

	}

	public HostelController(DatabaseManagerInterface dm) {

		//accountManager = new AccountManager(dm);
		//userManager = new UserManager(dm);
		warehouseManager = new WarehouseManager(dm);
		transactionManager = new TransactionManager(dm);
		itemModelManager = new ItemModelManager( dm);
		//snManager = new SerialNumberManager(dm);
		
	}
	public long addHostel(String name) throws SQLException{
		
		
		
		WarehouseModelImpl hostel = warehouseManager.create(name);
		return warehouseManager.addWarehouse(hostel).getId();
	}
	public int removeHostel(String code) throws SQLException{
		return warehouseManager.removeWarehouse(code);
	}
	public JSONObject generate(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			
			String hostelCode = req.getParameter("hostel_code");
			String itemCode = req.getParameter("item_code");
			String countStr = req.getParameter("count");
			
			WarehouseModelImpl hostel = warehouseManager.getWarehouseByCode(hostelCode);
			ItemModelImpl item = itemModelManager.getByCode(itemCode);
			
			int count = Integer.parseInt(countStr);
			
			
			int result = generateRoomInstance(hostel, item, count);
			
		}
		finally{
			return jsonObj;
		}
	}
	public int generateRoomInstance(WarehouseModelImpl hostel, ItemModelImpl room, int count) throws Exception{
		
		TransactionModelImpl trans =transactionManager.create(room.getCode(), count, TransactionConstant.TRANSACTION_STATUS_COMPLETE,
				TransactionConstant.TRANSACTION_TYPE_CREATE, 
				TransactionConstant.OBJECT_TYPE_OUTTER, 
				"", 
				TransactionConstant.OBJECT_TYPE_WAREHOUSE_CODE, 
				hostel.getCode(), 
				"");
		trans = transactionManager.addTransactionWithInstance(trans);
		
		
		
		return 1;
	}
	
	
	public ArrayList<WarehouseModelImpl> listHostels() throws SQLException{
		return warehouseManager.listWarehouses();
	}
	
	
	public JSONObject addHostel(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String hostelName = req.getParameter(PostDataConstant.POST_HOSTEL_NAME);
			addHostel(hostelName);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			return jsonObj;
		}
	}
	
	public JSONObject removeHostel(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String hostelCode = req.getParameter(PostDataConstant.POST_HOSTEL_CODE);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			return jsonObj;
		}
	}
	
	public JSONObject modifyHostel(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			
		}
		catch(Exception e){
			
		}
		finally{
			return jsonObj;
		}
	}
	
	
	
	
	public void handler(HttpServletRequest req, HttpServletResponse resp) {
		try {
			System.out.println("req.getPathInfo():"+req.getPathInfo());
			String returnUrl = req
					.getParameter(PostDataConstant.POST_RETURN_URL);
			returnUrl = UrlUtility.getUrl(req, returnUrl);
			//System.out.println("req.getPathInfo():"+req.getPathInfo());
			//logger.info("returnUrl:" + returnUrl);
			if (req.getPathInfo().startsWith("/add_hostel")) {
				JSONObject response = addHostel(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			} 
			else if (req.getPathInfo().startsWith("/generate")) {
				JSONObject response = generate(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			} 
			else if (req.getPathInfo().startsWith("/remove_hostel")) {
				JSONObject response = removeHostel(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			} 
			else if (req.getPathInfo().startsWith("/modify_hostel")) {
				JSONObject response = modifyHostel(req);
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
