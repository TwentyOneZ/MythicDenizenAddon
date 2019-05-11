package com.gmail.berndivader.mythicdenizenaddon.conditions;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import net.aufdemrand.denizen.objects.dLocation;

public
class 
LocationScriptCondition 
extends
ScriptCondition<dLocation>
implements
ILocationCondition
{
	public LocationScriptCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
	}

	@Override
	public boolean check(AbstractLocation location) {
		return __check(new dLocation(BukkitAdapter.adapt(location)));
	}

}
