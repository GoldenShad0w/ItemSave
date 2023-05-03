package goldenshadow.itemsave;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabComplete implements TabCompleter {


    List<String> arguments = new ArrayList<>();

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender.hasPermission("ItemSave.admin")){
            List<String> result = new ArrayList<>();
            if (args.length == 1) {
                arguments = new ArrayList<>(Arrays.asList("save", "give", "delete", "group", "give_nearest"));
                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
            if (args.length == 2) {

                if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("give_nearest")) {
                    arguments = ItemDataManager.getItemKeys();
                } else if (args[0].equalsIgnoreCase("group")) {
                    arguments = new ArrayList<>(Arrays.asList("create", "delete", "add_item", "remove_item", "give", "give_nearest"));
                } else if (args[0].equalsIgnoreCase("give")) {
                    arguments = new ArrayList<>();
                    Bukkit.getOnlinePlayers().forEach(p -> arguments.add(p.getName()));
                    arguments.add("<player>");
                }

                else arguments.clear();
                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("group") && (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("give_nearest") || args[1].equalsIgnoreCase("add_item") || args[1].equalsIgnoreCase("remove_item"))) {
                    arguments = ItemDataManager.getGroups();
                } else if (args[0].equalsIgnoreCase("give")) {
                    arguments = ItemDataManager.getItemKeys();
                } else if (args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("give")) {
                    arguments = new ArrayList<>();
                    Bukkit.getOnlinePlayers().forEach(p -> arguments.add(p.getName()));
                    arguments.add("<player>");
                }

                else arguments.clear();

                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[2].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
            if (args.length == 4) {

                if (args[0].equalsIgnoreCase("group") && (args[1].equalsIgnoreCase("add_item"))) {
                    arguments = ItemDataManager.getItemKeys();
                } else if (args[0].equalsIgnoreCase("group") && (args[1].equalsIgnoreCase("remove_item"))) {
                    arguments = ItemDataManager.getGroupContents(args[3]);
                } else if (args[0].equalsIgnoreCase("give")) {
                    arguments = new ArrayList<>(Collections.singletonList("silent"));
                } else if (args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("give")) {
                    arguments = ItemDataManager.getGroups();
                }

                else arguments.clear();

                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[3].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
            if (args.length == 5) {

                if (args[0].equalsIgnoreCase("group") && (args[1].equalsIgnoreCase("give"))) {
                    arguments = new ArrayList<>(Collections.singletonList("silent"));
                }
                else arguments.clear();

                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[3].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
            if (args.length > 6) {
                arguments.clear();
                return arguments;
            }
        }
        return null;
    }


}
