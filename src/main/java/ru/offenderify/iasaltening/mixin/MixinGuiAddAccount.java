package ru.offenderify.iasaltening.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import ru.offenderify.iasaltening.screen.ScreenAddAltening;
import the_fireplace.ias.gui.AbstractAccountGui;
import the_fireplace.ias.gui.GuiAddAccount;

@Mixin(value = GuiAddAccount.class)
public class MixinGuiAddAccount extends AbstractAccountGui {
    private MixinGuiAddAccount() {
        super(null, null);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    private void onInit(CallbackInfo callback) {
        addButton(new ButtonWidget(width / 2 - 50, this.height / 3 * 2 - 24, 100, 20, new LiteralText("The Altening"), new PressAction() {
            public void onPress(ButtonWidget button) {
                client.openScreen(new ScreenAddAltening(prev));
            }
        }));
    }

    @Override
    public void complete() {
        throw new UnsupportedOperationException();
    }
}
