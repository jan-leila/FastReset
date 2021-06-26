package fast_reset.client.mixin;

import fast_reset.client.Client;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin extends Screen {

    protected GameMenuMixin(Text title) {
        super(title);
    }

    // kill save on the shutdown
    @Redirect(method = "initWidgets", at = @At(value = "NEW", target = "net/minecraft/client/gui/widget/ButtonWidget", ordinal = 7))
    private ButtonWidget createExitButton(int x, int y, int width, int height, String message,
        ButtonWidget.PressAction onPress) {
        return new ButtonWidget(x, y, width, height, message, (b) -> {
            Client.saveOnQuit = true;
            onPress.onPress(b);
        });
    }

    @Inject(method = "initWidgets", at=@At(value ="TAIL"))
    private void createSaveButton(CallbackInfo ci) {
        int height = 20;

        int width, x, y;

        if (Client.buttonPos == 0) {
            // bottom left build
            width = 102;
            x = this.width - width - 4;
            y = this.height - height - 4;
        } else if (Client.buttonPos == 1) {
            // center build
            width = 204;
            x = this.width / 2 - width / 2;
            y = this.height / 4 + 148 - height;
        } else {
            throw new IllegalStateException("invalid button pos " + Client.buttonPos);
        }

        this.addButton(new ButtonWidget(x, y, width, height, "Quit World", (buttonWidgetx) -> {
            Client.saveOnQuit = false;

            boolean bl = this.minecraft.isInSingleplayer();
            boolean bl2 = this.minecraft.isConnectedToRealms();
            buttonWidgetx.active = false;
            this.minecraft.world.disconnect();

            Client.log(Level.INFO, "Fast quitting world");

            if (bl) {
                this.minecraft.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
            } else {
                this.minecraft.disconnect();
            }

            if (bl) {
                this.minecraft.openScreen(new TitleScreen());
            } else if (bl2) {
                RealmsBridge realmsBridge = new RealmsBridge();
                realmsBridge.switchToRealms(new TitleScreen());
            } else {
                this.minecraft.openScreen(new MultiplayerScreen(new TitleScreen()));
            }
        }));
    }
}
