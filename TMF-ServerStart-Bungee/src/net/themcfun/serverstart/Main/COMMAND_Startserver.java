package net.themcfun.serverstart.Main;

import java.util.logging.Level;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_Startserver extends Command {


String playername;


	public COMMAND_Startserver(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage("Bitte gebe den Namen des Servers ein!");
			return;
		}



		CommandSender cs = sender;
		playername = cs.getName();

		if(sender instanceof ProxiedPlayer) { //TODO CommandSender -> Player
			if(Main.consoles.keySet().contains(args[0])) {
				Main.INSTANCE.getLogger().log(Level.FINER, "Server "+args[0]+" is alredy online!! Conecting ...");
				if(!(sender.getName().equalsIgnoreCase("CONSOLE"))) {
					((ProxiedPlayer) sender).connect(BungeeCord.getInstance().getServers().get(args[0]));
						sender.sendMessage("Server " + args[0] + " is alredy online!! Conecting ...");
					BungeeCord.getInstance();
				}
				return;
			}



		}
		// /startserver <Servername>

		if(args.length > 0) {
			Main.seeconsol.put(args[0], sender);
			Main.INSTANCE.startserver(args[0]);


		} else {
			cs.sendMessage("Use /help to view a list of avaiable Commands!");
		}
	}
}
