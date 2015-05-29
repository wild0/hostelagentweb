package tw.com.wild0.hostelagentweb.servlet;

import java.io.File;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.json.JSONException;

import tw.com.orangice.sf.lib.db.DatabaseManager;
import tw.com.orangice.sf.lib.db.MongoDatabaseManager;
import tw.com.orangice.sf.lib.log.LogService;
import tw.com.wild0.hostelagentweb.constant.ContextConstant;
import tw.com.wild0.hostelagentweb.controller.DatabaseController;

public class ServiceContextListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public ServiceContextListener() {
		// TODO Auto-generated constructor stub
		System.out.println("ServiceContextListener:start");
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("ServiceContextListener:initial");
		// arg0.getServletContext().get
		ServletContext sc = arg0.getServletContext();
		String webAppPath = sc.getRealPath("/");
		// =====================LOG PROCESS

		String log4jLocation = webAppPath + "WEB-INF/log4j.properties";
		String adminLocation = webAppPath + "WEB-INF/config.json";
		String databaseLocation = webAppPath + "WEB-INF/database.properties";

		//String log4jProp = webAppPath + log4jLocation;
		//LogUtility.initial(log4jProp);

		// ======================DB PROCESS
		try {
			File logProperties = new File(log4jLocation);
			LogService log = new LogService(logProperties,"pinanweb");
			
			
			//DatabaseManager dm = DatabaseController
			//		.getDefaultDatabaseManager(databaseLocation);
			
			MongoDatabaseManager dm = DatabaseController.getMongoDatabaseManager(databaseLocation);
			sc.setAttribute(ContextConstant.DB_MANAGER, dm);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sc.setAttribute(ContextConstant.INITIATE, true);
		} catch (Exception e) {
			e.printStackTrace();
			sc.setAttribute(ContextConstant.INITIATE, true);
		}
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

}
