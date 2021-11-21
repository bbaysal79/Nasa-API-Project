/*
 * COMP416 - Project1: StratoNet: NASA-API based Network Application
 * Client Side
 * Hüseyin Burak Baysal - 61319
 */

import java.awt.Font;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket s = new Socket("localhost", 4499);
		
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter os = new PrintWriter(s.getOutputStream(), true);
		String name = "";
		if(args.length == 1)
			name = args[0];
		else
			name = "unknown_user";
		String msg = " ";
        String response = " ";
		
		os.println(name);
		os.flush();
		while(!response.equals("Connection authorized! New socket will be assigned, enter to proceed.")) {
			response = is.readLine();
        	print(response);
        	msg = userInput.readLine();
            os.println(msg);
            os.flush();
		}
		String username = is.readLine();
		String newP = is.readLine();
		int newPort = Integer.parseInt(newP);
		Socket fs = new Socket("localhost", newPort);
		is.close();
        os.close();
        s.close();
        BufferedReader is2 = new BufferedReader(new InputStreamReader(fs.getInputStream()));
		PrintWriter os2 = new PrintWriter(fs.getOutputStream(), true);
		response = is2.readLine();
		print(response);
		String date = userInput.readLine();
        os2.println(date);
        os2.flush();
		InputStream fis = fs.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(fis);
		String filepath = "C:\\Users\\Burak\\Desktop\\Spring 2021\\Elec 416\\projects\\Project1-StratoNet\\client_img\\"+username+"_"+date+"_"+"img.jpg";
		FileOutputStream fos = new FileOutputStream(filepath);
		String fsize = is2.readLine();
		int f_size = Integer.parseInt(fsize);
		byte b[] = new byte[f_size];
		bis.read(b);
		int hashCode_img = b.hashCode();
		fos.write(b);
		fos.flush();
		String mars_weather = is2.readLine();
		int hashCode_mars = mars_weather.hashCode();
		JSONObject mars_w = new JSONObject(mars_weather);
		String hc_img = is2.readLine();
		String hc_mars = is2.readLine();
		print("Calculated and received Hash Codes for image: "+hashCode_img+", "+hc_img);
		print("Calculated and received Hash Codes for string: "+hashCode_mars+", "+hc_mars);
		response = is2.readLine();
    	print(response);
		msg = userInput.readLine();
        os2.println(msg);
        os2.flush();
		while(!msg.equals("quit")) {
			response = is2.readLine();
        	print(response);
        	msg = userInput.readLine();
            os2.println(msg);
            os2.flush();
        }
		fs.close();
		bis.close();
		fos.close();
		is2.close();
		os2.close();
		
		
		String pre_av = "Average Pressure: " + mars_w.getNumber("av");
		String pre_ct = "Counted Pressure Measurments: " + mars_w.getNumber("ct");
		String pre_mn = "Minimum Pressure: " + mars_w.getNumber("mn");
		String pre_mx = "Maximum Pressure: " + mars_w.getNumber("mx");
		String pre_sol = "Sol (Mars day): " + mars_w.getNumber("sol");
		
		JFrame frame = new JFrame(username);
		frame.setSize(1300, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImageIcon temp = new ImageIcon(filepath);
		Image img_temp = temp.getImage();
		Image img_resized = img_temp.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		ImageIcon apod = new ImageIcon(img_resized);
		JLabel img_label = new JLabel(apod);
		img_label.setBounds(0, 0, 800, 600);
		
		
		
		JLabel label1 = new JLabel(pre_av);
		label1.setFont(new Font("Merriweather", Font.BOLD, 20));
		JLabel label2 = new JLabel(pre_ct);
		label2.setFont(new Font("Merriweather", Font.BOLD, 20));
		JLabel label3= new JLabel(pre_mn);
		label3.setFont(new Font("Merriweather", Font.BOLD, 20));
		JLabel label4 = new JLabel(pre_mx);
		label4.setFont(new Font("Merriweather", Font.BOLD, 20));
		JLabel label5= new JLabel(pre_sol);
		label5.setFont(new Font("Merriweather", Font.BOLD, 20));
		
		JPanel panel = new JPanel();
		panel.setBounds(800, 160, 400, 400);
		panel.add(label5);
		panel.add(label1);
		panel.add(label2);
		panel.add(label3);
		panel.add(label4);
		
		frame.setLayout(null);
		frame.add(img_label);
		frame.add(panel);
		
		frame.setVisible(true);
        
	}
	
	public static void print(String msg)
	{
        System.out.println(msg);
    }

}
