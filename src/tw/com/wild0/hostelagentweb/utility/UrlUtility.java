package tw.com.wild0.hostelagentweb.utility;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;



public class UrlUtility {
	public static String getUrlPrefix(HttpServletRequest request) throws MalformedURLException{
		URL url = new URL(request.getScheme(), 
		        request.getServerName(), 
		        request.getServerPort(), 
		        request.getContextPath());
		
		return url.toString();
	}
	public static String getProductsByCategoryUrl(HttpServletRequest request,String categoryCode) throws MalformedURLException{
		String url = getUrlPrefix(request);
		
		String returnUrl = url+"/admin/product-list.jsp?item_category_code="+categoryCode;
		return returnUrl;
	}
	public static String getProjectStep1ByCodeUrl(HttpServletRequest request,String projectCode) throws MalformedURLException{
		String url = getUrlPrefix(request);
		
		String returnUrl = url+"/admin/project-edit-step1.jsp?project_code="+projectCode;
		return returnUrl;
	}
	public static String getProjectProceedList(HttpServletRequest request) throws MalformedURLException{
		String url = getUrlPrefix(request);
		
		String returnUrl = url+"/admin/project-list-proceed.jsp";
		return returnUrl;
	}
	public static String getUrl(HttpServletRequest request, String parameter) throws MalformedURLException{
		String url = getUrlPrefix(request);
		if(parameter==null || parameter.length()==0){
			return null;
		}
		
		/*
		else if(parameter.equals("_login_")){
			return  url+"/"+UrlConstant.PAGE_LOGIN;
		}
		else if(parameter.equals("_initial_")){
			return  url+"/"+UrlConstant.PAGE_INITIAL;
		}
		else if(parameter.equals("_referer_")){
			return request.getHeader("Referer");
			  // handle empty referer.....
		}
		else if(parameter.equals("_index_")){
			return  url+"/"+UrlConstant.PAGE_INDEX;
		}
		*/
		else if(parameter.equals("_referer_")){
			return request.getHeader("Referer");
			  // handle empty referer.....
		}
		else if(parameter.equals("_hostel_index_")){
			return  url+"/pages/hostel.jsp";
		}
		else if(parameter.equals("_room_index_")){
			return  url+"/pages/room.jsp";
		}
		else{
			return url+"/"+parameter;
		}
	}
	
	
	public static String getLastPath(String path){
		String[] paths = path.split("/");
		int lastIndex = paths.length-1;
		return paths[lastIndex];
	}
	public static String getLastTwoPath(String path){
		String[] paths = path.split("/");
		int lastIndex = 0;
		if(paths.length>2){
			lastIndex = paths.length-2;
		}
		else{
			lastIndex = paths.length-1;
		}
		return paths[lastIndex];
	}
}
