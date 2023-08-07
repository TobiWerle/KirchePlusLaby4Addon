package uc.kircheplus.events;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import uc.kircheplus.KirchePlus;
import uc.kircheplus.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class tabcompletion {
    private List<String> lastargs = new ArrayList<>();

    @Subscribe
    public void onKeyPressEvent(KeyEvent e) {
        try {
            if(Laby.references().chatAccessor().isChatOpen()){
                if(e.state().equals(KeyEvent.State.PRESS)){
                    if (e.key().equals(Key.TAB)) {
                        String typedCommand = Laby.references().chatExecutor().getChatInputMessage();
                        if(typedCommand == null || !typedCommand.startsWith("/")) return;
                        String prefix = typedCommand.replace("/","").split(" ")[0];

                        for(Command cmd : KirchePlus.main.commands){
                            if(prefix.equalsIgnoreCase(cmd.getPrefix()) || Arrays.stream(cmd.getAliases()).anyMatch(name -> name.equalsIgnoreCase(prefix))){
                                String[] arguments = Arrays.copyOfRange(typedCommand.split(" "), 1, typedCommand.split(" ").length);
                                String output = "";
                                for(String args : arguments){
                                    output = output + " " +args;
                                }
                                System.out.println("Debug1:" + output);

                                if(lastargs.size() != 0 && arguments.length != 0){
                                    String lastTypedArg = arguments[arguments.length-1];
                                    if(lastargs.contains(lastTypedArg)) {
                                        arguments = Utils.removeStringFromArray(arguments, lastTypedArg);
                                    }
                                }
                                String[] withoutlast = new String[]{};
                                if(arguments.length != 0){
                                    withoutlast = Arrays.copyOfRange(arguments, 0, arguments.length - 1);
                                }
                                List<String> commandArgs = cmd.complete(arguments);

                                if(commandArgs.size() == 1){
                                    String guessArg = commandArgs.get(0);
                                    Laby.references().chatExecutor().suggestCommand("/"+prefix + Utils.buildString(withoutlast) +" "+guessArg);
                                }else{
                                    for(String arg : commandArgs){
                                        if(lastargs.size() == commandArgs.size()){
                                            lastargs.clear();
                                        }
                                        if(!lastargs.contains(arg)){
                                            Laby.references().chatExecutor().suggestCommand("/"+prefix + Utils.buildString(withoutlast) + " "+arg);
                                            lastargs.add(arg);
                                            break;
                                        }
                                    }

                                }

                            }
                        }
                    }else{
                        lastargs.clear();
                    }
                }
            }
        }catch (Exception error){
            error.printStackTrace();
        }
    }

}
