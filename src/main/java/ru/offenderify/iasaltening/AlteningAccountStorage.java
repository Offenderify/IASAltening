package ru.offenderify.iasaltening;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.MinecraftClient;

public class AlteningAccountStorage {
    private static AlteningAccountStorage instance = new AlteningAccountStorage();
    private List<AlteningAccount> accounts = Lists.newArrayList();

    public List<AlteningAccount> getAccounts() {
        return accounts;
    }

    public static AlteningAccountStorage getInstance() {
        return instance;
    }

    public void read() {
        try {
            File altFile = new File(MinecraftClient.getInstance().runDirectory, "altening.ias");
            if (altFile.exists()) {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(altFile));
                int total = input.readShort();
                for (int i = 0; i < total; i++) {
                    accounts.add((AlteningAccount) input.readObject());
                }
                input.close();
            }
        } catch (IOException | ClassNotFoundException exception) {
            System.err.println("Error reading account");
            exception.printStackTrace();
        }
    }

    public void write() {
        try {
            File altFile = new File(MinecraftClient.getInstance().runDirectory, "altening.ias");
            if (!altFile.exists()) altFile.createNewFile();
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(altFile));
            output.writeShort(accounts.size());
            for (AlteningAccount account : accounts) {
                output.writeObject(account);
            }
            output.close();
        } catch (IOException exception) {
            System.err.println("Error writing account");
            exception.printStackTrace();
        }
    }
}
