package uc.kircheplus.events;

import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import uc.kircheplus.KirchePlus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author RettichLP
 */
public class TabCompletionListener {

    private int lastSuggestedCommandPrefix = 0;
    private String lastSuggestedCommandPrefixStartString = null;
    private int lastSuggestedCommandParameter = 0;
    private String lastSuggestedCommandParameterStartString = null;
    @Subscribe
    public void onKey(KeyEvent e) {
        if (Laby.references().chatAccessor().isChatOpen() && e.state().equals(KeyEvent.State.PRESS)) {
            if (e.key().equals(Key.TAB)) {
                autoComplete();
            } else {
                this.lastSuggestedCommandPrefix = 0;
                this.lastSuggestedCommandPrefixStartString = null;
                this.lastSuggestedCommandParameter = 0;
                this.lastSuggestedCommandParameterStartString = null;
            }
        }
    }



    public void autoComplete() {
        String chatInputMessage = Laby.references().chatExecutor().getChatInputMessage();
        if (chatInputMessage != null && chatInputMessage.startsWith("/")) {
            String withoutSlash = chatInputMessage.substring(1);
            String[] arguments = withoutSlash.split(" ");
            if (arguments.length == 1 && !withoutSlash.endsWith(" ")) {
                if (this.lastSuggestedCommandPrefixStartString == null)
                    this.lastSuggestedCommandPrefixStartString = arguments[0];
                List<String> commandPrefixAndAliasesList = new ArrayList<>();
                KirchePlus.main.commands.forEach(command -> {
                    commandPrefixAndAliasesList.add(command.getPrefix());
                    commandPrefixAndAliasesList.addAll(Arrays.asList(command.getAliases()));
                });
                commandPrefixAndAliasesList.removeIf(s -> !s.startsWith(this.lastSuggestedCommandPrefixStartString));
                if (commandPrefixAndAliasesList.isEmpty())
                    return;
                if (commandPrefixAndAliasesList.size() <= this.lastSuggestedCommandPrefix)
                    this.lastSuggestedCommandPrefix = 0;
                String suggestedString = commandPrefixAndAliasesList.get(this.lastSuggestedCommandPrefix);
                this.lastSuggestedCommandPrefix++;
                Laby.references().chatExecutor().suggestCommand("/" + suggestedString);
            } else {
                if (this.lastSuggestedCommandParameterStartString == null)
                    this.lastSuggestedCommandParameterStartString = arguments[arguments.length - 1];
                Command command = KirchePlus.main.commands.stream().filter(c -> (c.getPrefix().equalsIgnoreCase(arguments[0]) || Arrays.<String>asList(c.getAliases()).contains(arguments[0].toLowerCase()))).findFirst().orElse(null);
                if (command == null)
                    return;
                String[] argumentsWithoutLast = Arrays.<String>copyOfRange(arguments, 0, arguments.length - 1);
                List<String> argumentsWithModifiedLast = new ArrayList<>(List.of(argumentsWithoutLast));
                argumentsWithModifiedLast.add(this.lastSuggestedCommandParameterStartString);
                List<String> commandParameterList = command.complete(argumentsWithModifiedLast.<String>toArray(new String[0]));
                commandParameterList.removeIf(s -> !s.toLowerCase().startsWith(this.lastSuggestedCommandParameterStartString.toLowerCase()));
                if (commandParameterList.isEmpty())
                    return;
                if (commandParameterList.size() <= this.lastSuggestedCommandParameter)
                    this.lastSuggestedCommandParameter = 0;
                String suggestedString = commandParameterList.get(this.lastSuggestedCommandParameter);
                this.lastSuggestedCommandParameter++;
                Laby.references().chatExecutor().suggestCommand("/" + makeStringByArgs((Object[])argumentsWithoutLast, " ") + " " + suggestedString);
            }
        }
    }

    public String makeStringByArgs(Object[] args, String space) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : args) {
            stringBuilder.append(o).append(space);
        }
        return stringBuilder.substring(0, stringBuilder.length() - space.length());
    }
}
