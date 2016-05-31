package mattjohns.minecraft.imagebook.common.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.IItemRenderer;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;

import java.io.File;
import java.util.List;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import mattjohns.common.math.Vector2F;
import mattjohns.common.storage.Path;
import mattjohns.common.storage.StorageException;
import mattjohns.minecraft.imagebook.BookItem;
import mattjohns.minecraft.imagebook.CentralController;
import mattjohns.minecraft.imagebook.Log;
import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocation;

public class ForgeApi {
	private Minecraft minecraft;
	private Object mod;
	
	public ForgeApi(Minecraft minecraft) {
		this.minecraft = minecraft;
	}
	
	public Object modGet() {
		return mod;
	}

	public Minecraft minecraftGet() {
		return minecraft;
	}

	public Vector2F screenGetSizeMinecraftScale() {
		ScaledResolution scaledResolution = new ScaledResolution(
				minecraft,
				minecraft.displayWidth,
				minecraft.displayHeight);

		return new Vector2F(
				(float)scaledResolution.getScaledWidth(),
				(float)scaledResolution.getScaledHeight());
	}
	
	public TextureManager textureManagerGet() {
		return minecraft.getTextureManager();
	}

	///// allow using an existing item id
	@SuppressWarnings( "deprecation" )
	public static void itemRegister(Item item,
			String nameInternal,
			String nameDisplay,
			IItemRenderer itemRenderer) {
		
		// The Forge 1.7.10 source says addName() is deprecated and to use
		// internationalization files instead.  The problem is this display name needs
		// to be added dynamically from the user's runtime configuration, so you can't use
		// static text from a language file.
		LanguageRegistry.addName(item, nameDisplay);

		GameRegistry.registerItem(item, nameInternal);

		MinecraftForgeClient.registerItemRenderer(item, itemRenderer);
	}
	
	public String minecraftDirectoryGet() {
		return minecraft.mcDataDir.getAbsolutePath();
	}
	
	public void modRegister(Object mod) {
		this.mod = mod;
        MinecraftForge.EVENT_BUS.register(mod);
	}

	public void guiHandlerRegister(IGuiHandler item, Object mod) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod, item);
	}
	
	public void guiClose() {
		minecraft.displayGuiScreen((GuiScreen)null);
	}
	
	public void guiOpen(int id, World world, EntityPlayer player) {
		player.openGui(mod, id, world,
				(int)player.posX, (int)player.posY, (int)player.posZ);
	}
	
	public void resourcePackAdd(String filename)
			throws StorageException {
    	List<IResourcePack> defaultResourcePackList = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class,
    			minecraft, "defaultResourcePacks", "field_110449_ao");
    	
    	String path = Path.combine(minecraftDirectoryGet(), "resourcepacks", filename);

		File file = new File(path);
		FileResourcePack pack = new FileResourcePack(file); 
		defaultResourcePackList.add(pack);

    	minecraft.refreshResources();
	}
}
