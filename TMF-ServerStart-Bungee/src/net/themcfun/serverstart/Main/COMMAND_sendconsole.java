/**
 *
 */
package net.themcfun.serverstart.Main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author Mr_Comand
 *
 */
public class COMMAND_sendconsole extends Command{

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
		if(args.length == 1) {
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
			sender.sendMessage("�4[TMF-Serverstart] Invalid Servername!");
			return;
		}


	try {
		Writer w = Main.consolinput.get(servername);
		w.write(args[1:]);
		w.flush();
	} catch (IOException e2) {
		e2.printStackTrace();
	}










	}

}