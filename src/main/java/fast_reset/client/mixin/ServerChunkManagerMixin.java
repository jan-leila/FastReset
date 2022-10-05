package fast_reset.client.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import fast_reset.client.FastReset;
import net.minecraft.server.world.ServerChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {

    @WrapWithCondition(method = "close", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;save(Z)V"))
    private boolean closeRedirect(ServerChunkManager serverChunkManager, boolean flush) {
        return FastReset.saveOnQuit;
    }
}
