package fast_reset.client.mixin;

import fast_reset.client.Client;
import net.minecraft.server.world.ServerChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin {

    @Redirect(method = "close", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;save(Z)V"))
    private void closeRedirect(ServerChunkManager serverChunkManager, boolean flush) {
        if(Client.saveOnQuit){
            serverChunkManager.save(flush);
        }
    }
}
