package ru.offenderify.iasaltening.mixin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.SystemToast.Type;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

@Mixin(value = ClientLoginNetworkHandler.class)
public class MixinClientLoginNetworkHandler {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "joinServerSession", at = @At(value = "HEAD"), cancellable = true)
    private void onJoinServerSession(String serverId, CallbackInfoReturnable<Text> callback) {
        if (client.getSession().getAccessToken().startsWith("$IASALTENING$")) {
            try {
                JsonObject authDataJson = new JsonObject();
                authDataJson.addProperty("accessToken",
                        client.getSession().getAccessToken().replace("$IASALTENING$", ""));
                authDataJson.addProperty("selectedProfile", client.getSession().getUuid());
                authDataJson.addProperty("serverId", serverId);
                HttpURLConnection connection = (HttpURLConnection) new URL("http://sessionserver.thealtening.com/session/minecraft/join").openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("Content-Length", "" + new Gson().toJson(authDataJson).getBytes().length);
                connection.setRequestMethod("POST");
                OutputStream output = connection.getOutputStream();
                IOUtils.write(new Gson().toJson(authDataJson), output, "UTF-8");
                output.flush();
                output.close();
                if (connection.getResponseCode() < 200 && connection.getResponseCode() > 299) {
                    InputStream input = connection.getErrorStream();
                    String error = IOUtils.toString(input, "UTF-8");
                    input.close();
                    throw new IllegalStateException(error);
                }
            } catch (Exception exception) {
                callback.setReturnValue(new LiteralText("[IAS-Altening] Error logging in: ").append(exception.toString()));
                callback.cancel();
                return;
            }
            callback.setReturnValue(null);
            callback.cancel();
            client.execute(new Runnable(){
                @Override
                public void run() {
                    client.getToastManager().add(SystemToast.create(client, Type.TUTORIAL_HINT, new LiteralText("[IAS-Altening]"), new LiteralText("Alt is valid.")));
                }
            });
        }
    }
}
