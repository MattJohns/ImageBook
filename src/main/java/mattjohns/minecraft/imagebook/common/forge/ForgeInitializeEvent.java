package mattjohns.minecraft.imagebook.common.forge;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ForgeInitializeEvent {
	public FMLInitializationEvent forgeEvent;
	
	public ForgeInitializeEvent(FMLInitializationEvent forgeEvent) {
		this.forgeEvent = forgeEvent;
	}
}
