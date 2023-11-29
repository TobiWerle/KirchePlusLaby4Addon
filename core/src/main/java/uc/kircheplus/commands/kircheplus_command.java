package uc.kircheplus.commands;

import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.KirchePlus;

public class kircheplus_command extends Command {

    public kircheplus_command() {
        super("kircheplus");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if (!CommandBypass.bypass)
            return true;
        CommandBypass.bypass = false;
        KirchePlus.main.utils.sendCommandUsage();
        return true;
    }
}
