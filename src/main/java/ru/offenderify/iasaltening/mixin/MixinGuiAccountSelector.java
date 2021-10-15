package ru.offenderify.iasaltening.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import ru.offenderify.iasaltening.AlteningAccount;
import ru.offenderify.iasaltening.AlteningAccountStorage;
import ru.vidtu.iasfork.msauth.Account;
import the_fireplace.ias.gui.GuiAccountSelector;

@Mixin(value = GuiAccountSelector.class)
public class MixinGuiAccountSelector {
    @Shadow
    private Throwable loginfailed;
    @Shadow
    private ButtonWidget login;
    @Shadow
    private ButtonWidget loginoffline;
    @Shadow
    private ButtonWidget delete;
    @Shadow
    private ButtonWidget edit;
    @Shadow
    private ButtonWidget reloadskins;
    @Shadow
    private int selectedAccountIndex;
    @Shadow
    private ArrayList<Account> queriedaccounts;

    @Inject(method = "convertData", at = @At(value = "TAIL"), cancellable = true, remap = false)
    private void onConvertData(CallbackInfoReturnable<ArrayList<Account>> callback) {
        ArrayList<Account> accountList = callback.getReturnValue();
        accountList.addAll(AlteningAccountStorage.getInstance().getAccounts());
        callback.setReturnValue(accountList);
    }

    @Inject(method = "updateButtons", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void onUpdateButtons(CallbackInfo callback) {
        if (!queriedaccounts.isEmpty() && queriedaccounts.get(selectedAccountIndex) instanceof AlteningAccount) {
            login.active = true;
            loginoffline.active = true;
            delete.active = true;
            edit.active = false;
            reloadskins.active = true;
            callback.cancel();
        }
    }

    @Inject(method = "delete", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void onDelete(CallbackInfo callback) {
        if (!queriedaccounts.isEmpty() && queriedaccounts.get(selectedAccountIndex) instanceof AlteningAccount) {
            AlteningAccountStorage.getInstance().getAccounts().remove(queriedaccounts.get(this.selectedAccountIndex));
            this.updateQueried();
            this.updateButtons();
            callback.cancel();
        }
    }

    @Inject(method = "removed", at = @At(value = "TAIL"))
    public void onRemoved(CallbackInfo callback) {
        AlteningAccountStorage.getInstance().write();
    }

    @Inject(method = "login", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void onLogin(int selected, CallbackInfo callback) {
        Account data = (Account)this.queriedaccounts.get(selected);
        if (data instanceof AlteningAccount) {
            this.loginfailed = data.login();
            callback.cancel();
        }
    }

    @Shadow
    private void updateQueried() {
    }

    @Shadow
    private void updateButtons() {
    }
}
