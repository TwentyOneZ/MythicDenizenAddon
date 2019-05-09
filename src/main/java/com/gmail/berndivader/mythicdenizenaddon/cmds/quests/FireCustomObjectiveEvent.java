package com.gmail.berndivader.mythicdenizenaddon.cmds.quests;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicdenizenaddon.Utils;
import com.gmail.berndivader.mythicdenizenaddon.events.CustomObjectiveEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.CustomObjectiveEvent.Action;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public 
class
FireCustomObjectiveEvent 
extends
AbstractCommand
{
	
	static String str_action="action",str_player="player";
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if(!entry.hasObject(str_action)&&arg.matchesPrefix(str_action)) {
				entry.addObject(str_action,arg.asElement());
			} else if(entry.hasObject(str_player)&&arg.matchesPrefix(str_player)&&arg.matchesArgumentType(dPlayer.class)) {
				entry.addObject(str_player,arg.asType(dPlayer.class));
			}
		}
		
		if(!entry.hasObject(str_action)) entry.addObject(str_action,new Element(Action.COMPLETE.toString()));
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		
		if(entry.hasObject(str_player)) {
			Action action=Utils.enum_lookup(Action.class,entry.getElement(str_action).asString().toUpperCase());
			Player player=((dPlayer)entry.getObject(str_player)).getPlayerEntity();
			
			CustomObjectiveEvent event=new CustomObjectiveEvent(player,action);
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

}
