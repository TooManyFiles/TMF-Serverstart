/**
 * 
 */
package net.themcfun.serverstart.Main.remotemaintenance;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONObject;

import net.md_5.bungee.BungeeCord;
import net.themcfun.serverstart.Main.Main;

/**
 * @author Lukas
 *
 */
public class RemoteMaintenanceMain {
	//private final Main main;
	private ServerSocket internalServer;
	private ServerSocket externalServer;
	public boolean isrunning;
	public HashMap<String, JSONObject> minecraftServers = new HashMap<String, JSONObject>();
	public HashMap<String, JSONObject> users = new HashMap<String, JSONObject>();
	private PrivateKey prkin;
	private PublicKey pukin;
	private PrivateKey prkex;
	private PublicKey pukex;

	public RemoteMaintenanceMain() { //Main main
		/*this.main = main;
		try
		{

			internalServer = new ServerSocket(main.config.getInt("RemoteMaintenance.interneal.Port"));
			externalServer = new ServerSocket(main.config.getInt("RemoteMaintenance.external.Port"));
			isrunning = true;

		} catch (SocketException e)
		{
			main.getLogger().log(Level.FINER, "Coud not start RemoteMaintenance");
			main.getLogger().log(Level.FINER, e.toString());

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		try {
			internalServer = new ServerSocket(9800);
			externalServer = new ServerSocket(9801);
			isrunning = true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//read keys
		try {
			Path publicpathin = Paths.get("C:\\Users\\Lukas\\Programmieren\\test\\key.pub");//main.config.getString("RemoteMaintenance.interneal.key.publicPath"));
			byte[] bytes = Files.readAllBytes(publicpathin);
			pukin = Crypto.byteToPublickey(bytes);
			
			
			Path privatepathin = Paths.get("C:\\Users\\Lukas\\Programmieren\\test\\key.key");//main.config.getString("RemoteMaintenance.interneal.key.PrivatePath"));
			bytes = Files.readAllBytes(privatepathin);
			prkin = Crypto.byteToPrivatekey(bytes);
			
			Path publicpathex = Paths.get("C:\\Users\\Lukas\\Programmieren\\test\\key.pub");//main.config.getString("RemoteMaintenance.external.key.publicPath"));
			bytes = Files.readAllBytes(publicpathex);
			pukex = Crypto.byteToPublickey(bytes);
			
			
			Path privatepathex = Paths.get("C:\\Users\\Lukas\\Programmieren\\test\\key.key");//main.config.getString("RemoteMaintenance.external.key.PrivatePath"));
			bytes = Files.readAllBytes(privatepathex);
			prkex = Crypto.byteToPrivatekey(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkclients();
	}
	public void checkminecraftserver()
	{
		System.out.println(internalServer.getInetAddress());
		
		while(isrunning)
		{
			try{

				System.out.println("Waiting for client(MinecraftServer) at " + internalServer.getLocalPort());
				Socket tempMinecraftServer = internalServer.accept();
				DataInputStream input = new DataInputStream(tempMinecraftServer.getInputStream());

				String name = input.readUTF();//TODO split
				if (name.startsWith("Name:")) {//chek if server its an vaid request
					name = name.substring(5);
					System.out.println(name);
					DataOutputStream output = new DataOutputStream(tempMinecraftServer.getOutputStream());
					
					//verification
					String[] publickey = input.readUTF().split("!");//TODO split
					byte[] bpublickey = new byte[publickey.length];
					for (int i = 0; i < publickey.length; i++) {
						bpublickey[i] = Integer.valueOf(publickey[i]).byteValue();
					}
					String spuk = "";
					for (byte b : bpublickey) {
						spuk = spuk + b+"!";
					}
					System.out.println(spuk);
					
					byte[] array = new byte[12]; //generate random string with 6 chars
				    new Random().nextBytes(array);
				    String generatedString = new String(array, Charset.forName("UTF-8"));
					
					String squestion = generatedString;
					PublicKey kpublickey = Crypto.byteToPublickey(bpublickey);
					byte[] question = Crypto.encrypt(squestion.getBytes(), kpublickey);
					output.writeInt(question.length);
					
					for (int i = 0; i < question.length; i++) {
						output.writeByte(question[i]);
					}
					
					
					
					int count = input.readInt();
					byte[] answer = new byte[count];
					for (int i = 0; i < count; i++) {
						answer[i] = input.readByte();
					}
					for (byte b : Crypto.decrypt(answer, this.prkin)) {System.out.print(b);}
					System.out.println();
					for (byte b : squestion.getBytes()) {System.out.print(b);}
					System.out.println();
					boolean verifyed=true;
					byte[] danswer = Crypto.decrypt(answer, this.prkin);
					for (int i = 0; i <danswer.length; i++) {
						if(squestion.getBytes()[i]==danswer[i]) {
							
						}else {
							verifyed=false;
							break;
						}
					}
					if (verifyed) {
						System.out.println("new Minecraft server has benn verified Name: "+ name +" IP: "+tempMinecraftServer.getRemoteSocketAddress());
						
						JSONObject hashmapin = new JSONObject();
						hashmapin.put("Publickey", kpublickey);
						hashmapin.put("Outsteam", output);
						hashmapin.put("Insteram", input);
						minecraftServers.put(name, hashmapin);
						
					}
					else {
						System.out.println("New server has tryed to conet to Remote Maintenance but insn't verified. name: "+ name+" IP: "+tempMinecraftServer.getRemoteSocketAddress());
					}
					
					



					

				}else {
					tempMinecraftServer.close();
					System.out.println("New server has tryed to conet to Remote Maintenance but with wrong format.IP: "+tempMinecraftServer.getRemoteSocketAddress());
				}




			}
			catch(Exception e)
			{
				e.printStackTrace();
				break;
			}
		}
	}

	public void checkclients() {
		
		System.out.println(externalServer.getInetAddress());
		while(isrunning)
		{
			try{

				System.out.println("Waiting for client(User) at " + externalServer.getLocalPort());
				Socket tempclientServer = externalServer.accept();
				DataInputStream input = new DataInputStream(tempclientServer.getInputStream());

				String name = input.readUTF();//TODO split
				if (name.startsWith("Name:")) {//chek if server its an vaid request
					name = name.substring(5);
					System.out.println(name);
					DataOutputStream output = new DataOutputStream(tempclientServer.getOutputStream());
					
					//verification
					String[] publickey = input.readUTF().split("!");//TODO split
					byte[] bpublickey = new byte[publickey.length];
					for (int i = 0; i < publickey.length; i++) {
						bpublickey[i] = Integer.valueOf(publickey[i]).byteValue();
					}
					String spuk = "";
					for (byte b : bpublickey) {
						spuk = spuk + b+"!";
					}
					System.out.println(spuk);
					
					byte[] array = new byte[12]; //generate random string with 6 chars
				    new Random().nextBytes(array);
				    String generatedString = new String(array, Charset.forName("UTF-8"));
					
					String squestion = generatedString;
					PublicKey kpublickey = Crypto.byteToPublickey(bpublickey);
					byte[] question = Crypto.encrypt(squestion.getBytes(), kpublickey);
					output.writeInt(question.length);
					
					for (int i = 0; i < question.length; i++) {
						output.writeByte(question[i]);
					}
					
					
					
					int count = input.readInt();
					byte[] answer = new byte[count];
					for (int i = 0; i < count; i++) {
						answer[i] = input.readByte();
					}
					for (byte b : Crypto.decrypt(answer, this.prkex)) {System.out.print(b);}
					System.out.println();
					for (byte b : squestion.getBytes()) {System.out.print(b);}
					System.out.println();
					boolean verifyed=true;
					byte[] danswer = Crypto.decrypt(answer, this.prkex);
					for (int i = 0; i <danswer.length; i++) {
						if(squestion.getBytes()[i]==danswer[i]) {
							
						}else {
							verifyed=false;
							break;
						}
					}
					if (verifyed) {
						System.out.println("new User has benn verified Name: "+ name +" IP: "+tempclientServer.getRemoteSocketAddress());
						
						JSONObject hashmapin = new JSONObject();
						hashmapin.put("Publickey", kpublickey);
						hashmapin.put("Outsteam", output);
						hashmapin.put("Insteram", input);
						users.put(name, hashmapin);
						JSONArray onlineServers =new JSONArray();
						for (Iterator iterator = minecraftServers.keySet().iterator(); iterator.hasNext();) {
							String s = (String) iterator.next();
							onlineServers.put(s);
						}
						sendByteList(output, onlineServers.toString().getBytes(), kpublickey);
						
						JSONArray allservers = new JSONArray();
						try {
						for (Iterator<String> servers = BungeeCord.getInstance().getServers().keySet().iterator(); servers.hasNext();) {
							String  s = (String) servers.next();
							allservers.put(s);
						}
						}catch (NullPointerException e) {}
						sendByteList(output, allservers.toString().getBytes(), kpublickey);
						
					}
					else {
						System.out.println("New user has tryed to conet to Remote Maintenance but insn't verified. name: "+ name+" IP: "+tempclientServer.getRemoteSocketAddress());
					}
					
					



					

				}else {
					tempclientServer.close();
					System.out.println("New user has tryed to conet to Remote Maintenance but with wrong format.IP: "+tempclientServer.getRemoteSocketAddress());
				}




			}
			catch(Exception e)
			{
				e.printStackTrace();
				break;
			}
		}
	}
	
	
	
	//TODO temp versiben 
	public static void main(String[] args) {
		System.out.println("test");
		//byte[][] out = Crypto.gen();
		
		String outFile = "C:\\Users\\Lukas\\Programmieren\\test\\key";
		
		/*try {
			FileOutputStream out1 = new FileOutputStream(outFile + ".key");
			out1.write(out[1]);
			out1.close();
			 
			out1 = new FileOutputStream(outFile + ".pub");
			out1.write(out[0]);
			out1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		byte[] array = new byte[12]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));

	    System.out.println("\n\n\n"+generatedString);

	    new RemoteMaintenanceMain();


	}
	private static void sendByteList(DataOutputStream output, byte[] data, PublicKey kpublickey) throws IOException {
		data = Crypto.encrypt(data.toString().getBytes(), kpublickey);
		output.writeInt(data.length);
		for (int i = 0; i < data.length; i++) {
			output.writeByte(data[i]);
		}
	}
}
