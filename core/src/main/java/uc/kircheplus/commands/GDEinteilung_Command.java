package uc.kircheplus.commands;

import net.labymod.api.client.chat.command.Command;
import uc.kircheplus.automaticactivity.Handler;

public class GDEinteilung_Command extends Command {

    public GDEinteilung_Command() {
        super("gdeinteilung", "einteilung", "gd", "gde");
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        Handler.GDGUI = true;
        return true;
    }
}
