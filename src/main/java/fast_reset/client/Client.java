package fast_reset.client;

import net.fabricmc.api.ModInitializer;

public class Client implements ModInitializer {
    public static boolean saveOnQuit;

    @Override
    public void onInitialize() {
        saveOnQuit = true;
        System.out.println("Using Fast Reset");
    }
}
