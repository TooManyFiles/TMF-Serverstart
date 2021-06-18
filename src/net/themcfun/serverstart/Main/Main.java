package net.themcfun.serverstart.Main;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.themcfun.serverstart.Main.Motd.OnPing;

public class Main extends Plugin{
	public static Boolean wartung = false;
	static String defaultpath;
	public static Configuration config;
	static String startcomand;

	public static Main INSTANCE;
	
	public static ArrayList<String> forbiddenservernames;
	public static HashMap<String, Process> consoles = new HashMap<String, Process>();
	public static HashMap<String, CommandSender> seeconsol = new HashMap<String, CommandSender>();
	public static HashMap<String, SendConsolLog> consolprinter = new HashMap<String, SendConsolLog>();
	public static HashMap<String, Writer> consolinput = new HashMap<String, Writer>();

	@Override
	public void onLoad() {
		configread();
	}
	

	@Override
	public void onEnable() {

		INSTANCE = this;

		getLogger().info(ChatColor.RED + "[TMF-Serverstart] sucessfully loaded! ");
		getLogger().info(" ");
		getLogger().info(ChatColor.GRAY + "----------------------------------------");
		getLogger().info(ChatColor.RED + "Version: 1.0");
		getLogger().info(ChatColor.RED + "Commands: /startserver");
		getLogger().info(ChatColor.RED + "Developers: Redstone_Studios & Mr_Comand");
		getLogger().info(ChatColor.GRAY + "----------------------------------------");

		 ProxyServer.getInstance().getPluginManager().registerListener(this, new OnPing());
		registerCommands();
		configread();
		startserver(config.getString("Lobbyname"));
	}	

	
	private void configread() {

		try {
			if(!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			File file = new File(getDataFolder().getPath(), "config.yml");
			if(!file.exists()) {
				file.createNewFile();
				Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				config.set("defaultpath", "..");
				config.set("startcomand", "./start.sh");
				config.set("Lobbyname", "Lobby");
				//config.set("#{path} is replaced by the pfath", "");
				config.set("forbiddenServers", new ArrayList<String>());
				config.set("MOTD.default", "§1§lThe§2§lMC§4§lFun - §7§lMinigames and more!");
				config.set("MOTD.LobbyOfline", "§1§lThe§2§lMC§4§lFun \n §4Lobby offline! Bitte kontaktieren sie uns!");
				config.set("MOTD.Wartungsarbeiten", "§1§lThe§2§lMC§4§lFun \\n §4Wartungsarbeiten! Wir sind am umbauen!");
				
				config.set("Version.LobbyOfline", "Lobby offline!");
				config.set("Version.Wartungsarbeiten", "Wartungsarbeiten!");
				

				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
			} 

			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);

			defaultpath = config.getString("defaultpath");
			forbiddenservernames = (ArrayList<String>) config.getList("forbiddenServers");
			forbiddenservernames = new ArrayList<String>();
			if (forbiddenservernames==null) {
				forbiddenservernames = new ArrayList<String>();
			}
			startcomand = config.getString("startcomand").replace("{path}", "%s");
			
		} catch(IOException e) {

		}

	}

	@Override
	public void onDisable() {
		
		for (Iterator<String> iterator = consoles.keySet().iterator(); iterator.hasNext();) {
			String servername = (String) iterator.next();
			try {
				Writer w = Main.consolinput.get(servername);
				w.write("stop\n");
				w.flush();
			} catch (IOException e2) {
				e2.printStackTrace();
			};
			
		}
		getLogger().info(ChatColor.RED + "[TMF-Serverstart] sucessfully disabled!");
	}	
	private void registerCommands() {

		ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_Startserver("startserver"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_seeconsole("seeconsole"));


	}
	public void startserver(String servername) {
		if(consoles.keySet().contains(servername)) {
			INSTANCE.getLogger().log(Level.FINER, "Server "+servername+" is alredy online!!");
			
			return;
			
			
		}

		if (defaultpath != "unset") {
			
			boolean allowed = false;
			for (Iterator<String> servers = BungeeCord.getInstance().getServers().keySet().iterator(); servers.hasNext();) {
				String server = (String) servers.next();
				if ((server.equalsIgnoreCase(servername))&&!(forbiddenservernames.contains(server))) {
					servername = server;
					allowed = true;
					break;	
				}
			}
			

			if (!allowed) {
				INSTANCE.getLogger().warning(ChatColor.RED + "[TMF-Serverstart] Invalid Servername!");
				return;
			}
			String path = (defaultpath + "/" + servername);
			
			
			try {
				ProcessBuilder proc = new ProcessBuilder(String.format(startcomand, path));
				//proc.redirectOutput(ProcessBuilder.Redirect.INHERIT);
				File pathfile = new File(path);
				proc.directory(pathfile);
				Process p = proc.start();
				p.onExit().runAsync(new MessageOnServerexit(servername, p));
				
				consoles.put(servername, p);
				
				consolinput.put(servername, new OutputStreamWriter(p.getOutputStream(), "UTF-8"));
				
				SendConsolLog runner = new SendConsolLog(servername, seeconsol.get(servername), Main.consoles.get(servername));
				Thread tr = new Thread(runner);
				tr.start();
				Main.consolprinter.put(servername.toString(), runner);

			} catch (IOException e) {

				e.printStackTrace();
			}
		} else {

			INSTANCE.getLogger().warning("[TMF-Serverstart] Set a defaultpath first!");

		}
	}
}
