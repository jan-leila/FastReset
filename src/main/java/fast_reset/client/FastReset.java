package fast_reset.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;

public class FastReset implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    private static final File configurationFile = FabricLoader.getInstance().getConfigDir().resolve("fastReset").resolve("settings.txt").toFile();

    public static boolean saveOnQuit = true;
    public static final Object saveLock = new Object();

    public static int buttonLocation = 0;

    public static void updateButtonLocation() {
        buttonLocation = (buttonLocation + 1) % 3;
        save();
    }

    private static void save() {
        try {
            Files.write(configurationFile.toPath(), String.valueOf(buttonLocation).getBytes());
        } catch (IOException e) {
            LOGGER.error("Failed to save FastReset config", e);
        }
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Using Fast Reset");

        if (!configurationFile.exists()) {
            try {
                if (!configurationFile.getParentFile().mkdirs()) {
                    throw new IOException("couldn't make config folder");
                }
                if (!configurationFile.createNewFile()) {
                    throw new IOException("couldn't make config file");
                }
                save();
            } catch (IOException e) {
                LOGGER.error("Failed to create FastReset config", e);
            }
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(configurationFile));
            buttonLocation = Integer.parseInt(reader.readLine()) % 3;
            reader.close();
        } catch (IOException | NumberFormatException e) {
            LOGGER.error("Failed to load FastReset config", e);
        }
    }
}
