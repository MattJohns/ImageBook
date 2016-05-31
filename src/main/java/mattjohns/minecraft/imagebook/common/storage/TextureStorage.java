package mattjohns.minecraft.imagebook.common.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.io.InputStream;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import mattjohns.common.math.*;
import mattjohns.common.storage.ImageStorage;
import mattjohns.common.storage.Path;
import mattjohns.common.storage.StorageException;
import mattjohns.minecraft.imagebook.Log;
import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocation;
import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocationList;

public class TextureStorage {
	public enum GameObjectType {
		BLOCK,
		ITEM,
		GUI
	}

	// must use forward slash, not backslash
	private static final String INTERNAL_FOLDER_SEPARATOR = "/"; 
	private static final String INTERNAL_BASE_FOLDER = "textures";
	private static final String INTERNAL_BLOCK_FOLDER = "blocks";
	private static final String INTERNAL_ITEM_FOLDER = "items";
	private static final String INTERNAL_GUI_FOLDER = "gui";

	private TextureManager textureManager;

	public TextureStorage(TextureManager textureManager) {
		this.textureManager = textureManager;
	}

	private static String internalResourceGetDirectory(GameObjectType textureType) {
		String folder;
		switch (textureType) {
			case BLOCK: folder = INTERNAL_BLOCK_FOLDER; break;
			case ITEM: folder = INTERNAL_ITEM_FOLDER; break;
			case GUI: folder = INTERNAL_GUI_FOLDER; break;

			default: throw new IllegalArgumentException();
		}
		
		return INTERNAL_BASE_FOLDER + INTERNAL_FOLDER_SEPARATOR + folder;
	}
	
	private static String internalResourceGetPath(GameObjectType textureType, String filename) {
		return internalResourceGetDirectory(textureType) + INTERNAL_FOLDER_SEPARATOR + filename;
	}

	private ResourceLocation internalResourceGetResourceLocation(String textureName, GameObjectType gameObjectType,
			String modNameInternal) {
		String internalPath = internalResourceGetPath(gameObjectType, textureName);

		return new ResourceLocation(modNameInternal, internalPath);
	}

	public Vector2I internalGetSize(ResourceLocation resourceLocation)
			throws StorageException {
		IResource resource;
		
		try
		{
			resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);
		}
		catch (Exception e) {
			throw new StorageException("Unable to get internal resource file \""
					+ resourceLocation.toString() + "\" .");
		}
		
		InputStream stream = resource.getInputStream();
		
		BufferedImage image;
		try {
			image = ImageStorage.copyFromStream(stream);
		}
		catch (IOException e) {
			throw new StorageException("Unable to read internal texture resource file \""
					+ resourceLocation.toString() + "\" .");
		}

		Vector2I size = sizeGet(image);

		return size;
	}

	private String gameObjectGetTexture(GameObjectType gameObjectType,
			ResourceLocation location, int objectMetadata)
			throws StorageException {
		switch (gameObjectType) {
			case GUI: throw new StorageException("Internal GUI texture copy not supported.");
			
			case ITEM: {
				Item item = (Item)GameRegistry.findItem(location.getResourceDomain(), location.getResourcePath());
				if (item == null)
					throw new StorageException("Internal item \"" + location.toString() + "\" not found.");
				
				IIcon icon = item.getIconFromDamage(objectMetadata);
				if (icon == null)
					throw new StorageException("Internal item \"" + location.toString() + "\" not found for metadata "
							+ Integer.toString(objectMetadata) + " .");
				
				String newPath = internalResourceGetPath(GameObjectType.ITEM, icon.getIconName()
						+ ".png");
				
				return newPath;
			}
			
			case BLOCK: throw new StorageException("Internal block texture copy not implemented.");
			default: throw new RuntimeException("Unsupported game object type.");
		}
	}
			
	
	public TextureResourceLocation internalGet(GameObjectType gameObjectType,
			String objectName,
			int objectMetadata,
			String modNameInternal,
			boolean isRepeat,
			Vector2F repeatFrequency)
			throws StorageException {
		ResourceLocation resourceLocation = new ResourceLocation(objectName);

		String newPath = gameObjectGetTexture(gameObjectType, resourceLocation, objectMetadata);
		ResourceLocation newResourceLocation = new ResourceLocation(newPath);
		
		return internalGet(newResourceLocation,
				modNameInternal,
				isRepeat,
				repeatFrequency);
	}
	
	public TextureResourceLocation internalResourceGet(String filename,
			GameObjectType gameObjectType,
			String modNameInternal,
			boolean isRepeat,
			Vector2F repeatFrequency)
			throws StorageException {
		ResourceLocation resourceLocation = internalResourceGetResourceLocation(filename,
				gameObjectType, modNameInternal);
		
		return internalGet(resourceLocation, modNameInternal, isRepeat, repeatFrequency);
	}

	public TextureResourceLocation internalGet(ResourceLocation resourceLocation,
			String modNameInternal,
			boolean isRepeat,
			Vector2F repeatFrequency)
			throws StorageException {
		Vector2I size;
		try {
			size = internalGetSize(resourceLocation);
		}
		catch (StorageException e) {
			throw new StorageException("Unable to get size of internal texture.  " + e.getMessage());
		}

		return new TextureResourceLocation(resourceLocation, size, isRepeat,
				repeatFrequency);
	}

	private static Vector2I sizeGet(BufferedImage item) throws StorageException{
		Vector2I size = new Vector2I(item.getWidth(), item.getHeight());

		if (size.xGet() <= 0 || size.yGet() <= 0) {
			throw new StorageException("Invalid size of " + size.toString()
					+ " for image.");
		}

		return size;
	}

	public TextureResourceLocation externalCreate(
			String relativePath,
			String minecraftDirectory,
			String modNameInternal,
			boolean isRepeat,
			Vector2F repeatFrequency)
			throws StorageException {
		// only relative paths allowed
		File file = new File(relativePath);
		if (file.isAbsolute()) {
			throw new StorageException(
					"Absolute paths are not allowed for images.  Please use a path relative to the minecraft folder.  Image path: \""
					+ relativePath + "\" .");
		}

		String path = Path.combine(minecraftDirectory, relativePath);

		// prevent access to files outside the minecraft folder tree
		try {
			if (!Path.checkIfSubDirectory(minecraftDirectory, path))
			{
				throw new StorageException("Invalid image path \""
						+ path
						+  "\" .  File must be located inside .minecraft folder or a sub folder.");
			}
		}
		catch (IOException e) {
			throw new StorageException("File system error while checking for sub folder.  Minecraft folder path \""
					+ minecraftDirectory
					+  "\" .  Image path \"" + path
					+ "\" .  " + e.getMessage());
		}

		// load it
		try {
			return externalCreate(file, modNameInternal, isRepeat, repeatFrequency);
		}
		catch (StorageException e) {
			throw new StorageException("Unable to load image at \""
					+ path
					+  "\" .  Please check the configuration file.  " + e.getMessage());
		}
	}

	private TextureResourceLocation externalCreate(
			File file,
			String modNameInternal,
			boolean isRepeat,
			Vector2F repeatFrequency)
			throws StorageException {
		BufferedImage image;
		try {
			image = ImageStorage.copyFromFile(file);
		}
		catch (IOException e) {
			throw new StorageException("Unable to load file.  " + e.getMessage());
		}

		ResourceLocation resourceLocation = externalCreateAsDynamic(image, modNameInternal);

		Vector2I size = sizeGet(image);

		return new TextureResourceLocation(resourceLocation, size, isRepeat, repeatFrequency);
	}

	private ResourceLocation externalCreateAsDynamic(
			BufferedImage image, String modNameInternal) {
		// entity name is minecraft:dynamic/[mod name]_[unique automatic id]
		DynamicTexture texture = new DynamicTexture(image);
		return textureManager.getDynamicTextureLocation(modNameInternal, texture);
	}
}
