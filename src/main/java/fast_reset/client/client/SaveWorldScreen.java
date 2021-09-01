package fast_reset.client.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.awt.*;

public class SaveWorldScreen extends Screen {
    public SaveWorldScreen(){
        super(new TranslatableText("menu.savingLevel"));
    }

    public void render(int mouseX, int mouseY, float delta){
        this.renderBackground();

        this.drawCenteredString(this.font, new TranslatableText("still saving the last world").asString(), this.width / 2, 70, Color.white.getRGB());

        super.render(mouseX, mouseY, delta);
    }
}