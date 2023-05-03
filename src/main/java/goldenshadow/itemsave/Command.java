package goldenshadow.itemsave;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof BlockCommandSender b) {
            if (args.length >= 2) {
                if (args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("give_nearest")) {
                    if (args.length == 3) {
                        if (ItemDataManager.getGroups().contains(args[2].toLowerCase())) {
                            Player nearestPlayer = getNearestPlayer(b.getBlock().getLocation());
                            if (nearestPlayer != null) {
                                for (ItemStack i : ItemDataManager.getItemsFromGroup(args[2].toLowerCase())) {
                                    nearestPlayer.getInventory().addItem(i);
                                }
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("give_nearest")) {
                    if (ItemDataManager.getItemKeys().contains(args[1].toLowerCase())) {
                        Player nearestPlayer = getNearestPlayer(b.getBlock().getLocation());
                        if (nearestPlayer != null) {
                            nearestPlayer.getInventory().addItem(ItemDataManager.getItem(args[1].toLowerCase()));
                        }
                    }
                }
                return true;
            }

        }



        if (args.length >= 2) {
                if (args[0].equalsIgnoreCase("save")) {
                    String name = args[1];
                    if (ItemDataManager.getItemKeys().contains(name.toLowerCase())) {
                        sender.sendMessage(ChatColor.RED + "[ItemSave] Error: An item with that name exists already! Please delete the old one before saving a new item under this name!");
                        return true;
                    }
                    if (sender instanceof Player player) {
                        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                            sender.sendMessage(ChatColor.RED + "[ItemSave] Error: You must be holding the item you want to save!");
                            return true;
                        }
                        ItemDataManager.saveItem(player.getInventory().getItemInMainHand().clone(), name.toLowerCase());
                        sender.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "Saved item!");
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "[ItemSave] Error: This command must be run by a player!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("give")) {

                    if (args[1].equals("<player>")) {
                        if (ItemDataManager.getItemKeys().contains(args[2].toLowerCase())) {
                            if (sender instanceof Player player) {
                                player.getInventory().addItem(ItemDataManager.getItem(args[2].toLowerCase()));
                                if (args.length < 4) {
                                    player.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "Gave you saved item!");
                                }
                                return true;
                            }
                            sender.sendMessage(ChatColor.RED + "[ItemSave] Error: This command must be run by a player!");
                            return true;
                        }
                    }
                    if (ItemDataManager.getItemKeys().contains(args[2].toLowerCase())) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            target.getInventory().addItem(ItemDataManager.getItem(args[2].toLowerCase()));
                            if (args.length < 4) {
                                target.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "Gave you saved item!");
                            }
                            return true;
                        }
                        sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No player of that name is currently online!");
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No saved item of that name exists!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (ItemDataManager.getItemKeys().contains(args[1].toLowerCase())) {
                        ItemDataManager.deleteItem(args[1].toLowerCase());
                        sender.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "Item deleted!");
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No saved item of that name exists!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("group")) {
                    if (args.length >= 3) {
                        if (args[1].equalsIgnoreCase("create")) {
                            if (args.length == 3) {
                                if (ItemDataManager.getGroups().contains(args[2].toLowerCase())) {
                                    sender.sendMessage(ChatColor.RED + "[ItemSave] Error: A group of this name already exists!");
                                    return true;
                                }
                                ItemDataManager.createGroup(args[2].toLowerCase());
                                sender.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "New group created!");
                                return true;

                            }
                            sender.sendMessage(ChatColor.RED + "[ItemSave] Usage: /itemsave group create <name>");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("delete")) {
                            if (args.length == 3) {
                                if (ItemDataManager.getGroups().contains(args[2].toLowerCase())) {
                                    ItemDataManager.deleteGroup(args[2].toLowerCase());
                                    sender.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "Group deleted!");
                                    return true;
                                }
                                sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No group of this name exists!");
                                return true;

                            }
                            sender.sendMessage(ChatColor.RED + "[ItemSave] Usage: /itemsave group delete <group>");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("give")) {
                            if (args.length >= 4) {
                                if (args[2].equals("<player>")) {
                                    if (ItemDataManager.getGroups().contains(args[3].toLowerCase())) {
                                        if (sender instanceof Player player) {
                                            for (ItemStack i : ItemDataManager.getItemsFromGroup(args[3].toLowerCase())) {
                                                player.getInventory().addItem(i);
                                            }
                                            if (args.length == 4) {
                                                player.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "Gave you all group items!");
                                            }
                                            return true;
                                        }
                                        sender.sendMessage(ChatColor.RED + "[ItemSave] Error: This command must be run by a player!");
                                    }
                                }
                                if (ItemDataManager.getGroups().contains(args[3].toLowerCase())) {

                                    Player target = Bukkit.getPlayer(args[2]);
                                    if (target != null) {

                                        for (ItemStack i : ItemDataManager.getItemsFromGroup(args[3].toLowerCase())) {
                                            target.getInventory().addItem(i);
                                        }
                                        if (args.length == 4) {
                                            sender.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "Gave " + target.getName() +  " all group items!");
                                        }
                                        return true;
                                    }
                                    sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No player of that name is currently online!");
                                    return true;
                                }
                                sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No group of this name exists!");
                                return true;

                            }
                            sender.sendMessage(ChatColor.RED + "[ItemSave] Usage: /itemsave group give <player> <group> <optional: silent>");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("give_nearest")) {
                            if (args.length == 3) {

                                if (sender instanceof Player player) {

                                    if (ItemDataManager.getGroups().contains(args[2].toLowerCase())) {
                                        for (ItemStack i : ItemDataManager.getItemsFromGroup(args[2].toLowerCase())) {
                                            player.getInventory().addItem(i);
                                        }
                                        return true;
                                    }
                                    player.sendMessage(ChatColor.RED + "[ItemSave] Error: No group of this name exists!");
                                    return true;
                                }
                                sender.sendMessage(ChatColor.RED + "[ItemSave] Error: This command must be run by a player or a command block!");
                                return true;
                            }
                            sender.sendMessage(ChatColor.RED + "[ItemSave] Usage: /aurum item group give_nearest <group>");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("add_item")) {
                            if (args.length == 4) {
                                if (ItemDataManager.getGroups().contains(args[2].toLowerCase())) {
                                    if (ItemDataManager.getItemKeys().contains(args[3].toLowerCase())) {
                                        ItemDataManager.addItemToGroup(args[2].toLowerCase(), args[3].toLowerCase());
                                        sender.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "Added item to group!");
                                        return true;
                                    }
                                    sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No item of this name exists!");
                                    return true;
                                }
                                sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No group of this name exists!");
                                return true;
                            }
                            sender.sendMessage(ChatColor.RED + "[ItemSave] Usage: /itemsave group add_item <group> <item>");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("remove_item")) {
                            if (args.length == 4) {
                                if (ItemDataManager.getGroups().contains(args[2].toLowerCase())) {
                                    if (ItemDataManager.getItemKeys().contains(args[3].toLowerCase())) {
                                        ItemDataManager.removeItemFromGroup(args[2].toLowerCase(), args[3].toLowerCase());
                                        sender.sendMessage(ChatColor.GOLD + "[ItemSave] " + ChatColor.YELLOW + "Removed item from group!");
                                        return true;
                                    }
                                    sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No item of this name exists!");
                                    return true;
                                }
                                sender.sendMessage(ChatColor.RED + "[ItemSave] Error: No group of this name exists!");
                                return true;
                            }
                            sender.sendMessage(ChatColor.RED + "[ItemSave] Usage: /aurum item group remove_item <group> <item>");
                            return true;
                        }
                    }
                }

            sender.sendMessage(ChatColor.RED + "[ItemSave] Usage: /itemsave <give/save/delete/group> ...");
            return true;
        }




        return false;
    }

    private static Player getNearestPlayer(Location commandBlockLocation) {
        Player nearestPlayer = null;
        double distance = 5;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().equals(commandBlockLocation.getWorld())) {
                double d = p.getLocation().distance(commandBlockLocation);
                if (d < distance) {
                    nearestPlayer = p;
                    distance = d;
                }
            }
        }
        return nearestPlayer;
    }
}
