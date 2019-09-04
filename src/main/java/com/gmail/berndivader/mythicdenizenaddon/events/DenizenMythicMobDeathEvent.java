package com.gmail.berndivader.mythicdenizenaddon.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;

public class DenizenMythicMobDeathEvent 
extends 
BukkitScriptEvent 
implements 
Listener
{
	public static DenizenMythicMobDeathEvent instance;
	public MythicMobDeathEvent e;
	
	public DenizenMythicMobDeathEvent() {
		instance=this;
	}

	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		String s1=s.toLowerCase();
		return s1.startsWith("mm denizen death")||s1.startsWith("mythicmobs death");
	}
	
	@Override
	public boolean matches(ScriptContainer container, String s) {
		return true;
	}

	@Override
	public String getName() {
		return "MythicMobsDeathEvent";
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
	}
	
    @Override
    public void destroy() {
    	MythicMobDeathEvent.getHandlerList().unregister(this);
    }
    
    @Override
    public ScriptEntryData getScriptEntryData() {
    	EntityTag killer=new EntityTag(e.getEntity());
    	return new BukkitScriptEntryData(killer.isPlayer()?killer.getDenizenPlayer():null,killer.isNPC()?killer.getDenizenNPC():null);
    }

	@Override
    public boolean applyDetermination(ScriptPath container,ObjectTag tag) {
		String determination=tag.toString();
		String[]c=determination.toLowerCase().split(";");
		for (int a=0;a<c.length;a++) {
			String[]parse=c[a].split(":");
			String d=parse[0].toLowerCase();
			String v=parse[1];
			switch(d) {
			case "drops":
				if (Argument.valueOf(v).matchesArgumentType(ListTag.class)) {
					List<ItemStack>is=new ArrayList<ItemStack>();
					for(ItemTag di:Argument.valueOf(v).asType(ListTag.class).filter(ItemTag.class)) {
						is.add(di.getItemStack());
					}
					e.setDrops(is);
				}
				break;
			case "money":
				if (Argument.valueOf(v).matchesPrimitive(PrimitiveType.Double)) e.setCurrency(Double.parseDouble(v));
				break;
			case "exp":
			case "xp":
				if (Argument.valueOf(v).matchesPrimitive(PrimitiveType.Integer)) e.setExp(Integer.parseInt(v));
				break;
			}
		}
		return true;
    }
	
	@Override
    public ObjectTag getContext(String name) {
		switch(name.toLowerCase()) {
		case "drops":
			ListTag dl=new ListTag();
			for (ItemStack i:e.getDrops()) {
				dl.add(new ItemTag(i).identify());
			}
			return dl;
		case "killer":
		case "attacker":
			return new EntityTag(e.getKiller());
		case "victim":
		case "entity":
			return new EntityTag(e.getEntity());
		case "activemob":
			return new dActiveMob(e.getMob());
		case "money":
			return new ElementTag(e.getCurrency());
		case "exp":
			return new ElementTag(e.getExp());
		case "event":
			return new ElementTag(this.e.toString());
		}
        return super.getContext(name);
    }
	
	@EventHandler
	public void onMythicMobsDeathEvent(MythicMobDeathEvent ev) {
		this.e=ev;
		fire();
	}
}
