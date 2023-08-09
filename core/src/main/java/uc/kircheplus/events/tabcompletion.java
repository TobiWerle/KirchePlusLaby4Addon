package uc.kircheplus.events;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.commands.CommandBypass;
import uc.kircheplus.commands.hv_Command;
import uc.kircheplus.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class tabcompletion {
    private List<String> lastargs = new ArrayList<>();
    private String lastTyped = "";
    private String lastComplete = "";
    public static Integer spaces = 0;
    @Subscribe
    public void onKeyPressEvent(KeyEvent e) {
        try {
            if(Laby.references().chatAccessor().isChatOpen()){
                if(e.state().equals(KeyEvent.State.PRESS)){
                    if (e.key().equals(Key.TAB)) {
                        String typedCommand = Laby.references().chatExecutor().getChatInputMessage();
                        if(typedCommand == null || !typedCommand.startsWith("/")) return;
                        String prefix = typedCommand.replace("/","").split(" ")[0];
                        if(!typedCommand.contains(" ")) return;
                        spaces = (int)typedCommand.chars().filter(c -> c == (int) ' ').count();
                        for(Command cmd : KirchePlus.main.commands){
                            if(prefix.equalsIgnoreCase(cmd.getPrefix()) || Arrays.stream(cmd.getAliases()).anyMatch(name -> name.equalsIgnoreCase(prefix))){
                                String[] arguments = Arrays.copyOfRange(typedCommand.split(" "), 1, typedCommand.split(" ").length);
                                String[] arguments2 = Arrays.copyOfRange(typedCommand.split(" "), 1, typedCommand.split(" ").length);

                                System.out.println("Arguments : " +Utils.buildString(arguments));
                                String[] withoutlast = new String[]{};
                                if(typedCommand.endsWith(" ")){
                                    withoutlast = Arrays.copyOfRange(arguments, 0, arguments.length);
                                    System.out.println("Debug withoutlast: "+Utils.buildString(withoutlast));
                                }else{
                                    withoutlast = Arrays.copyOfRange(arguments, 0, arguments.length-1);
                                    System.out.println("Debug withoutlast: "+Utils.buildString(withoutlast));
                                }
                                String lastTypedArg = "";


                                List<String> commandArgs;
                                if(lastTyped.length() != 0){
                                    System.out.println("Debug 1");
                                    if(lastargs.size() != 0 && arguments2.length != 0){
                                        System.out.println("Debug 2");
                                        lastTypedArg = arguments2[arguments2.length-1];
                                        if(lastargs.contains(lastTypedArg)) {
                                            System.out.println("Debug 3");
                                            arguments2 = Utils.removeStringFromArray(arguments2, lastTypedArg);
                                        }
                                    }
                                    commandArgs = cmd.complete(Utils.addStringToArray(arguments2, lastTyped));

                                }else{
                                    if(lastargs.size() != 0 && arguments.length != 0){
                                        lastTypedArg = arguments[arguments.length-1];
                                        if(lastargs.contains(lastTypedArg)) {
                                            arguments = Utils.removeStringFromArray(arguments, lastTypedArg);
                                        }

                                    }
                                    commandArgs = cmd.complete(arguments);
                                }


                                if(lastTyped.length() == 0 && arguments2.length != 0){
                                    if(!typedCommand.endsWith(" ")) {
                                        if(lastComplete.length() == 0) {
                                            lastTyped = arguments2[arguments2.length - 1];
                                            System.out.println("Last typed: " + lastTyped);
                                        }
                                    }
                                }

                                if(commandArgs.size() == 1){
                                    String guessArg = commandArgs.get(0);
                                    Laby.references().chatExecutor().suggestCommand("/"+prefix + " " + Utils.buildString(withoutlast) + guessArg);
                                    sendGuesses(commandArgs, guessArg);
                                }else{
                                    for(String arg : commandArgs){
                                        if(lastargs.size() == commandArgs.size()){
                                            lastargs.clear();
                                        }
                                        if(!lastargs.contains(arg)){
                                            Laby.references().chatExecutor().suggestCommand("/"+prefix + " " + Utils.buildString(withoutlast) + arg);
                                            sendGuesses(commandArgs, arg);
                                            lastargs.add(arg);
                                            lastComplete = arg;
                                            break;
                                        }
                                    }

                                }

                            }
                        }
                    }else{
                        lastargs.clear();
                        spaces = 0;
                        lastTyped = "";
                        lastComplete = "";
                    }
                }
            }
        }catch (Exception error){
            error.printStackTrace();
        }
    }


    private void sendGuesses(List<String> commandArgs, String guess){
        if(commandArgs.size() == 0) return;
        StringBuilder builder = new StringBuilder();
        int count = 0;
        while(count < commandArgs.size()-1){
            if(commandArgs.get(count).equals(guess)){
                builder.append("§a").append(commandArgs.get(count)).append("§7, ");
                count++;
            }else{
                builder.append("§7").append(commandArgs.get(count)).append("§7, ");
                count++;
            }
        }
        if(commandArgs.get(count).equals(guess)){
            builder.append("§a").append(commandArgs.get(count));
        }else{
            builder.append("§7").append(commandArgs.get(count));
        }
        KirchePlus.main.utils.printChatMessageWithOptionalDeletion(builder.toString());
    }

}
