/**
 * 
 */
package net.themcfun.serverstart.Main;

import java.io.IOException;
import java.util.ArrayList;

import ch.jamiete.mcping.MinecraftPing;
import ch.jamiete.mcping.MinecraftPingOptions;
import ch.jamiete.mcping.MinecraftPingReply;
import ch.jamiete.mcping.MinecraftPingReply.Player;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

/**
 * @author Lukas
 *
 */
public class COMMAND_status extends Command  implements TabExecutor{

	/**
	 * @param name
	 */
	public COMMAND_status(String name) {
		super(name);
	}

	/**
	 * @param sender
	 * @param args
	 * @return
	 */
	
	
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		
		if(args.length==1) {
			return BungeeCord.getInstance().getServers().keySet();
		}
		
		
		
		return new ArrayList<String>();
	}

	/**
	 * @param sender
	 * @param args
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage("Serverstatus:");
		for (String tempServerName :  BungeeCord.getInstance().getServers().keySet()) {
			ServerInfo server = BungeeCord.getInstance().getServerInfo(tempServerName);
			String ipp = server.getSocketAddress().toString();
			String ip = ipp.substring(ipp.indexOf("/")+1, ipp.indexOf(":"));
			int port =Integer.valueOf(ipp.substring(ipp.indexOf(":")+1));
			
			
			
			
			
			if(Main.consoles.keySet().contains(tempServerName))
			{
				MinecraftPingReply data;
				try {
					data = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(ip).setPort(port));
					sender.sendMessage(tempServerName+" - §2 Online §8     Connected  Players  §7"+ data.getPlayers().getOnline() + "§8/§7" + data.getPlayers().getMax()+" §3"+getServerPlayers(data)+"   §8MOTD: "+ data.getDescription().getText());
				} catch (IOException e) {
					sender.sendMessage(tempServerName+" - §4 Offline " );
				}
			}
			else {
				MinecraftPingReply data;
				try {
					data = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname(ip).setPort(port));
					System.out.println(data.getDescription() + "  --  " + data.getPlayers().getOnline() + "/" + data.getPlayers().getMax());
					sender.sendMessage(tempServerName+" - §2 Online §8 not Connected  Players  §7"+ data.getPlayers().getOnline() + "§8/§7" + data.getPlayers().getMax()+" §3"+getServerPlayers(data)+"   MOTD: "+ data.getDescription().getText());
				} catch (IOException e) {
					sender.sendMessage(tempServerName+" - §4 Offline " );
				}
				
				
				
			}
		}
		if(args.length==1) {
			System.out.println(Main.consoles.keySet());
			 Main.consoles.keySet();
			
		}
		
		
	}
	private static String getServerPlayers(MinecraftPingReply data) {
		String out = "";
		if(data.getPlayers().getOnline()>0) {
			for (Player iterable_element : data.getPlayers().getSample()) {
				out += iterable_element.getName()+", ";
			}
			out = out.substring(0,out.length()-2);
		}else {
			return "";
		}
		return out;
	}

}
