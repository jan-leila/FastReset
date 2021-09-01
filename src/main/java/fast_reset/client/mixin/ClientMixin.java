package fast_reset.client.mixin;

import fast_reset.client.Client;
import fast_reset.client.client.SaveWorldScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class ClientMixin {

    @Shadow public abstract void openScreen(Screen screen);

    @Inject(method = "startIntegratedServer", at=@At("HEAD"))
    public void worldWait(String name, String displayName, LevelInfo levelInfo, CallbackInfo ci){
        this.openScreen(new SaveWorldScreen());
        synchronized(Client.saveLock){
            System.out.println("done waiting for save lock");
        }
    }
}