package ru.offenderify.iasaltening;

import java.net.Proxy;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import net.fabricmc.api.ModInitializer;
import the_fireplace.ias.gui.GuiAddAccount;

public class IASAlteningMod implements ModInitializer {
    private static IASAlteningMod instance;
    private YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY);
    public void onInitialize() {
        AlteningAccountStorage.getInstance().read();
    }

    public static IASAlteningMod getInstance() {
        return instance;
    }
}