/**
 * 
 */
package net.themcfun.serverstart.Main.Motd;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.themcfun.serverstart.Main.Main;

/**
 * @author Mr_Comand
 *
 */
public class OnPing implements Listener {
	public static String motd = "TheMcFun";

	@EventHandler
	public void onPing(ProxyPingEvent e){
		ServerPing serverPing = e.getResponse();
		if(!Main.consoles.containsKey(Main.INSTANCE.config.getString("Lobbyname"))) {
			serverPing.setDescription(Main.config.getString("MOTD.LobbyOfline"));
			serverPing.setVersion(new Protocol(Main.config.getString("Version.LobbyOfline"), 0));
			
		}else if(Main.wartung) {
			System.out.println(serverPing.getVersion());
			serverPing.setDescription(Main.config.getString("MOTD.Wartungsarbeiten"));
			serverPing.setVersion(new Protocol(Main.config.getString("Version.Wartungsarbeiten"), 0));
		}else	
		{
		serverPing.setDescription(Main.config.getString("MOTD.default"));
		
		}
		
		//serverPing.setFavicon(new Favicon(""));
		e.setResponse(serverPing);
	}
	
}
