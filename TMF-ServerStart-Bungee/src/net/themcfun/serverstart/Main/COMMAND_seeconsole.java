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
public class COMMAND_seeconsole extends Command implements TabExecutor{

	/**
	 * @param name
	 */
	public COMMAND_seeconsole(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage("Bitte gebe den Namen des Servers ein!");
			return;
		}
		if(args.length >1) {
			sender.sendMessage("Bitte gebe den Namen des Servers ein! (Zu viele Argumente)");
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
			if(Main.seeconsol.get(servername).getName().equalsIgnoreCase(sender.getName())){
				Main.seeconsol.remove(servername);
				Main.consolprinter.get(servername).setSender(null);
				sender.sendMessage("[" + servername + "-ConsoleFeed stopped]");
				return;
			}
		}catch (NullPointerException e) {}


		sender.sendMessage("[" + servername + "-ConsoleFeed start]");

		Main.seeconsol.put(servername, sender);
		Main.consolprinter.get(servername).setSender(sender);








	}
	public Iterable<String> onTabComplete(CommandSender sender, String[] args){
		if(args.length==1) {
			System.out.println(Main.consoles.keySet());
			return Main.consoles.keySet();
			
		}
		
		return new ArrayList<String>();
	}
}
