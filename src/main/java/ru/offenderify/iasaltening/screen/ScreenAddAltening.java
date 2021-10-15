package ru.offenderify.iasaltening.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.SystemToast.Type;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import ru.offenderify.iasaltening.AlteningAccountStorage;
import ru.offenderify.iasaltening.AlteningFreeAccount;
import the_fireplace.ias.gui.AbstractAccountGui;
import the_fireplace.ias.gui.GuiAccountSelector;

public class ScreenAddAltening extends Screen {
    private Screen prev;
    private ButtonWidget doneButton;
    private TextFieldWidget token;

    public ScreenAddAltening(Screen prev) {
        super(new LiteralText(""));
        this.prev = prev;
    }

    protected void init() {
        addButton(token = new TextFieldWidget(textRenderer, width / 2 - 100, height / 2 - 50, 200, 20, new LiteralText("")));
        addButton(doneButton = new ButtonWidget(width / 2 - 102, height - 24, 100, 20, new TranslatableText("gui.done"),
                new PressAction() {
                    public void onPress(ButtonWidget button) {
                        try {
                            AlteningAccountStorage.getInstance().getAccounts()
                                    .add(new AlteningFreeAccount(token.getText()));
                            client.openScreen(prev);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            client.getToastManager().add(SystemToast.create(client, Type.TUTORIAL_HINT,
                                    new LiteralText("Error adding account"), new LiteralText(exception.toString())));
                        }
                    }
        }));
        addButton(new ButtonWidget(width / 2 + 2, height - 24, 100, 20, new TranslatableText("gui.cancel"),
                new PressAction() {
                    public void onPress(ButtonWidget button) {
                        client.openScreen(prev);
                    }
        }));
    }

    public void tick() {
        doneButton.active = !token.getText().isEmpty();
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        drawCenteredText(matrices, textRenderer, new LiteralText("The Altening"), width / 2, 10, 0xFFFFFFFF);
        drawCenteredText(matrices, textRenderer, new LiteralText("Free Token:"), width / 2, height / 2 - 60, 0xFFFFFFFF);
        drawCenteredText(matrices, textRenderer, new LiteralText("Premium token system is not implemented yet."), width / 2, height / 2 + 50, 0xFFFF0000);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
