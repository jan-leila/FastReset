package fast_reset.client.mixin;

import fast_reset.client.Client;
import fast_reset.client.client.SaveWorldScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class ClientMixin {

    @Shadow public abstract void openScreen(Screen screen);

    @Inject(method = "createWorld", at=@At("HEAD"))
    public void worldWait(String worldName, LevelInfo levelInfo, DynamicRegistryManager.Impl registryTracker, GeneratorOptions generatorOptions, CallbackInfo ci){
        this.openScreen(new SaveWorldScreen());
        synchronized(Client.saveLock){
            System.out.println("done waiting for save lock");
        }
    }
}