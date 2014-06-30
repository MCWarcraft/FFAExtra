package randy.ffaextra;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import core.Custody.CustodySwitchEvent;

public class PlayerLeave implements Listener
{
	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		main.towerPlayers.remove(event.getPlayer());
	}
}
