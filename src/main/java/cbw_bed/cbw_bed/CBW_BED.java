package cbw_bed.cbw_bed;

import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.*;

public final class CBW_BED extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pl = Bukkit.getServer().getPluginManager();
        pl.registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length == 1) ;
            if (!StringUtils.isNumeric(args[0])) {
            return true;
            }
            final int count = Integer.valueOf(args[0]);
            if(count <= 0) {
                sender.sendMessage("カウントは1以上にしてください。");
                return true;
            }
            if(cmd.getName().equalsIgnoreCase("task")) {
                BukkitRunnable task = new BukkitRunnable() {
                int i = count;
                @Override
                public void run() {
                    if(i==0) {
                        sender.sendMessage("カウントが0になりました。");
                        cancel();
                        return;
                    }

                    sender.sendMessage("カウント" + i);
                    i--;

                }
            };
            task.runTaskTimer(this, 0L, 20L);
            return true;
        }
        return false;
    }

    @EventHandler
    public void bedEvent(PlayerBedEnterEvent e) {
        Player player = e.getPlayer();
        int tick = player.getSleepTicks();
        if (tick == 0) {
            BukkitRunnable task = new BukkitRunnable() {
                int i = 5;
                @Override
                public void run() {
                    if(player.isSleeping() == false) {
                        player.sendMessage("寝ていません。");
                        cancel();
                        return;
                    }

                    if(i==0) {
                        player.sendMessage("朝になりました。");
                        player.wakeup(true);
                        String s = e.getPlayer().getWorld().getName();
                        String a = "[";
                        String b = "]";
                        World world = getServer().getWorld(s);
                        world.setTime(0);
                        world.setStorm(true);
                        String s2 = e.getPlayer().getName();
                        Bukkit.broadcastMessage(ChatColor.WHITE + a + ChatColor.GRAY + s + ChatColor.RESET + b + ChatColor.WHITE + s2 + ChatColor.RESET + ChatColor.GRAY + "がベッドで寝ました。");
                        cancel();

                        if(world.hasStorm()) {
                            world.setStorm(false);
                            return;
                        }
                    }

                        player.sendMessage("朝になるまであと" + i);
                    i--;

                }
            };
            task.runTaskTimer(this, 0L, 20L);
        }
    }
}
