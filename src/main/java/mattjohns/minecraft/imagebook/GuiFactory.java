package mattjohns.minecraft.imagebook;

import net.minecraft.client.gui.GuiScreen;

public interface GuiFactory {
	GuiScreen guiCreateClient(int id);
	Object guiCreateServer(int id);
}
