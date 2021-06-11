/**
 * 
 */
package net.themcfun.serverstart.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Lukas
 *
 */
public class COMAND_seeconsole extends Command{

	/**
	 * @param name
	 */
	public COMAND_seeconsole(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {

		String servername = args[0]; 
		boolean allowed = false;
		for (Iterator servers = BungeeCord.getInstance().getServers().keySet().iterator(); servers.hasNext();) {
			String server = (String) servers.next();
			if ((server.equalsIgnoreCase(servername))&&(Main.forbiddenservernames.contains(server))) {
				servername = server;
				allowed = true;
				break;	
			}
		}
		if (!allowed)
			return;


		try {
			if(Main.seeconsol.get(servername).getName().equalsIgnoreCase(sender.getName())){
				Main.seeconsol.remove(servername);
				return;
			}
		}catch (NullPointerException e) {}


		Process p = Main.consoles.get(servername);

		

		
		sender.sendMessage("[" + servername + "-ConsoleFeed]");

		Main.seeconsol.put(servername, sender);

		
		Thread tr = new Thread(new SendConsolLog(servername, sender, p));
		tr.start();





	}

}
class SendConsolLog implements Runnable{
	Object servername;
	CommandSender sender;
	Process p;
	public SendConsolLog(Object servername, CommandSender sender, Process p) {
		this.servername = servername;
		this.sender = sender;
		this.p = p;
		
	}
	/**
	 * 
	 */
	public void run() {
		InputStream stdIn = p.getInputStream();
		
		try {//TODO in seperaten comand
			new OutputStreamWriter(p.getOutputStream()).write("say conected");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		};
		
		
		
		
		InputStreamReader isr = new InputStreamReader(stdIn);
		BufferedReader br = new BufferedReader(isr);
		
		String line = null;
		
		try {
			while (((line = br.readLine()) != null)&&Main.seeconsol.get(servername).getName().equalsIgnoreCase(sender.getName()))
				sender.sendMessage(String.format("§c[%0$s-ConsolLog]:§r  %1$s", servername, line));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			isr.close();
			br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(line == null) {
			
			int exitVal = -1;
			try {
				exitVal = p.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sender.sendMessage(String.format("§c[%0$s-ConsolLog]:§4 shutdown with exitCode: %1$d", servername, exitVal));
		}
	}
	
}
