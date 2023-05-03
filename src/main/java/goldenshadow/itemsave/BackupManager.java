package goldenshadow.itemsave;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupManager {

    public static void startBackupLoop() {
        int freqInMinutes = ItemSave.getPlugin().getConfig().getInt("backup-frequency-in-minutes");
        long delay = ((long) freqInMinutes * 60 * 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                backUpFiles();
            }
        }.runTaskTimer(ItemSave.getPlugin(), 0, delay);
    }

    private static void backUpFiles() {
        try {

            File itemFile = new File(ItemSave.getPlugin().getDataFolder().getAbsolutePath() + "/items.json");
            File groupFile = new File(ItemSave.getPlugin().getDataFolder().getAbsolutePath() + "/item_groups.json");

            File[] files = {itemFile, groupFile};

            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy__HH_mm_ss");

            String backupDirectory = ItemSave.getPlugin().getDataFolder().getAbsolutePath() + "/backups/" + sdf.format(new Date());
            Files.createDirectories(Paths.get(backupDirectory));
            for (File file : files) {
                Path sourceFile = file.toPath();
                Path targetFile = Paths.get(backupDirectory + "/" + file.getName());
                Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
