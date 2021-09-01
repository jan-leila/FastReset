package fast_reset.client;

import net.fabricmc.api.ModInitializer;

public class Client implements ModInitializer {
    public static boolean saveOnQuit;
    public static boolean saving;
    public static final Object saveLock = new Object();

    @Override
    public void onInitialize() {
        saveOnQuit = true;
        saving = false;
        System.out.println("Using Fast Reset");
    }
}
