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

    private static final int bottomRightWidth = 102;

    // kill save on the shutdown
    @Redirect(method = "initWidgets", at = @At(value = "NEW", target = "net/minecraft/client/gui/widget/ButtonWidget", ordinal=7))
    private ButtonWidget createExitButton(int defaultX, int defaultY, int defaultWidth, int height, String message, ButtonWidget.PressAction onPress){
        int x = Client.buttonLocation == 2 ? (int) (this.width - (bottomRightWidth * 1.5) - 4) : defaultX;
        int y = Client.buttonLocation == 2 ? this.height - 24 : defaultY;
        int width = Client.buttonLocation == 2 ? (int) (bottomRightWidth * 1.5) : defaultWidth;

        return new ButtonWidget(x, y, width, height, message, (b) -> {
            Client.saveOnQuit = true;
            onPress.onPress(b);
        });
    }

    @Inject(method = "initWidgets", at=@At(value ="TAIL"))
    private void createSaveButton(CallbackInfo ci){
        int height = 20;

        int width;
        int x;
        int y;
        switch(Client.buttonLocation){
            // bottom right build
            case 0:
                width = 102;
                x = this.width - width - 4;
                y = this.height - height - 4;
                break;
            // center build
            case 1:
                width = 204;
                x = this.width / 2 - width/2;
                y = this.height / 4 + 148 - height;
                break;
            case 2:
            default:
                width = 204;
                x = this.width / 2 - width/2;
                y = this.height / 4 + 124 - height;
                break;
        }


        this.addButton(new ButtonWidget(x, y, width, height, new TranslatableText("menu.quitWorld").getString(), (buttonWidgetX) -> {
            Client.saveOnQuit = false;

            boolean bl = this.minecraft.isInSingleplayer();
            boolean bl2 = this.minecraft.isConnectedToRealms();
            buttonWidgetX.active = false;
            this.minecraft.world.disconnect();
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
