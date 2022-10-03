package fast_reset.client.mixin;

import fast_reset.client.FastReset;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin extends Screen {

    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "createLevel", at = @At("HEAD"))
    private void worldWait(CallbackInfo ci) {
        this.client.method_29970(new SaveLevelScreen(new TranslatableText("still saving the last world")));
        synchronized(FastReset.saveLock) {
            FastReset.LOGGER.info("done waiting for save lock");
        }
    }

}
