/*
 * COMP416 - Project1: StratoNet: NASA-API based Network Application
 * Server Side
 * Hüseyin Burak Baysal - 61319
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServerThread extends Thread {
	public static final String API_KEY = "Jn7LOL2jcfX8WxhwWLiLwuKbzpefwyqe9p9dGxnf";
	public static final String APOD_URL = "https://api.nasa.gov/planetary/apod?api_key=";
	public static final String INSIGHT_URL = "https://api.nasa.gov/insight_weather/?api_key=";
	private static Scanner x;
	Socket socket;
	ServerThread(Socket socket){
		this.socket = socket;
	}
	
	public void run() {
		try {
			//String message = "";
			PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String user = is.readLine();
			String username = "";
			print("New client is connected now...");
			
			// Authorization phase.
			if(user.equals("unknown_user")) {
				os.println("Enter username:");
	            os.flush();
	            username = is.readLine();
            }
			else
				username = user;
            if(verifyUsername(username, "users"))
            {
            	String password = "";
            	os.println("Enter password:");
                os.flush();
            	password = is.readLine();
            	if(!verifyPassword(username, password, "users")) {
	                for(int i=2; i>0; i--) {
	                	os.println("Wrong password! Enter password again(Remaining attempts:" + Integer.toString(i)+")");
	                    os.flush();
	                	password = is.readLine();
	                	if(verifyPassword(username, password, "users"))
	                		break;
	                }
                }
                if(verifyPassword(username, password, "users"))
                {
                	os.println("Connection authorized! New socket will be assigned, enter to proceed.");
                    os.flush();
                    os.println(username);
                    os.flush();
                    int newPort = getPort(1000, 30000);
                    os.println(Integer.toString(newPort));
                    os.flush();
                    ServerSocket fss = new ServerSocket(newPort);
                    Socket fs = fss.accept();
                    is.close();
                    os.close();
                    socket.close();
                    PrintWriter os2 = new PrintWriter(fs.getOutputStream(), true);
        			BufferedReader is2 = new BufferedReader(new InputStreamReader(fs.getInputStream()));
        			
        			
        			os2.println("Give the date for Astronomy Picture(yyyy-mm-dd). And also I\'ll give you a Mars day Weather data for bonus :)");
        			os2.flush();
        			String date = is2.readLine();
        			JSONObject Weather = insightAPI(API_KEY, INSIGHT_URL);
        			String mars_weather = Weather.toString();
        			String img_url = apodAPI(date, API_KEY, APOD_URL);
        			URL url = new URL(img_url);
        			String filepath = "C:\\Users\\Burak\\Desktop\\Spring 2021\\Elec 416\\projects\\Project1-StratoNet\\server_img\\"+username+"_"+date+"_"+"img.jpg";
        			downloadFile(url, filepath);
        			OutputStream fos = fs.getOutputStream();
        			FileInputStream fis = new FileInputStream(filepath);
        			File img_file = new File(filepath);
        			byte b[] = new byte[(int) img_file.length()];
        			String file_size = Long.toString(img_file.length());
        			os2.println(file_size);
        			os2.flush();
        			fis.read(b);
        			int hashCode_img = b.hashCode();
        			fos.write(b);
        			fos.flush();
        			int hashCode_mars = mars_weather.hashCode();
        			os2.println(mars_weather);
        			os2.flush();
        			os2.println(Integer.toString(hashCode_img));
        			os2.flush();
        			os2.println(Integer.toString(hashCode_mars));
        			os2.flush();
        			os2.println("All Files Received! Type \"quit\" to display the data. Or you can continue to chat with the server :)");
        			os2.flush();
        			String message = is2.readLine();
        			while(!message.equals("quit")) {
        				print("incoming message from "+username+": "+message);
        				os2.println("response from server: "+message);
        				os2.flush();
        				message = is2.readLine();
        			}
        			fs.close();
        			fss.close();
        			fis.close();
        			fos.close();
        			os2.close();
        			is2.close();
                    
                }
                else
                {
                	os.println("Wrong password. Connection lost!");
                    os.flush();
                    is.close();
                    os.close();
                    socket.close();
                }
            }
            else
            {
            	os.println("Username does not exist. Connection lost!");
                os.flush();
                is.close();
                os.close();
                socket.close();
            }
			
				
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void print(String msg)
	{
        System.out.println(msg);
    }
	
	public static boolean verifyUsername(String username, String file) 
    {
    	boolean found = false;
    	String tempU = "";
    	
    	try
    	{
    		x = new Scanner(new File(file));
    		x.useDelimiter("[\n]");
    		
    		while(x.hasNext() && !found) 
    		{
    			tempU = x.next();
    			
    			if(tempU.split(",")[0].equals(username))
    			{
    				found = true;
    			}
    		}
			x.close();
    	}
    	catch(Exception e)
    	{
    		print("Error when parsing the users file!" + e);
    	}
		return found;
    }
    
    public static boolean verifyPassword(String username, String password, String file) 
    {
    	boolean found = false;
    	String tempU = "";
    	String tempP = "";
    	
    	try
    	{
    		x = new Scanner(new File(file));
    		x.useDelimiter("[,\n]");
    		
    		while(x.hasNext() && !found) 
    		{
    			tempU = x.next();
    			tempP = x.next();
    			
    			if(tempU.trim().equals(username.trim()) && tempP.trim().equals(password.trim()))
    			{
    				found = true;
    			}
    		}
    		x.close();
    	}
    	catch(Exception e)
    	{
    		print("Error when parsing the users file!" + e);
    	}
		return found;
    }
    
    public static String apodAPI(String date, String api_key, String api_url) throws IOException, InterruptedException {
    	String url = api_url+api_key+"&date="+date;
    	HttpClient client = HttpClient.newHttpClient();
    	HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
    	HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    	JSONObject json = new JSONObject(response.body());
		String imgURL = json.getString("url");
    	return imgURL;
    }
    
    public static JSONObject insightAPI(String api_key, String api_url) throws IOException, InterruptedException {
    	String url = api_url+api_key+"&feedtype=json&ver=1.0";
    	HttpClient client = HttpClient.newHttpClient();
    	HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
    	HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    	JSONObject json = new JSONObject(response.body());
		JSONArray sols = json.getJSONArray("sol_keys");
		String randomSol = sols.getString(getPort(0, sols.length()));
		JSONObject pre = json.getJSONObject(randomSol).getJSONObject("PRE");
		pre.put("sol", randomSol);
    	return pre;
    }
    
    public static int getPort(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    
    public static void downloadFile(URL url, String fileName) throws Exception {
        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }
    
    
    
    
    
    
    
}
