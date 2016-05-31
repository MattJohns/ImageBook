package mattjohns.minecraft.imagebook.common.forge;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ForgeLoadEvent {
	public FMLInitializationEvent forgeEvent;
	
	public ForgeLoadEvent(FMLInitializationEvent forgeEvent) {
		this.forgeEvent = forgeEvent;
	}
}
