package fast_reset.client.mixin;

import fast_reset.client.FastReset;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin extends Screen {

    protected GameMenuMixin(Text title) {
        super(title);
    }

    @ModifyVariable(method = "initWidgets", at = @At(value = "STORE", ordinal = 1))
    private ButtonWidget createExitButton(ButtonWidget saveButton) {
        if (!this.client.isInSingleplayer()) {
            return saveButton;
        }

        int height = 20;
        int width;
        int x;
        int y;
        switch (FastReset.buttonLocation){
            // bottom right build
            case 0:
                width = 102;
                x = this.width - width - 4;
                y = this.height - height - 4;
                break;
            // center build
            case 1:
                width = 204;
                x = this.width / 2 - width / 2;
                y = this.height / 4 + 148 - height;
                break;
            // replace s&q build
            case 2:
            default:
                width = 204;
                x = this.width / 2 - width / 2;
                y = this.height / 4 + 124 - height;

                saveButton.x = (int) (this.width - (102 * 1.5) - 4);
                saveButton.y = this.height - 24;
                saveButton.setWidth((int) (102 * 1.5));
                break;
        }

        this.addButton(new ButtonWidget(x, y, width, height, new TranslatableText("menu.quitWorld"), (buttonWidgetX) -> {
            FastReset.saveOnQuit = false;
            saveButton.onPress();
            FastReset.saveOnQuit = true;
        }));
        return saveButton;
    }
}