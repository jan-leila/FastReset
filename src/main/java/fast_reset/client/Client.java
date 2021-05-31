package fast_reset.client;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client implements ModInitializer {
    public static boolean saveOnQuit;
    public static int buttonPos;

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "fastreset";
    public static final String MOD_NAME = "Fast Reset";

    @Override
    public void onInitialize() {
        saveOnQuit = true;
        buttonPos = 1;
        log(Level.INFO, "Using Fast Reset");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }
}
