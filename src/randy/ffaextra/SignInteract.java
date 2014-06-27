package randy.ffaextra;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import randy.core.CoreScoreboard;
import randy.core.tools.CoreDatabase;

public class SignInteract implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Location blockLoc = event.getClickedBlock().getLocation();
			if(main.towerSign.equals(blockLoc)){
				main.towerKit.AssignKitToPlayer(player);
				CoreDatabase.ModifyTowerKitGrabs(player.getName(), 1);
				main.towerScores.get(player.getName()).set(3, main.towerScores.get(player.getName()).get(3) + 1);
				CoreScoreboard.UpdateScoreboard(player.getName());
				player.sendMessage(ChatColor.GREEN + "You got the OP kit!");
			}
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		if(event.getLine(0).equalsIgnoreCase("[Tower]") ){
			event.setLine(0, "");
			event.setLine(1, ""+ChatColor.DARK_BLUE + ChatColor.BOLD + "- OP KIT -");
			event.setLine(2, "");
			event.setLine(3, "");
			main.towerSign = event.getBlock().getLocation();
			
			event.getPlayer().sendMessage(ChatColor.GREEN + "Tower sign set!");
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(player.hasPermission("FFAExtra.towerset")){
			if(main.towerSign == event.getBlock().getLocation()) main.towerSign = null;
		}
	}
}
