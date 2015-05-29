package tw.com.wild0.hostelagentweb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.json.JSONException;
import org.json.JSONObject;

import tw.com.orangice.sf.lib.db.DatabaseInitiate;
import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db.MongoDatabaseManager;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.orangice.sf.lib.utility.DatabaseUtility;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.constant.PostDataConstant;
import tw.com.wild0.hostelagentweb.utility.UrlUtility;

public class DatabaseController {
	static DatabaseController instance = null;
	static LogService logger = LogService.getInstance();
	static DatabaseManager dm = null;
	//LogService logger = LogService.getInstance();
	
	public static DatabaseController getInstance(){
		if(instance==null){
			
			instance = new DatabaseController();
		}
		else {
			return instance;
		}
		return instance;
	}
	
	public static DatabaseManager getDefaultDatabaseManager(){
		return dm;
	}
	public static JSONObject resetDatabaseManager(String path){
		
		JSONObject jsonObj = new JSONObject();
		initTableOperation(path);
		dropTableOperation(path);
		return jsonObj;
	}
	public static void initialDatabaseManager(String path){
		try {
			dm = getDefaultDatabaseManager(path);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dm = null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dm = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dm = null;
		}
	}
	public static MongoDatabaseManager getMongoDatabaseManager(String path) throws JSONException{
		JSONObject jsonObj = loadDatabaseSetting(path);

		String host = jsonObj.getString("database_host");
		String name = jsonObj.getString("database_name");
		String username = jsonObj.getString("database_username");
		String password = jsonObj.getString("database_password");

		//String log4jLocation = "WEB-INF/log4j.properties";

		//String log4jProp = path + log4jLocation;

		//LogUtility.initial(log4jProp);

		//Connection conn = DatabaseUtility.getConnection(host, 3306, username,
		//		password, name);
		//DatabaseManager dm = new DatabaseManager(LogService.getInstance(), conn);
		MongoDatabaseManager mdm = new MongoDatabaseManager(logger, host, 27017, username,
				password, name);
		//System.out.println("DATASOURCE:"+ds.getDriverClassName());
		//DatabaseManager dm = new DatabaseManager(logger, ds);
		return mdm;
	}
	public static DatabaseManager getDefaultDatabaseManager(String path)
			throws JSONException, ClassNotFoundException, SQLException {
		JSONObject jsonObj = loadDatabaseSetting(path);

		String host = jsonObj.getString("database_host");
		String name = jsonObj.getString("database_name");
		String username = jsonObj.getString("database_username");
		String password = jsonObj.getString("database_password");

		//String log4jLocation = "WEB-INF/log4j.properties";

		//String log4jProp = path + log4jLocation;

		//LogUtility.initial(log4jProp);

		//Connection conn = DatabaseUtility.getConnection(host, 3306, username,
		//		password, name);
		//DatabaseManager dm = new DatabaseManager(LogService.getInstance(), conn);
		DataSource ds = DatabaseUtility.getTomcatDataSource(host, 3306, username,
				password, name);
		System.out.println("DATASOURCE:"+ds.getDriverClassName());
		DatabaseManager dm = new DatabaseManager(logger, ds);
		return dm;
	}
	public static JSONObject saveDatabaseSetting(HttpServletRequest req) {

		JSONObject jsonObj = new JSONObject();
		ServletContext sc = req.getServletContext();
		String webAppPath = sc.getRealPath("/");
		String path = webAppPath +"WEB-INF/database.properties";
		
		try {
			Properties p = new Properties();
			File file = new File(path);
			FileOutputStream fs = new FileOutputStream(file);
			//logger.info("PROPERTIES URL:" + webAppPath + "database.properties");

			p.store(new FileOutputStream(file), null);

			String host = req.getParameter("database_host");
			String name = req.getParameter("database_name");
			String username = req.getParameter("database_username");
			String password = req.getParameter("database_password");

			//logger.info("database_host:" + host);
			//logger.info("database_name:" + name);
			//logger.info("database_username:" + username);
			//logger.info("database_password:" + password);

			p.setProperty("database_host", host);
			p.setProperty("database_name", name);
			p.setProperty("database_username", username);
			p.setProperty("database_password", password);

			p.store(fs, null);
			fs.close();
			// System.out.println("Operation completly successfuly!");
		}

		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return jsonObj;
	}
	public static JSONObject loadDatabaseSetting(String path) {
		JSONObject jsonObj = new JSONObject();
		Properties prop = new Properties();
		FileInputStream filestream = null;
		try {

			System.out.println(path );
			
			filestream = new FileInputStream(path );
			prop.load(filestream);

			jsonObj.put("database_host", prop.getProperty("database_host"));
			jsonObj.put("database_name", prop.getProperty("database_name"));
			jsonObj.put("database_username",
					prop.getProperty("database_username"));
			jsonObj.put("database_password",
					prop.getProperty("database_password"));

			filestream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObj;

	}
	public static JSONObject createDatabase(String path) {
		JSONObject jsonObj = new JSONObject();
		try {
			JSONObject dbInfo = loadDatabaseSetting(path);

			String host = dbInfo.getString("database_host");
			String username = dbInfo.getString("database_username");
			String password = dbInfo.getString("database_password");
			String database = dbInfo.getString("database_name");
			
			DatabaseInitiate.createDatabase(LogService.getInstance(), host, 3306, username,
					password, database);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return jsonObj;
		}
	}
	public static JSONObject initTableOperation(String path) {
		JSONObject jsonObj = new JSONObject();
		try {
			JSONObject dbInfo = loadDatabaseSetting(path);

			String host = dbInfo.getString("database_host");
			String username = dbInfo.getString("database_username");
			String password = dbInfo.getString("database_password");
			String database = dbInfo.getString("database_name");
			DatabaseInitiate init = new DatabaseInitiate(LogService.getInstance(), host, 3306,
					username, password, database);
			//DatabaseInit init = new DatabaseInit(logger, host, 3306, username,
			//		password, database);
			File sqlSchema = new File( path + "WEB-INF/db.sql");
			init.initTable(sqlSchema);
			//init.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			return jsonObj;
		}

	}
	public static JSONObject dropTableOperation(String path) {
		JSONObject jsonObj = new JSONObject();
		try {
			JSONObject dbInfo = loadDatabaseSetting(path);

			String host = dbInfo.getString("database_host");
			String username = dbInfo.getString("database_username");
			String password = dbInfo.getString("database_password");
			String database = dbInfo.getString("database_name");

			//DatabaseInit init = new DatabaseInit(logger, host, 3306, username,
			//		password, database);
			DatabaseInitiate init = new DatabaseInitiate(LogService.getInstance(), host, 3306,
					username, password, database);
			File sqlSchema = new File( path + "WEB-INF/drop_db.sql");
			init.dropTable(sqlSchema);
			//init.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			return jsonObj;
		}

	}
	
	public static JSONObject checkDatabaseSetting(String path) {
		JSONObject jsonObj = new JSONObject();
		try {
			JSONObject dbInfo = loadDatabaseSetting(path+ "WEB-INF/database.properties");

			String host = dbInfo.getString("database_host");
			String username = dbInfo.getString("database_username");
			String password = dbInfo.getString("database_password");
			String database = dbInfo.getString("database_name");

			int result = checkDatabaseSetting(host, 3306, username, password,
					database);

			if (result == 1) {
				jsonObj.put("database_check_error_code", 200);
				jsonObj.put("database_check_description", "Check success");
			} else if (result == -1) {
				jsonObj.put("database_check_error_code", 404);
				jsonObj.put("database_check_description", "Check fail");
			}
		}

		finally {
			return jsonObj;
		}
	}
	public static int checkDatabaseSetting(String host, int port,
			String username, String password, String database) {

		try {
			Connection conn = DatabaseUtility.getConnection(host, port, username,
					password, database);
			return 1;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		// DatabaseUtility.checkDatabase(conn, database);

	}
	public void handler(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		//String returnUrl = req.getParameter(PostDataConstant.POST_RETURN_URL);
		
		String returnUrl = req.getParameter(PostDataConstant.POST_RETURN_URL);
		returnUrl = UrlUtility.getUrl(req, returnUrl);
		
		//logger.info("REDIRECT URL:" + req.getPathInfo() + "=>" + returnUrl);

		if (req.getPathInfo().equals("/initial_database")) {
			ServletContext sc = req.getServletContext();
			String webAppPath = sc.getRealPath("/");
			
			JSONObject response = DatabaseController.createDatabase(webAppPath);
			if (returnUrl == null) {
				//logger.info("OUTPUT MESSAGE:" + response.toString());
				final PrintWriter output = resp.getWriter();
				output.println(response.toString());
				output.close();
			} else {
				resp.sendRedirect(returnUrl);
			}
			
			
		} else if (req.getPathInfo().equals("/reset_database")) {
			ServletContext sc = req.getServletContext();
			String webAppPath = sc.getRealPath("/");
			
			JSONObject response = DatabaseController.resetDatabaseManager(webAppPath);
			
			if (returnUrl == null) {
				//logger.info("OUTPUT MESSAGE:" + response.toString());
				final PrintWriter output = resp.getWriter();
				output.println(response.toString());
				output.close();
			} else {
				resp.sendRedirect(returnUrl);
			}
			
			
		} else if (req.getPathInfo().equals("/clear_database")) {
			ServletContext sc = req.getServletContext();
			String webAppPath = sc.getRealPath("/");
			
			JSONObject response = DatabaseController.clearTableOperation(webAppPath);
			
			if (returnUrl == null) {
				//logger.info("OUTPUT MESSAGE:" + response.toString());
				final PrintWriter output = resp.getWriter();
				output.println(response.toString());
				output.close();
			} else {
				resp.sendRedirect(returnUrl);
			}
			
			
		}  else if (req.getPathInfo().equals("/save_database_setting")) {
			JSONObject response = DatabaseController.saveDatabaseSetting(req);
			if (returnUrl == null) {
				//logger.info("OUTPUT MESSAGE:" + response.toString());
				final PrintWriter output = resp.getWriter();
				output.println(response.toString());
				output.close();
			} else {
				resp.sendRedirect(returnUrl);
			}
			
		}
	}
	public static JSONObject clearTableOperation(String path) {
		JSONObject jsonObj = new JSONObject();
		try {
			JSONObject dbInfo = loadDatabaseSetting(path);

			String host = dbInfo.getString("database_host");
			String username = dbInfo.getString("database_username");
			String password = dbInfo.getString("database_password");
			String database = dbInfo.getString("database_name");

			DatabaseInitiate init = new DatabaseInitiate(logger, host, 3306,
					username, password, database);
			File sqlSchema = new File(path + "WEB-INF/clear_db.sql");
			// File sqlSchema = new File( path + "WEB-INF/db.sql");
			init.clearTable(sqlSchema);
			logger.info(ContextConstant.TAG, 
					instance.getClass().getName(), 
					"dropTableOperation", 
					"drop db complete");
			//init.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(ContextConstant.TAG, 
					instance.getClass().getName(), 
					"dropTableOperation", 
					"drop db cause JSONException", e);
		} finally {
			return jsonObj;
		}

	}
}