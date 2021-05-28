package fast_reset.client.mixin;

import fast_reset.client.Client;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;


@Mixin(MinecraftServer.class)
public class ResetMixin {
    // kill save on the shutdown
    @Redirect(method = "shutdown", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 0))
    private boolean getWorldsInjectOne(Iterator<ServerWorld> iterator){
        if(Client.saveOnQuit){
            return iterator.hasNext();
        }
        while(iterator.hasNext()){
            iterator.next().savingDisabled = true;
        }
        return false;
    }

    @Redirect(method = "shutdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;saveAllPlayerData()V"))
    private void shutdownPlayerSaveInject(PlayerManager playerManager){
        if(Client.saveOnQuit){
            playerManager.saveAllPlayerData();
        }
    }

    // kill all things auto save things
//    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;save(ZZZ)Z"))
//    private boolean autoSaveInject(MinecraftServer server, boolean a, boolean b, boolean c){
//        return false;
//    }
//
//    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;saveAllPlayerData()V"))
//    private void shutdownPlayerDataSaveInject(PlayerManager server){}
}
