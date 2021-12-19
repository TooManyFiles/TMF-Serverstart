/**
 *
 */
package net.themcfun.serverstart.Main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

/**
 * @author Mr_Comand
 *
 */

public class COMMAND_sendconsole extends Command implements TabExecutor{


	/**
	 * @param name
	 */

	public COMMAND_sendconsole(String name) {

		super(name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender.getName().equalsIgnoreCase("CONSOLE"))) {
			sender.sendMessage("CONSOLE only Command");
			return;
		}

		if(args.length == 0) {

			sender.sendMessage("Bitte gebe den Namen des Servers ein!");
			return;
		}


		String servername = args[0];
		boolean allowed = false;
		for (Iterator<String> servers = BungeeCord.getInstance().getServers().keySet().iterator(); servers.hasNext();) {
			String server = (String) servers.next();
			if ((server.equalsIgnoreCase(servername))&&!(Main.forbiddenservernames.contains(server))) {
				servername = server;
				allowed = true;
				break;
			}
		}
		if (!allowed)
		{

			sender.sendMessage("§4[TMF-Serverstart] Invalid Servername!");

			return;
		}


		try {
			Writer w = Main.consolinput.get(servername);

			String command = "";
			for (int i = 1; i < args.length; i++) {
				command = command+ args[i] + " ";

			}
			sender.sendMessage("§4[TMF-Serverstart - "+servername+"] §r sent  Command: "+ command);
			w.write(command+"\n");
			w.flush();
		} catch (IOException e2) {
			e2.printStackTrace();
		}


	}
	
	public Iterable<String> onTabComplete(CommandSender sender, String[] args){
		if(!(sender.getName().equalsIgnoreCase("CONSOLE"))) {
			return new ArrayList<String>();
		}
		if(args.length==1) {
			return BungeeCord.getInstance().getServers().keySet();
		}
		if(args.length>=2) {
			String servername = args[0];
			boolean allowed = false;
			for (Iterator<String> servers = BungeeCord.getInstance().getServers().keySet().iterator(); servers.hasNext();) {
				String server = (String) servers.next();
				if ((server.equalsIgnoreCase(servername))&&!(Main.forbiddenservernames.contains(server))) {
					servername = server;
					allowed = true;
					break;
				}
			}
			if (!allowed)
			{

				

				return new ArrayList<String>();
			}
			
			ArrayList<String> text = new ArrayList<String>();
			text.add(servername);
			return text;
		}
		return null;
	}


}
