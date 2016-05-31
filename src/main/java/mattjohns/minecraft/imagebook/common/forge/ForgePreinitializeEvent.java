package mattjohns.minecraft.imagebook.common.forge;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ForgePreinitializeEvent {
	public FMLPreInitializationEvent forgeEvent;
	
	public ForgePreinitializeEvent(FMLPreInitializationEvent forgeEvent) {
		this.forgeEvent = forgeEvent;
	}
}

