package mattjohns.minecraft.imagebook;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

public class Log {
	private String modNameDisplay;
	
	public Log(String modNameDisplay) {
		this.modNameDisplay = modNameDisplay; 
	}
	
	public void Add(String item) {
		AddInformation(item);
	}

	public void AddInformation(String item) {
		Add(Level.INFO, item);
	}

	public void AddWarning(String item) {
		Add(Level.WARN, item);
	}

	public void AddError(String item) {
		Add(Level.ERROR, item);
	}

	public void Add(Level level, String item) {
        FMLLog.log(modNameDisplay, level, item);
	}

	public void AddConsoleErrorGeneral() {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("error.load"));
	}
}