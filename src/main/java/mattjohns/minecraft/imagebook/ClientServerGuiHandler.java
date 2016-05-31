package mattjohns.minecraft.imagebook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class ClientServerGuiHandler implements IGuiHandler {
	protected GuiFactory guiFactory; 

	public void guiFactorySet(GuiFactory guiFactory) {
		this.guiFactory = guiFactory;
	}
	
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	return guiFactory.guiCreateServer(id);
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	return guiFactory.guiCreateClient(id);
    }
}