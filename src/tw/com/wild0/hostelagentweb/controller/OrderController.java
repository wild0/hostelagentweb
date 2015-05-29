package tw.com.wild0.hostelagentweb.controller;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.orangice.sf.erp.constant.OrderConstant;
import tw.com.orangice.sf.erp.constant.TransactionConstant;
import tw.com.orangice.sf.erp.exception.OrderNotFoundException;
import tw.com.orangice.sf.erp.exception.SourceNotEnoughException;
import tw.com.orangice.sf.erp.manager.CustomerManager;
import tw.com.orangice.sf.erp.manager.EmployeeManager;
import tw.com.orangice.sf.erp.manager.ItemCategoryManager;
import tw.com.orangice.sf.erp.manager.ItemModelManager;
import tw.com.orangice.sf.erp.manager.OrderDetailManager;
import tw.com.orangice.sf.erp.manager.OrderManager;
import tw.com.orangice.sf.erp.manager.TransactionManager;
import tw.com.orangice.sf.erp.manager.UserManager;
import tw.com.orangice.sf.erp.manager.WarehouseManager;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemInstanceImpl;
import tw.com.orangice.sf.erp.model.item.item_model.impl.ItemModelImpl;
import tw.com.orangice.sf.erp.model.order_model.impl.OrderDetailModelImpl;
import tw.com.orangice.sf.erp.model.order_model.impl.OrderModelImpl;
import tw.com.orangice.sf.erp.model.user.impl.CustomerImpl;
import tw.com.orangice.sf.erp.model.user.impl.EmployeeImpl;
import tw.com.orangice.sf.erp.model.warehouse_model.impl.WarehouseModelImpl;
import tw.com.orangice.sf.erp.utility.DateUtility;
import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db._interface.DatabaseManagerInterface;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.constant.PostDataConstant;
import tw.com.wild0.hostelagentweb.utility.UrlUtility;

public class OrderController {
	
	
	OrderManager orderManager = null;
	OrderDetailManager orderDetailManager = null;
	EmployeeManager employeeManager = null;
	CustomerManager customerManager = null;
	WarehouseManager warehouseManager = null;
	//SerialNumberManager snManager = null;
	ItemModelManager itemManager = null;
	static OrderController instance = null;

	LogService logger = LogService.getInstance();

	public static OrderController getInstance(ServletContext sc)
			throws JSONException, ClassNotFoundException, SQLException {
		
		
		if (instance == null) {
			DatabaseManagerInterface dm;
			// try {
			dm = (DatabaseManagerInterface) sc.getAttribute(ContextConstant.DB_MANAGER);
			instance = new OrderController(dm);
			return instance;

		} else {
			return instance;
		}

	}
	
	public OrderController(DatabaseManagerInterface dm) {

		//accountManager = new AccountManager(dm);
		//userManager = new UserManager(dm);
		//itemCategoryManager = new ItemCategoryManager(dm);
		orderManager = new OrderManager(dm);
		orderDetailManager = new OrderDetailManager(dm);
		customerManager = new CustomerManager( dm);
		itemManager = new ItemModelManager( dm);
		warehouseManager = new WarehouseManager( dm);
		employeeManager = new EmployeeManager( dm);
		//snManager = new SerialNumberManager(dm);
		
	}
	public ArrayList<OrderModelImpl> listOrders(long startDate, long endDate) throws SQLException{
		
		ArrayList<OrderModelImpl> data = orderManager.listOrderModelsByDateRange(startDate, endDate, 0, 999);
		return data;
	}
	public ArrayList<ItemInstanceImpl> getAvailableSpaces(String hostelCode, long timeStart, long timeEnd) throws SQLException{
		//取出能用的房間或床位
		ArrayList<OrderDetailModelImpl> data = orderDetailManager.listOrderDetailsByNonStatusAndWithinDate(OrderConstant.ORDER_DETAIL_STATUS_CANCEL,  timeStart, timeEnd);
		ArrayList<ItemInstanceImpl> spaceData = itemManager.listItemInstancesByHomeCode(hostelCode);
		
		System.out.println("data size :"+data.size());
		System.out.println("spaceData size :"+spaceData.size());
		
		for(int j=0;j<data.size();j++){
			for(int i=0;i<spaceData.size();i++){
			
				if(spaceData.get(i).getInstanceCode().equals(data.get(j).getItemCode())){
					spaceData.remove(i);
					break;
				}
			}
		}
		return spaceData;
		
	}
	public int arrange(String customerCode, long orderId, int person, String hostelCode, long timeStart, long timeEnd) throws SQLException, SourceNotEnoughException{
		
		//customerManager.create(uname, customerName, email, address, phone, type)
		
		//orderManager.create(amount, date, status, classification, signatureCode)
		System.out.println("space available from :"+timeStart+"~"+timeEnd);
		ArrayList<ItemInstanceImpl> data = getAvailableSpaces(hostelCode, timeStart, timeEnd);
		System.out.println("space available:"+data.size());
		
		if(data.size()>person){
			//剩餘的房間數大於人數
			System.out.println("createOrder:automatically:剩餘的房間數大於人數");
			for(int i=0;i<person;i++){
				ItemInstanceImpl space = data.get(i);
				ItemModelImpl item = itemManager.getByCode(space.getItemCode());
				OrderDetailModelImpl detail = orderDetailManager.create(orderId, space.getItemCode(), item.getOutCost(), OrderConstant.ORDER_CLASSIFICATION_RENTAL);
				detail.setStartTime(timeStart);
				detail.setEndTime(timeEnd);
				detail.setType(OrderConstant.ORDER_DETAIL_TYPE_INSTANCE_RENTAL);
				
				orderDetailManager.addRentalOrderDetail(detail, 
						TransactionConstant.OBJECT_TYPE_WAREHOUSE_CODE, 
						hostelCode, 
						TransactionConstant.OBJECT_TYPE_CUSTOMER_CODE, 
						customerCode,
						"");
				System.out.println("createOrder:automatically:剩餘的房間數大於人數:addRentalOrderDetail");
				
			}
			
			return 200;
		}
		else{
			//人數大於房間數
			
			return 404;
		}
		
		
	}
	
	public ArrayList<OrderDetailModelImpl> getOrderDetails(String orderCode) throws SQLException, OrderNotFoundException{
		ArrayList<OrderDetailModelImpl> data = orderDetailManager.listOrderDetailsByOrderCode(orderCode);
		return data;
	}
	
	
	public JSONObject createOrder(HttpServletRequest req){
		JSONObject jsonObj = new JSONObject();
		try{
			//String tenantCode = req.getParameter("tenant_code");
			String employeeCode = req.getParameter("employee_code");
			String hostelCode = req.getParameter("hostel_code");
			//String customerCode = req.getParameter("customer_code");
			String customerName = req.getParameter("customer_name");
			String customerIdentity = req.getParameter("customer_identity");
			String customerEmail = req.getParameter("customer_email");
			String customerMobile = req.getParameter("customer_mobile");
			String personStr = req.getParameter("person_count");
			int person = Integer.parseInt(personStr);
			
			String arrangeStr = req.getParameter("space_arrange");
			
			System.out.println("customer_name:"+customerName);
			System.out.println("customer_identity:"+customerIdentity);
			System.out.println("customer_email:"+customerEmail);
			System.out.println("customer_mobile:"+customerMobile);
			
			CustomerImpl customer = customerManager.create(customerEmail, customerName, customerEmail, customerIdentity, "", customerMobile, 0);
			customer.setIdentity(customerIdentity);
			customer = customerManager.addCustomer(customer);
			
			
			
			
			String startTimeStr = req.getParameter("reservation_start_time");
			String endTimeStr = req.getParameter("reservation_end_time");
			
			//long startTime = Long.parseLong(startTimeStr);
			//long endTime = Long.parseLong(endTimeStr);
			
			Date startTime = DateUtility.convertTimeStrToDate(startTimeStr, "MM/dd/yyyy");
			Date endTime = DateUtility.convertTimeStrToDate(endTimeStr, "MM/dd/yyyy");
			
			EmployeeImpl employee = employeeManager.getEmployeeByEmployeeCode(employeeCode);
			//CustomerImpl customer = customerManager.getCustomerByCustomerCode(customerCode);
			
			OrderModelImpl order = createOrder("", employee, customer);
			
			if(arrangeStr.equals("automatically")){
				System.out.println("createOrder:automatically");
				int result = arrange(customer.getCustomerCode(), order.getId(), person, hostelCode, startTime.getTime(), endTime.getTime());
				ArrayList<OrderDetailModelImpl> details = getOrderDetails(order.getCode());
			}
			
			
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			return jsonObj;
		}
	}
	
	
	public OrderModelImpl createOrder(String tenantCode, EmployeeImpl employee, CustomerImpl customer) throws SQLException{
		int objectType = OrderConstant.ORDER_OBJECT_TYPE_CUSTOMER;
		long objectId = customer.getId();
		String employeeCode = employee.getEmployeeCode();
		long date = System.currentTimeMillis();
		int status = OrderConstant.ORDER_STATUS_NON_COMPLETE;
		int classification = OrderConstant.ORDER_CLASSIFICATION_NORMAL;
		OrderModelImpl orderModel = OrderManager.create(0, date, status, classification, employeeCode, objectId, objectType);
		//orderModel.setTenantCode(tenantCode);
		orderModel = orderManager.addOrder(orderModel);
		return orderModel;
	}
	public JSONObject addOrderDetail(HttpServletRequest req) throws SQLException, SourceNotEnoughException{
		JSONObject jsonObj = new JSONObject();
		try{
			String orderCode = req.getParameter("order_code");
			String customerCode = req.getParameter("customer_code");
			String warehouseCode = req.getParameter("warehouse_code");
			String itemInstanceCode = req.getParameter("item_instance_code");
			
			String itemInstancePriceStr = req.getParameter("item_instance_price");
			String startRentalTimeStr = req.getParameter("start_rental_time");
			String endRentalTimeStr = req.getParameter("end_rental_time");
			
			String description = req.getParameter("description");
			
			
			OrderModelImpl order = orderManager.getOrderModelByCode(orderCode);
			CustomerImpl customer = customerManager.getCustomerByCustomerCode(customerCode);
			ItemInstanceImpl itemInstance = itemManager.getItemInstanceByCode(itemInstanceCode);
			WarehouseModelImpl warehouse = warehouseManager.getWarehouseByCode(warehouseCode);
			
			long startRentalTime = Long.getLong(startRentalTimeStr);
			long endRentalTime = Long.getLong(endRentalTimeStr);
			int itemInstancePrice = Integer.parseInt(itemInstancePriceStr);
			
			
			addOrderDetail(order, warehouse, customer, itemInstance, itemInstancePrice, startRentalTime, endRentalTime, description);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			return jsonObj;
		}
	
	}
	public OrderDetailModelImpl addOrderDetail(OrderModelImpl orderModel, 
			WarehouseModelImpl warehouse, 
			CustomerImpl customer,
			ItemInstanceImpl itemInstance, 
			int itemInstancePrice, 
			long startTime, 
			long endTime, 
			String description) throws SQLException, SourceNotEnoughException{
		int srcType = TransactionConstant.OBJECT_TYPE_WAREHOUSE_CODE;
		int dstType = TransactionConstant.OBJECT_TYPE_CUSTOMER_CODE;
		
		OrderDetailModelImpl detail = OrderDetailManager.createWithRentalItemInstance(orderModel.getId(), itemInstance.getItemCode(), itemInstancePrice, itemInstance.getInstanceCode());
		detail.setStartTime(startTime);
		detail.setEndTime(endTime);
		orderDetailManager.addOrderDetail(detail , srcType, warehouse.getCode(), dstType, customer.getCustomerCode(), description);
		return detail;
	
	}
	/*
	public ArrayList<OrderModelImpl> listOrdersByTenantCodeAndStatus(String tenantCode, int status, int start, int limit){
		ArrayList<OrderModelImpl> orders = new ArrayList<OrderModelImpl>();
		orders = orderManager.listOrderModelsByStatus(status, start, limit);
		return orders;
	}
	
	public ArrayList<OrderDetailModelImpl> listOrderDetails(){
		
		ArrayList<OrderDetailModelImpl> details = new ArrayList<OrderDetailModelImpl>();
		
		return details;
		
	}
	*/
	
	public void handler(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String returnUrl = req
					.getParameter(PostDataConstant.POST_RETURN_URL);
			returnUrl = UrlUtility.getUrl(req, returnUrl);
			// logger.info("returnUrl:" + returnUrl);
			if (req.getPathInfo().startsWith("/create_order")) {
				JSONObject response = createOrder(req);
				if (returnUrl == null) {
					// logger.info("OUTPUT MESSAGE:" + response.toString());
					final PrintWriter output = resp.getWriter();
					output.println(response.toString());
					output.close();
				} else {
					resp.sendRedirect(returnUrl);

				}
			} else if (req.getPathInfo().startsWith("/add_order_detail")) {
				JSONObject response = addOrderDetail(req);
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
