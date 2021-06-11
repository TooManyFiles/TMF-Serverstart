package net.themcfun.serverstart.Main;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_Startserver extends Command {

	String playername;
	
	public COMMAND_Startserver(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		CommandSender cs = sender;
		playername = cs.getName();
		
		if(sender instanceof CommandSender) { //TODO CommandSender -> Player
			
		// /startserver <Servername>
		
		if(args.length > 0) {
		
		Main.startserver(args[0]);
							
		} else {
			cs.sendMessage("Use /help to view a list of avaiable Commands!");
		}
		} else {
		   	playername = "Console";
   }
  }	
 }
