package woodcutter.wc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands  implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(label.equalsIgnoreCase("woodcutter") || label.equalsIgnoreCase("wct")){
            if(!(sender instanceof Player)) return false;
            Player p = (Player) sender;
            if(PlayerListeners.disable.contains(p)){
                PlayerListeners.disable.remove(p);
                sender.sendMessage(ChatColor.GREEN + "[WoodCutter] " + ChatColor.WHITE + "機能を"
                        + ChatColor.BLUE +  "有効化" + ChatColor.WHITE + "しました.");
            }else{
                PlayerListeners.disable.add(p);
                sender.sendMessage(ChatColor.GREEN + "[WoodCutter] " + ChatColor.WHITE + "機能を"
                        + ChatColor.RED +  "無効化" + ChatColor.WHITE + "しました.");
            }
            return true;
        }

        return false;
    }
}
