package uc.kircheplus.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Utils;

public class tabcompletion {
    private final List<String> lastArguments = new ArrayList<>();
    private String lastTyped = "";
    private String lastComplete = "";
    public static int spaces = 0;

    @Subscribe
    public void onKeyPressEvent(KeyEvent event) {
        try {
            if (Laby.references().chatAccessor().isChatOpen()) {
                if (event.state().equals(KeyEvent.State.PRESS)) {
                    if (event.key().equals(Key.TAB)) {
                        String typedCommand = Laby.references().chatExecutor().getChatInputMessage();
                        if (typedCommand == null || !typedCommand.startsWith("/")) return;
                        String prefix = typedCommand.replace("/", "").split(" ")[0];
                        if (!typedCommand.contains(" ")) return;
                        spaces = (int) typedCommand.chars().filter(c -> c == ' ').count();

                        for (Command cmd : KirchePlus.main.commands) {
                            if (prefix.equalsIgnoreCase(cmd.getPrefix()) || Arrays.stream(cmd.getAliases()).anyMatch(name -> name.equalsIgnoreCase(prefix))) {
                                String[] arguments = Arrays.copyOfRange(typedCommand.split(" "), 1, typedCommand.split(" ").length);
                                String[] arguments2 = Arrays.copyOfRange(typedCommand.split(" "), 1, typedCommand.split(" ").length);

                                String[] withoutLast;
                                if (typedCommand.endsWith(" ")) {
                                    withoutLast = Arrays.copyOfRange(arguments, 0, arguments.length);
                                } else {
                                    withoutLast = Arrays.copyOfRange(arguments, 0, arguments.length - 1);
                                }
                                String lastTypedArg = "";

                                List<String> commandArgs;
                                if (lastTyped.length() != 0) {
                                    if (lastArguments.size() != 0 && arguments2.length != 0) {
                                        lastTypedArg = arguments2[arguments2.length - 1];
                                        if (lastArguments.contains(lastTypedArg)) {
                                            arguments2 = Utils.removeStringFromArray(arguments2, lastTypedArg);
                                        }
                                    }
                                    commandArgs = cmd.complete(Utils.addStringToArray(arguments2, lastTyped));

                                } else {
                                    if (lastArguments.size() != 0 && arguments.length != 0) {
                                        lastTypedArg = arguments[arguments.length - 1];
                                        if (lastArguments.contains(lastTypedArg)) {
                                            arguments = Utils.removeStringFromArray(arguments, lastTypedArg);
                                        }
                                    }
                                    commandArgs = cmd.complete(arguments);
                                }

                                if (lastTyped.length() == 0 && arguments2.length != 0) {
                                    if (!typedCommand.endsWith(" ")) {
                                        if (lastComplete.length() == 0) {
                                            lastTyped = arguments2[arguments2.length - 1];
                                        }
                                    }
                                }

                                if (commandArgs.size() == 1) {
                                    String guessArg = commandArgs.get(0);
                                    Laby.references().chatExecutor().suggestCommand("/" + prefix + " " + Utils.buildString(withoutLast) + guessArg.replaceAll("§[0-9A-FK-ORa-fk-or]", ""));
                                    sendGuesses(commandArgs, guessArg);
                                } else {
                                    for (String arg : commandArgs) {
                                        if (lastArguments.size() == commandArgs.size()) {
                                            lastArguments.clear();
                                        }
                                        if (!lastArguments.contains(arg)) {
                                            Laby.references().chatExecutor().suggestCommand("/" + prefix + " " + Utils.buildString(withoutLast) + arg.replaceAll("§[0-9A-FK-ORa-fk-or]", ""));
                                            sendGuesses(commandArgs, arg);
                                            lastArguments.add(arg);
                                            lastComplete = arg;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        lastArguments.clear();
                        spaces = 0;
                        lastTyped = "";
                        lastComplete = "";
                    }
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private void sendGuesses(List<String> commandArgs, String guess) {
        if (commandArgs.size() == 0) return;
        StringBuilder builder = new StringBuilder();
        int count = 0;
        while (count < commandArgs.size() - 1) {
            if (commandArgs.get(count).equals(guess)) {
                builder.append("§c").append(commandArgs.get(count)).append("§7, ");
            } else {
                builder.append("§7").append(commandArgs.get(count)).append("§7, ");
            }
            count++;
        }
        if (commandArgs.get(count).equals(guess)) {
            builder.append("§c").append(commandArgs.get(count));
        } else {
            builder.append("§7").append(commandArgs.get(count));
        }
        KirchePlus.main.utils.printChatMessageWithOptionalDeletion(builder.toString());
    }
}