package fast_reset.client.mixin;

import fast_reset.client.FastReset;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

@Mixin(MinecraftServer.class)
public class ResetMixin {
    @Shadow @Final protected LevelStorage.Session session;

    // kill save on the shutdown
    @ModifyConstant(method = "shutdown", constant = @Constant(intValue = 0, ordinal = 0))
    private int disableWorldSaving(int savingDisabled) {
        return FastReset.saveOnQuit ? savingDisabled : 1;
    }

    @Redirect(method = "shutdown", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 1))
    private boolean closeWorldsRedirect(Iterator<ServerWorld> iterator) {
        if (FastReset.saveOnQuit) {
            return iterator.hasNext();
        }

        new Thread(() -> {
            synchronized(FastReset.saveLock) {
                while(iterator.hasNext()) {
                    ServerWorld world = iterator.next();
                    if (world != null) {
                        try {
                            world.close();
                        } catch (ConcurrentModificationException | IOException ignored) {
                        }
                    }
                }
                try {
                    this.session.deleteSessionLock();
                } catch (IllegalStateException | IOException ignored) {
                }
            }
        }).start();
        return false;
    }

    @Redirect(method = "shutdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;saveAllPlayerData()V"))
    private void disablePlayerSaving(PlayerManager playerManager) {
        if (FastReset.saveOnQuit) {
            playerManager.saveAllPlayerData();
        }
    }
}
