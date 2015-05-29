package tw.com.wild0.hostelagentweb.controller;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.orangice.sf.erp.constant.ItemConstant;
import tw.com.orangice.sf.erp.constant.OrderConstant;
import tw.com.orangice.sf.erp.manager.ItemCategoryManager;
import tw.com.orangice.sf.erp.manager.ItemModelManager;
import tw.com.orangice.sf.erp.manager.OrderDetailManager;
import tw.com.orangice.sf.erp.manager.OrderManager;
import tw.com.orangice.sf.erp.manager.UserManager;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemCategoryImpl;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemInstanceImpl;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemModelImpl;
import tw.com.orangice.sf.erp.model.order_model.impl.OrderDetailModelImpl;
import tw.com.orangice.sf.erp.model.order_model.impl.OrderModelImpl;
import tw.com.orangice.sf.erp.model.transaction_model.impl.TransactionModelImpl;
import tw.com.orangice.sf.erp.model.user.impl.CustomerImpl;
import tw.com.orangice.sf.erp.model.user.impl.EmployeeImpl;
import tw.com.orangice.sf.erp.utility.EncryptUtility;
import tw.com.orangice.sf.erp.utility.ItemInstanceUtility;
import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db._interface.DatabaseManagerInterface;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.constant.PostDataConstant;
import tw.com.wild0.hostelagentweb.constant.RoomStatusConstant;
import tw.com.wild0.hostelagentweb.constant.StatusCodeConstant;
import tw.com.wild0.hostelagentweb.utility.UrlUtility;

public class RoomUnitController {
	static RoomUnitController instance = null;

	ItemCategoryManager itemCategoryManager = null;
	ItemModelManager itemModelManager = null;
	UserManager userManager = null;
	OrderManager orderManager = null;
	//SerialNumberManager snManager = null;

	LogService logger = LogService.getInstance();

	public static RoomUnitController getInstance(ServletContext sc)
			throws JSONException, ClassNotFoundException, SQLException {
		
		
		if (instance == null) {
			DatabaseManagerInterface dm;
			// try {
			dm = (DatabaseManagerInterface) sc.getAttribute(ContextConstant.DB_MANAGER);
			instance = new RoomUnitController(dm);
			return instance;

		} else {
			return instance;
		}

	}

	public RoomUnitController(DatabaseManagerInterface dm) {

		//accountManager = new AccountManager(dm);
		userManager = new UserManager( dm);
		itemCategoryManager = new ItemCategoryManager( dm);
		itemModelManager = new ItemModelManager(dm);
		orderManager = new OrderManager( dm);
		//snManager = new SerialNumberManager(dm);
		
	}
	public ItemModelImpl addRoom(ItemCategoryImpl cat, String name) throws Exception{
		
		int countable = ItemConstant.COUNTABLE;
		String unitLabel = "room";
		
		int safeStocks = 1000;
		int maxStocks = 1000;
		
		int type = ItemConstant.ITEM_TYPE_NONE;
		
		ItemModelImpl item = itemModelManager.create(name, unitLabel, unitLabel, cat.getCategoryCode(), safeStocks, maxStocks, 0, 0, countable, type);
		System.out.println("room name:"+name);
		
		item = itemModelManager.addItemModel(item);
		return item;
	
	}
	
	public ItemInstanceImpl addRoomInstance(ItemModelImpl itemModel){
		
		String instanceCode = EncryptUtility.getRandomEncrypt();
		
		ItemInstanceImpl itemInstance = itemModelManager.createInstance(itemModel, instanceCode);
		itemInstance.setStatus(ItemConstant.ITEM_INSTANCE_STATUS_NONE);
		
		return itemInstance;
		
	}
	
	
	public JSONObject addRoom(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String roomName = req.getParameter("room_name");
			String categoryCode = req.getParameter("category_code");
			
			ItemCategoryImpl category = itemCategoryManager.getByCode(categoryCode);
			ItemModelImpl item = addRoom(category, roomName);
			
			
		}
		catch(Exception e){
			
		}
		finally{
			return jsonObj;
		}
	}
	
	public JSONObject addRoomInstance(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			
		}
		catch(Exception e){
			
		}
		finally{
			return jsonObj;
		}
	}
	public ItemInstanceImpl rentRoomInstance(String instanceCode, int targetType, String targetCode) throws SQLException{
		ItemInstanceImpl itemInstance = itemModelManager.getItemInstanceByCode(instanceCode);
		itemInstance.setStatus(ItemConstant.ITEM_INSTANCE_STATUS_IN_RENT);
		itemInstance.setTargetType(targetType);
		itemInstance.setTargetCode(targetCode);
		
		itemModelManager.updateItemInstance(itemInstance);
		return itemInstance;
		
		//Hashtable data = ItemInstanceUtility.convertToHashtable(itemInstance);
		
		//itemModelManager.updateItemInstance(data, criteria)
	}
	
	public ItemInstanceImpl bookRoomInstance(String instanceCode, int targetType, String targetCode) throws SQLException{
		ItemInstanceImpl itemInstance = itemModelManager.getItemInstanceByCode(instanceCode);
		itemInstance.setStatus(RoomStatusConstant.ROOM_STATUS_RESERVATION);
		itemInstance.setTargetType(targetType);
		itemInstance.setTargetCode(targetCode);
		
		itemModelManager.updateItemInstance(itemInstance);
		
		
		
		
		
		return itemInstance;
	}
	
	
	public JSONObject modifyRoomInstance(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String instanceCode = req.getParameter("instance_code");
			String roomLabel = req.getParameter("room_label");
			
			int result = modifyRoomInstance(instanceCode, roomLabel);
			
		}
		catch(Exception e){
			
		}
		finally{
			return jsonObj;
		}
	}
	
	public int modifyRoomInstance(String instanceCode, String roomLabel) throws SQLException{
		ItemInstanceImpl room = itemModelManager.getItemInstanceByCode(instanceCode);
		
		room.setLabel(roomLabel);
		
		int result = itemModelManager.updateItemInstance(room);
		return result;
		
	}
	public ArrayList<ItemInstanceImpl> listRoomInstancesByItemCodeWithStatusAvailable(String itemCode) throws Exception{
		//ArrayList<ItemInstanceImpl> data = itemModelManager.listItemInstancesByItemCodeAndStatus(itemCode, RoomStatusConstant.ROOM_STATUS_AVAILABLE);
		return null;
	}
	public ArrayList<ItemInstanceImpl> listRoomInstancesByHostelCode(String hostelCode) throws Exception{
		ArrayList<ItemInstanceImpl> data = itemModelManager.listItemInstancesByHomeCode(hostelCode);
		return data;
	}
	public ItemInstanceImpl getRoomInstanceByCode(String instanceCode) throws SQLException{
		return itemModelManager.getItemInstanceByCode(instanceCode);
	}
	
	public ArrayList<ItemInstanceImpl> listRoomInstancesByItemCode(String itemCode) throws Exception{
		ArrayList<ItemInstanceImpl> data = itemModelManager.listItemInstancesByItemCode(itemCode);
		return data;
	}
	public ArrayList<ItemModelImpl> listRooms(String categoryCode) throws Exception{
		ArrayList<ItemModelImpl> data = itemModelManager.listItemModelsByCategoryCode(categoryCode);
		return data;
	}
	
	public ItemInstanceImpl checkInRoomInstance(String instanceCode, int targetType, String targetCode) throws SQLException{
		ItemInstanceImpl itemInstance = itemModelManager.getItemInstanceByCode(instanceCode);
		itemInstance.setStatus(RoomStatusConstant.ROOM_STATUS_USED);
		itemInstance.setTargetType(targetType);
		itemInstance.setTargetCode(targetCode);
		
		itemModelManager.updateItemInstance(itemInstance);
		
		TransactionModelImpl trans = new TransactionModelImpl();
		
		return itemInstance;
		
		//Hashtable data = ItemInstanceUtility.convertToHashtable(itemInstance);
		
		//itemModelManager.updateItemInstance(data, criteria)
	}
	public ItemInstanceImpl checkOutRoomInstance(String instanceCode, int targetType, String targetCode) throws SQLException{
		ItemInstanceImpl itemInstance = itemModelManager.getItemInstanceByCode(instanceCode);
		itemInstance.setStatus(RoomStatusConstant.ROOM_STATUS_AVAILABLE);
		itemInstance.setTargetType(targetType);
		itemInstance.setTargetCode(targetCode);
		
		itemModelManager.updateItemInstance(itemInstance);
		return itemInstance;
		
		//Hashtable data = ItemInstanceUtility.convertToHashtable(itemInstance);
		
		//itemModelManager.updateItemInstance(data, criteria)
	}
	public JSONObject rentRoomInstance(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String instanceCode = req.getParameter("item_instance_code");
			//String targetType = req.getParameter("item_instance_target_type");
			String targetCode = req.getParameter("item_instance_target_code");
			int targetType = ItemConstant.ITEM_INSTANCE_TARGET_TYPE_USER;
			ItemInstanceImpl itemInstance = rentRoomInstance(instanceCode, targetType, targetCode);
			
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
	public ItemInstanceImpl returnRoomInstance(String instanceCode, int targetType, String targetCode) throws SQLException{
		ItemInstanceImpl itemInstance = itemModelManager.getItemInstanceByCode(instanceCode);
		itemInstance.setStatus(RoomStatusConstant.ROOM_STATUS_AVAILABLE);
		itemInstance.setTargetType(targetType);
		itemInstance.setTargetCode(targetCode);
		
		itemModelManager.updateItemInstance(itemInstance);
		return itemInstance;
		
		//Hashtable data = ItemInstanceUtility.convertToHashtable(itemInstance);
		
		//itemModelManager.updateItemInstance(data, criteria)
	}
	
	public JSONObject returnRoomInstance(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			String instanceCode = req.getParameter("item_instance_code");
			//String targetType = req.getParameter("item_instance_target_type");
			String targetCode = req.getParameter("item_instance_target_code");
			int targetType = ItemConstant.ITEM_INSTANCE_TARGET_TYPE_USER;
			ItemInstanceImpl itemInstance = returnRoomInstance(instanceCode, targetType, targetCode);
			
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
			if (req.getPathInfo().startsWith("/add_room")) {
				JSONObject response = addRoom(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			} else if (req.getPathInfo().startsWith("/add_room_instance")) {
				JSONObject response = addRoomInstance(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			} 
			else if (req.getPathInfo().startsWith("/rent_room_instance")) {
				JSONObject response = rentRoomInstance(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			}
			else if (req.getPathInfo().startsWith("/return_room_instance")) {
				JSONObject response = returnRoomInstance(req);
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
