package ru.offenderify.iasaltening;

import com.google.gson.JsonObject;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import ru.vidtu.iasfork.mixins.MinecraftClientAccessor;
import ru.vidtu.iasfork.msauth.Request;

public class AlteningFreeAccount implements AlteningAccount {
    private String accessToken;
    private String id;
    private String name;
    public AlteningFreeAccount(String freeToken) throws Exception {
        JsonObject authJson = new JsonObject();
        authJson.addProperty("username", freeToken);
        JsonObject json = GSON.fromJson(new Request("http://authserver.thealtening.com/authenticate").post(GSON.toJson(authJson)).body(), JsonObject.class);
        if (json.has("error")) throw new IllegalStateException(json.has("errorMessage")?json.get("errorMessage").getAsString():json.get("error").getAsString());
        JsonObject selectedProfile = json.getAsJsonObject("selectedProfile");
        accessToken = json.get("accessToken").getAsString();
        id = selectedProfile.get("id").getAsString();
        name = selectedProfile.get("name").getAsString();
    }

    @Override
    public String alias() {
        return name;
    }

    @Override
    public Throwable login() {
        try {
            JsonObject validateJson = new JsonObject();
            validateJson.addProperty("accessToken", accessToken);
            int i = new Request("http://authserver.thealtening.com/validate").post(GSON.toJson(validateJson)).response();
            if (i < 200 || i > 299) throw new IllegalStateException("Not validated (Probably expired): " + i);
            ((MinecraftClientAccessor)MinecraftClient.getInstance()).setSession(new Session(name, id, "$IASALTENING$" + accessToken, "mojang"));
        } catch (Exception exception) {
            return exception;
        }
        return null;
    }
    
}
