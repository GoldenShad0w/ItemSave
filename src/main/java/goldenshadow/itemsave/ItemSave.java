package goldenshadow.itemsave;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ItemSave extends JavaPlugin {

    private static ItemSave plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        Objects.requireNonNull(this.getCommand("itemsave")).setExecutor(new Command());
        Objects.requireNonNull(this.getCommand("itemsave")).setTabCompleter(new TabComplete());

        //delayed by one tick because otherwise it sometimes won't be able to load persistent data correctly
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, ItemDataManager::loadFromFile, 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, BackupManager::startBackupLoop, 10L);

    }

    @Override
    public void onDisable() {
        ItemDataManager.saveToFile();
    }

    public static ItemSave getPlugin() {
        return plugin;
    }
}
