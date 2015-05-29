package tw.com.wild0.hostelagentweb.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtility {
	
	public static String read(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		try {
	        
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.getProperty("line.separator"));
	            line = br.readLine();
	        }
	        String everything = sb.toString();
	    } finally {
	        br.close();
	    }
		return sb.toString();
	}
	public static String getFileExt(String fileName){
		String ext = "";
		if (fileName.indexOf(".") != -1) {
			ext = fileName.substring(fileName.lastIndexOf('.')+1);
		}
		return ext;
	}
}
