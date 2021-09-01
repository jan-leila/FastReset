package fast_reset.client;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class Client implements ModInitializer {
    private static File configurationFile;

    public static boolean saveOnQuit;
    public static boolean saving;
    public static final Object saveLock = new Object();

    public static int buttonLocation;

    public static void updateButtonLocation(){
        buttonLocation++;
        if(buttonLocation > 2){
            buttonLocation = 0;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configurationFile));
            writer.write(String.valueOf(buttonLocation));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitialize() {
        saveOnQuit = true;
        saving = false;
        System.out.println("Using Fast Reset");

        configurationFile = new File(new File(FabricLoader.getInstance().getConfigDir().toFile(), "fastReset"), "settings.txt");

        if (!configurationFile.exists()) {
            try {
                if(!configurationFile.getParentFile().mkdirs()){
                    throw new IOException("couldn't make config folder");
                }
                if(!configurationFile.createNewFile()){
                    throw new IOException("couldn't make config file");
                }
                FileWriter writer = new FileWriter(configurationFile);
                writer.write("0");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(configurationFile));
            String line = reader.readLine();
            buttonLocation = Integer.parseInt(line);
        } catch (IOException | NumberFormatException e) {
            buttonLocation = 0;
        }
    }
}
