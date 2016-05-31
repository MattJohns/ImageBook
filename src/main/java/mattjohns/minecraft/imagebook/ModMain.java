package mattjohns.minecraft.imagebook;

import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import mattjohns.minecraft.imagebook.common.event.EventSend;
import mattjohns.minecraft.imagebook.common.forge.ForgeApi;
import mattjohns.minecraft.imagebook.common.forge.ForgeInitializeEvent;
import mattjohns.minecraft.imagebook.common.forge.ForgeLoadEvent;
import mattjohns.minecraft.imagebook.common.forge.ForgePreinitializeEvent;

@Mod(modid = CentralController.NAME_INTERNAL, 
		name = CentralController.NAME_DISPLAY,
		version = CentralController.VERSION_PUBLIC)
public class ModMain {
	public EventSend<FMLPreInitializationEvent> eventPreintialize = new EventSend<FMLPreInitializationEvent>();
	public EventSend<FMLInitializationEvent> eventInitialize = new EventSend<FMLInitializationEvent>();
	public EventSend<FMLInitializationEvent> eventLoad = new EventSend<FMLInitializationEvent>();

	// filled with instance by forge
    @Instance(CentralController.NAME_INTERNAL)
    public static ModMain instance;

    // guiHandler is replaced with either client or combined client & server object via reflection
    @SidedProxy(clientSide = CentralController.PACKAGE_NAME + ".ClientGuiHandler",
			serverSide = CentralController.PACKAGE_NAME + ".ClientServerGuiHandler")
    public static ClientServerGuiHandler guiHandler;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    	CentralController centralController = new CentralController(this);

		eventPreintialize.send(event);
    }

	@EventHandler
    public void init(FMLInitializationEvent event) {
		eventInitialize.send(event);
}

	@EventHandler
    public void load(FMLInitializationEvent event) {
		eventLoad.send(event);
    }
}
