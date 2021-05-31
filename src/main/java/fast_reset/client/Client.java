package fast_reset.client;

import net.fabricmc.api.ModInitializer;

public class Client implements ModInitializer {
    public static boolean saveOnQuit;

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "fastreset";
    public static final String MOD_NAME = "Fast Reset";

    @Override
    public void onInitialize() {
        saveOnQuit = true;
        log(Level.INFO, "Using Fast Reset");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }
}
