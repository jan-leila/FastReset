package fast_reset.client.mixin;

import fast_reset.client.FastReset;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    private static Text getButtonText() {
        String text;
        switch (FastReset.buttonLocation) {
            case 0:
                text = "bottom right";
                break;
            case 1:
                text = "center";
                break;
            case 2:
            default:
                text = "replace s&q";
        }
        return new LiteralText(text);
    }

    @Inject(method = "init", at=@At("TAIL"))
    public void initInject(CallbackInfo ci) {
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 142 - 4, 150, 20, getButtonText(), (buttonWidget) -> {
            FastReset.updateButtonLocation();
            buttonWidget.setMessage(getButtonText());
        }));
    }
}