package fast_reset.client.mixin;

import fast_reset.client.Client;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.util.Iterator;

@Mixin(MinecraftServer.class)
public class ResetMixin {
    @Shadow @Final protected LevelStorage.Session session;

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

    @Redirect(method = "shutdown", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 1))
    private boolean getWorldsInjectTwo(Iterator<ServerWorld> iterator){
        if(Client.saveOnQuit){
            return iterator.hasNext();
        }

        new Thread(() -> {
            synchronized(Client.saveLock){
                while(iterator.hasNext()){
                    ServerWorld world = iterator.next();
                    if (world != null) {
                        try {
                            world.close();
                        } catch (IOException ignored) {

                        }
                    }
                }
                try {
                    this.session.deleteSessionLock();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
