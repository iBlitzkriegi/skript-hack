package me.wheezygold.skripthack;

import ch.njol.skript.Skript;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;

public class SkriptHack extends JavaPlugin implements Listener {

    private static SkriptHack instance;

    @Override
    public void onEnable() {
        instance = this;
        if (getServer().getPluginManager().getPlugin("Skript")!=null) {
            log(C.cAqua + "#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#");
            log(C.cAqua + "This plugin's use is at your own risk!");
            log(C.cAqua + "This plugin can stop working at any moment and is not supported by Skript.");
            log(C.cAqua + "Known Issues: Types, Running Skript code when registrations are opened.");
            log(C.cAqua + "By using this plugin you agree that I am not responsible for what this does.");
            log(C.cAqua + "#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#+#");
            log("Starting Metrics...");
            Metrics metrics = new Metrics(this);
            metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Bukkit.getServer().getPluginManager().getPlugin("Skript").getDescription().getVersion()));
            log("Loaded Metrics!");
            if (Skript.isAcceptRegistrations()) {
                log("Loading Syntax...");
                try {
                    Skript.registerAddon(this).loadClasses("me.wheezygold.skripthack", "skript");
                    log("Loaded Syntax! (Normal Mode)");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                log("Funny, you're trying to run skript-hack, a plugin that loads syntax post-skript loading, when you in-fact installed an addon post-skript loading.");
                log("Anyway, I'll just hack my own syntax in.");
                try {
                    hackSkript(true);
                    Skript.registerAddon(this).loadClasses("me.wheezygold.skripthack", "skript");
                    hackSkript(false);
                    log("Loaded Syntax (Hacked Mode)");
                } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            log("We cannot hack skript when skript is not loaded.");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("skript-hack")) {
            if (args.length >= 1) {
                if (sender instanceof Player) {
                    final Player p = (Player) sender;
                    if (args[0].equalsIgnoreCase("true")) {
                        p.sendMessage(ChatColor.BLUE + "skript-hack >> " + ChatColor.GRAY + "Skript's registration system has been unlocked.");
                        try {
                            hackSkript(true);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("false")) {
                        p.sendMessage(ChatColor.BLUE + "skript-hack >> " + ChatColor.GRAY + "Skript's registration system has been locked.");
                        try {
                            hackSkript(false);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        return false;
                    }

                } else {
                    if (args[0].equalsIgnoreCase("true")) {
                        System.out.println(ChatColor.BLUE + "skript-hack >> " + ChatColor.GRAY + "Skript's registration system has been unlocked.");
                        return true;
                    } else if (args[0].equalsIgnoreCase("false")) {
                        System.out.println(ChatColor.BLUE + "skript-hack >> " + ChatColor.GRAY + "Skript's registration system has been locked.");
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private void log(String message) {
        getServer().getConsoleSender().sendMessage("[skript-hack] " + message);
    }

    public static SkriptHack getInstance() {
        return instance;
    }

    public void hackSkript(boolean allowReg) throws NoSuchFieldException, IllegalAccessException {
        Field field;
        field = Skript.class.getDeclaredField("acceptRegistrations");
        field.setAccessible(true);
        field.set(null, allowReg);
        log("Skript's Registrations was hacked to: " + String.valueOf(allowReg));
    }

    @SuppressWarnings("unused")
    public static void enableRegistrations() throws NoSuchFieldException, IllegalAccessException {
        instance.hackSkript(true);
    }

    @SuppressWarnings("unused")
    public static void disableRegistrations() throws NoSuchFieldException, IllegalAccessException {
        instance.hackSkript(false);
    }

    @SuppressWarnings("unused")
    public static class C {

        public static String Error = "§k";
        public static String Bold = "§l";
        public static String Strike = "§m";
        public static String BoldStrike = "§l§m";
        public static String NewLine = "§n";
        public static String Italics = "§o";

        public static String cAqua = "" + ChatColor.AQUA;
        public static String cBlack = "" + ChatColor.BLACK;
        public static String cBlue = "" + ChatColor.BLUE;
        public static String cDAqua = "" + ChatColor.DARK_AQUA;
        public static String cDBlue = "" + ChatColor.DARK_BLUE;
        public static String cDGray = "" + ChatColor.DARK_GRAY;
        public static String cDGreen = "" + ChatColor.DARK_GREEN;
        public static String cDPurple = "" + ChatColor.DARK_PURPLE;
        public static String cDRed = "" + ChatColor.DARK_RED;
        public static String cGold = "" + ChatColor.GOLD;
        public static String cGray = "" + ChatColor.GRAY;
        public static String cGreen = "" + ChatColor.GREEN;
        public static String cPurple = "" + ChatColor.LIGHT_PURPLE;
        public static String cRed = "" + ChatColor.RED;
        public static String cWhite = "" + ChatColor.WHITE;
        public static String cYellow = "" + ChatColor.YELLOW;

    }

}
