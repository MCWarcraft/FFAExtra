package randy.ffaextra;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import core.Custody.Custody;
import randy.core.CoreAPI;
import randy.core.CoreScoreboard;
import randy.core.tools.CoreDatabase;
import randy.kits.CustomKit;
import randy.kits.Kits;

public class main extends JavaPlugin {

	//Listeners
	private final PlayerDeath playerDeathListener = new PlayerDeath();
	private final SetPosition setPositionListener = new SetPosition();
	private final SignInteract signInteractListener = new SignInteract();
	private final PlayerLeave playerLeaveListener = new PlayerLeave();

	//Spawn locations
	public static Location towerLocation;

	//Signs
	public static Location towerSign;

	//Kits
	public static CustomKit towerKit = null;

	//Players
	public static ArrayList<Player> towerPlayers = new ArrayList<Player>();

	//Scoreboard
	public static HashMap<String, ArrayList<Integer>> towerScores = new HashMap<String, ArrayList<Integer>>();

	@Override
	public void onDisable() {
		Config.SaveConfig();
		System.out.print("[FFA Extra] succesfully disabled.");
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(playerDeathListener, this);
		getServer().getPluginManager().registerEvents(setPositionListener, this);
		getServer().getPluginManager().registerEvents(signInteractListener, this);
		getServer().getPluginManager().registerEvents(playerLeaveListener, this);
		Config.LoadConfig();
		System.out.print("[FFA Extra] Succesfully enabled.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;

			if(commandName.equalsIgnoreCase("tower")){
				if(args.length == 3){
					if(args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("kit")){
						//if(player.hasPermission("FFAExtra.towerset")){
						CustomKit kit = Kits.getKitByName(args[2], "tower");
						if(kit != null){
							towerKit = kit;
							player.sendMessage(ChatColor.GREEN + "Tower kit succesfully set.");
						}else{
							player.sendMessage(ChatColor.RED + "Kit not found.");
						}
						return true;
						//}
					}
				}

				if(args.length == 2){
					if(args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("lobby")){
						//if(player.hasPermission("FFAExtra.towerset")){
						SetPosition.positiontype = "tower_lobby";
						SetPosition.stickPlayer = player;
						player.sendMessage(ChatColor.GREEN + "Right click a block with a stick to set the tower lobby.");
						return true;
						//}
					}
				}

				if(args.length == 0){
					Custody.switchCustody(player);
					SetPlayerTower(player);
					return true;
				}
			}
		}
		return false;
	}

	public static void SetPlayerTower(Player player){
		CoreAPI.ExitGameModes(player);

		player.teleport(towerLocation);
		towerPlayers.add(player);
		setTowerInventory(player);
		
		if(!towerScores.containsKey(player.getName())){
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(CoreDatabase.GetKills(player.getName(), "tower"));
			list.add(CoreDatabase.GetDeaths(player.getName(), "tower"));
			list.add(0);
			list.add(CoreDatabase.GetTowerKitGrabs(player.getName()));
			towerScores.put(player.getName(), list);
		}
		
		CoreScoreboard.UpdateScoreboard(player.getName());
	}
	
	public static void setTowerInventory(Player player){
		
		//Create the 3 items needed
		
		/*
		 * 	Compass - Game Menu (Right click to go)
			Clock - Kits (Select your kit)
			Nether star - Upgrades (Upgrade your kits)
		 */
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setArmorContents(new ItemStack[4]);
		
		//Compass		
		String name = "" + ChatColor.GREEN + ChatColor.BOLD +"Game menu";
		String lore = "" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Right click to go";
		inventory.addItem(Kits.getItem(Material.COMPASS, 1, name, lore, null, null));
		
		//Watch		
		name = ""+ ChatColor.GREEN + ChatColor.BOLD +"Kits";
		lore = ""+ ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Select your kit";
		inventory.addItem(Kits.getItem(Material.RECORD_12, 1, name, lore, null, null));
		
		//Nether star		
		name = ""+ ChatColor.GREEN + ChatColor.BOLD +"Upgrades";
		lore = ""+ ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Upgrade your kits";
		inventory.addItem(Kits.getItem(Material.NETHER_STAR, 1, name, lore, null, null));
	}

	public static void UpdateScoreboard(String player){
		ArrayList<Integer> score = towerScores.get(player);		
		CoreScoreboard.SetTitle(player, ChatColor.DARK_GREEN + "TOWER SCOREBOARD");
		CoreScoreboard.SetScore(player, ChatColor.GOLD + "Honor: ", "tower", CoreDatabase.GetCurrency(player));
		CoreScoreboard.SetScore(player, ChatColor.GOLD + "OP Kit: ", "tower", score.get(3));
		CoreScoreboard.SetScore(player, ChatColor.GOLD + "Killstreak: ", "tower", score.get(2));
		CoreScoreboard.SetScore(player, ChatColor.GOLD + "Deaths: ", "tower", score.get(1));
		CoreScoreboard.SetScore(player, ChatColor.GOLD + "Kills: ", "tower", score.get(0));
	}
}
