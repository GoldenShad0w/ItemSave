package goldenshadow.itemsave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class ItemDataManager {

    private static Map<String,ItemStack> itemMap = new HashMap<>();
    private static Map<String, List<String>> groupMap = new HashMap<>();

    public static void loadFromFile() {
        try {
            Gson gson = new GsonBuilder()
                    .create();
            File itemFile = new File(ItemSave.getPlugin().getDataFolder().getAbsolutePath() + "/items.json");
            File groupFile = new File(ItemSave.getPlugin().getDataFolder().getAbsolutePath() + "/item_groups.json");

            if (itemFile.exists()) {
                Reader reader = new FileReader(itemFile);

                Type type = new TypeToken<HashMap<String, String>>() {
                }.getType();


                Map<String, String> encodedMap = gson.fromJson(reader, type);
                if (encodedMap == null) itemMap = new HashMap<>();
                else {
                    itemMap.clear();
                    for (String s : encodedMap.keySet()) {
                        itemMap.put(s, decodeItem(encodedMap.get(s)));
                    }
                }

            }
            if (groupFile.exists()) {
                Reader reader2 = new FileReader(groupFile);
                Type type2 = new TypeToken<HashMap<String, List<String>>>() {
                }.getType();
                Map<String, List<String>> groups = gson.fromJson(reader2, type2);
                if (groups == null) groupMap = new HashMap<>();
                else groupMap = groups;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile() {
        Map<String,String> encodedItems = new HashMap<>();
        for (String s : itemMap.keySet()) {
            encodedItems.put(s, encodeItem(itemMap.get(s)));
        }

        try {
            Gson gson = new GsonBuilder()
                    .create();
            File file = new File(ItemSave.getPlugin().getDataFolder().getAbsolutePath() + "/items.json");
            File file2 = new File(ItemSave.getPlugin().getDataFolder().getAbsolutePath() + "/item_groups.json");
            file.getParentFile().mkdir();
            file2.getParentFile().mkdir();

            file.createNewFile();
            file2.createNewFile();
            Writer writer = new FileWriter(file, false);
            Writer writer2 = new FileWriter(file2, false);
            gson.toJson(encodedItems, writer);
            gson.toJson(groupMap, writer2);
            writer2.flush();
            writer.flush();
            writer2.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<String> getItemKeys() {
        if (itemMap != null) {
            return new ArrayList<>(itemMap.keySet());
        }
        return new ArrayList<>();
    }

    public static ItemStack getItem(String itemKey) {
        if (itemMap.containsKey(itemKey)) {
            return itemMap.get(itemKey);
        }
        return new ItemStack(Material.RED_WOOL);
    }

    public static void deleteItem(String itemKey) {
        itemMap.remove(itemKey);
        saveToFile();
        loadFromFile();
    }

    public static void saveItem(ItemStack itemStack, String name) {
        itemMap.put(name,itemStack);
        saveToFile();
        loadFromFile();
    }

    public static void createGroup(String name) {
        groupMap.put(name, new ArrayList<>());
        saveToFile();
        loadFromFile();
    }

    public static void addItemToGroup(String group, String item) {
        if (groupMap.containsKey(group)) {
            groupMap.get(group).add(item);
            saveToFile();
            loadFromFile();
        }
    }

    public static void removeItemFromGroup(String group, String item) {
        if (groupMap.containsKey(group)) {
            groupMap.get(group).remove(item);
            saveToFile();
            loadFromFile();
        }
    }

    public static void deleteGroup(String group) {
        groupMap.remove(group);
        saveToFile();
        loadFromFile();
    }

    public static List<ItemStack> getItemsFromGroup(String group) {
        List<ItemStack> list = new ArrayList<>();
        if (groupMap.containsKey(group)) {
            for (String s : groupMap.get(group)) {
                list.add(getItem(s));
            }
        }
        return list;
    }

    public static List<String> getGroups() {
        return new ArrayList<>(groupMap.keySet());
    }
    public static List<String> getGroupContents(String group) {
        List<String> list = new ArrayList<>();
        if (groupMap.containsKey(group)) {
            list = groupMap.get(group);
        }
        return list;
    }

    private static String encodeItem(ItemStack itemStack) {
        String encoded;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(itemStack);
            objectOutputStream.flush();

            byte[] serialized = byteArrayOutputStream.toByteArray();
            encoded = Base64.getEncoder().encodeToString(serialized);

            objectOutputStream.close();

            return encoded;


        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    private static ItemStack decodeItem(String string) {
        if (string.equals("error")) return new ItemStack(Material.RED_WOOL);

        try {
            byte[] serializedObject = Base64.getDecoder().decode(string);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);
            BukkitObjectInputStream inputStream = new BukkitObjectInputStream(byteArrayInputStream);

            ItemStack i = (ItemStack) inputStream.readObject();
            inputStream.close();
            return i;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return getErrorItem();
        }
    }

    private static ItemStack getErrorItem() {
        ItemStack itemStack = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + String.valueOf(ChatColor.BOLD) + "ERROR");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "The item that was supposed to be loaded");
        lore.add(ChatColor.GRAY + "has probably been deleted but not removed");
        lore.add(ChatColor.GRAY + "from a group.");
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
