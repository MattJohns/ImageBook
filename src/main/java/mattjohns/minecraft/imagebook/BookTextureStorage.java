package mattjohns.minecraft.imagebook;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import mattjohns.common.math.Vector2F;
import mattjohns.common.storage.StorageException;
import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocation;
import mattjohns.minecraft.imagebook.common.storage.TextureStorage;

public class BookTextureStorage extends TextureStorage {
	private BookTextureList bookList = new BookTextureList();

	public BookTextureStorage(TextureManager textureManager) {
		super(textureManager);
	}

	public BookTextureList bookListGet() {
		return bookList;
	}

	public void textureCreate(ConfigurationBook configuration, 
			String minecraftFolderPath,
			String modNameInternal)
			throws StorageException {
		BookTexture bookTextureResourceLocation
				= new BookTexture(configuration.nameInternalGet());

		TextureResourceLocation textureResourceLocation;

		// icon
		try
		{
			textureResourceLocation = textureCreate(configuration.iconGet(),
					minecraftFolderPath, modNameInternal);
		}
		catch (StorageException e) {
			throw new StorageException("Unable to load item icon for book \"" + configuration.nameInternalGet()
					+ "\" .  " + e.getMessage());
		}
		 
		bookTextureResourceLocation.itemIconSet(textureResourceLocation);

		// book container
		try
		{
			textureResourceLocation = textureCreate(configuration.bookContainerImageGet(),
					minecraftFolderPath, modNameInternal);
		}
		catch (StorageException e) {
			throw new StorageException("Unable to load container image for book \"" + configuration.nameInternalGet()
					+ "\" .  " + e.getMessage());
		}

		bookTextureResourceLocation.bookContainerSet(textureResourceLocation);

		// pages
		for (int pageIndex = 0; pageIndex < configuration.pageImageListGet().length; pageIndex++) {
			try
			{
				textureResourceLocation = textureCreate(configuration.pageImageListGet()[pageIndex],
						minecraftFolderPath, modNameInternal);
			}
			catch (StorageException e) {
				throw new StorageException("Unable to load page at index "
						+ Integer.toString(pageIndex) + " for book \"" + configuration.nameInternalGet()
						+ "\" .  " + e.getMessage());
			}

			bookTextureResourceLocation.pageAdd(textureResourceLocation);
		}
		

		bookList.add(bookTextureResourceLocation);
	}
 
	private TextureResourceLocation textureCreate(ConfigurationTexture configurationTexture,
			String minecraftDirectory,
			String modNameInternal)
			throws StorageException {

		TextureResourceLocation returnValue;

		switch (configurationTexture.imageTypeGet()) {
			// an image file in a sub directory of the minecraft directory 
			case EXTERNAL_FILE: {

				try {
					return externalCreate(configurationTexture.externalPathRelativeGet(),
							minecraftDirectory, modNameInternal,
							configurationTexture.isRepeatGet(),
							new Vector2F(configurationTexture.repeatRatioXGet(),
									configurationTexture.repeatRatioYGet()));
				}
				catch (StorageException e) {
					throw new StorageException("Unable to create external texture.  " + e.getMessage());
				}
			}

			// an image that is part of vanilla minecraft
			case INTERNAL: {
				try
				{
					return internalGet(configurationTexture.internalGameObjectTypeGet(),
							configurationTexture.internalFilenameGet(),
							configurationTexture.internalMetadataGet(),
							modNameInternal,
							configurationTexture.isRepeatGet(),
							new Vector2F(configurationTexture.repeatRatioXGet(),
									configurationTexture.repeatRatioYGet()));
				}
				catch (StorageException e) {
					throw new StorageException("Unable to create internal texture.  " + e.getMessage());
				}
			}

			// an image that is packaged as part of this mod
			case IMAGEBOOK_RESOURCE: {
				try
				{
					return internalResourceGet(configurationTexture.internalFilenameGet(),
							TextureStorage.GameObjectType.GUI,
							modNameInternal,
							configurationTexture.isRepeatGet(),
							new Vector2F(configurationTexture.repeatRatioXGet(),
									configurationTexture.repeatRatioYGet()));
				}
				catch (StorageException e) {
					throw new StorageException("Unable to create internal texture.  " + e.getMessage());
				}
			}
			
			// an image from a resource pack
			case RESOURCE_PACK: {
				try
				{
					return internalResourceGet(
							configurationTexture.internalFilenameGet(),
							TextureStorage.GameObjectType.GUI,
							configurationTexture.packIdGet(),
							configurationTexture.isRepeatGet(),
							new Vector2F(configurationTexture.repeatRatioXGet(),
									configurationTexture.repeatRatioYGet()));
				}
				catch (StorageException e) {
					throw new StorageException("Unable to create resource pack texture.  " + e.getMessage());
				}
			}
			
			case SOLID_COLOR:
			default: {
				throw new StorageException("Not implemented.");
			}
		}
	}
}
