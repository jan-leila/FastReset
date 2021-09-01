package fast_reset.client.mixin;

import fast_reset.client.Client;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
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

    private static Text getButtonText(){
        switch(Client.buttonLocation){
            case 0:
                return new LiteralText("bottom right");
            case 1:
                return new LiteralText("center");
            case 2:
            default:
                return new LiteralText("replace s&q");
        }
    }

    @Inject(method = "init", at=@At("TAIL"))
    public void initInject(CallbackInfo ci){
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 142 - 4, 150, 20, getButtonText(), (buttonWidget) -> {
            Client.updateButtonLocation();
            buttonWidget.setMessage(getButtonText());
        }));
    }
}
