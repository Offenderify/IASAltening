package ru.offenderify.iasaltening;

import java.io.Serializable;

import com.google.gson.Gson;

import ru.vidtu.iasfork.msauth.Account;

public interface AlteningAccount extends Account, Serializable {
    public static final Gson GSON = new Gson();
    @Override
    public default String alias() {
        return "TheAltening account";
    }
}
