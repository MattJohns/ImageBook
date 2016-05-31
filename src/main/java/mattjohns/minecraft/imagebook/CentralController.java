package mattjohns.minecraft.imagebook;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import mattjohns.common.storage.Path;
import mattjohns.common.storage.StorageException;
import mattjohns.minecraft.imagebook.common.event.EventReceive;
import mattjohns.minecraft.imagebook.common.forge.ForgeApi;
import mattjohns.minecraft.imagebook.common.forge.ItemRenderer;
import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocation;
import mattjohns.minecraft.imagebook.common.video.Render2d;

public class CentralController implements GuiFactory {
	public static final String NAME_INTERNAL = "imagebook";
	public static final String NAME_DISPLAY = "ImageBook";

	public static final String VERSION_PUBLIC = "0.1";
	public static final String VERSION_TEST_CYCLE = "0";
	public static final String VERSION_PRIVATE = VERSION_PUBLIC + "." + VERSION_TEST_CYCLE;

	public static final String PACKAGE_NAME = "mattjohns.minecraft.imagebook";

	public class GuiId {
		// only has to be unique within this mod
		public static final int BOOK = 1;
	}

	private ModMain mod;
	private ForgeApi forgeApi;
	private Log log;

	private ConfigurationStorage configurationStorage;
	private Configuration configuration;

	private BookTextureStorage bookTextureStorage;
	private String openBookName;

	private Render2d render2d;
	private ItemRenderer bookItemRenderer;
	private boolean isLoadError = false;

	public BookTextureStorage bookTextureStorageGet() {
		return bookTextureStorage;
	}
	public boolean isLoadErrorGet() {
		return isLoadError;
	}
 
	public String openBookNameGet() {
		return openBookName;
	}

	public void openBookNameSet(String item) {
		openBookName = item;
	}

	public Render2d render2dGet() {
		return render2d;
	}

	public ForgeApi forgeApiGet() {
		return forgeApi;
	}

	public Configuration configurationGet() {
		return configuration;
	}

	public CentralController(ModMain mod) {
		this.mod = mod;

		log = new Log(NAME_INTERNAL);
		
		forgeApi = new ForgeApi(Minecraft.getMinecraft());

		// attach event

		// preinitialize
		this.mod.eventPreintialize.add(new EventReceive<FMLPreInitializationEvent>() {
			public void receive(FMLPreInitializationEvent item) {
				forgePreinitalize(item.getModConfigurationDirectory().getAbsolutePath());
			}
		});

		// initialize
		this.mod.eventInitialize.add(new EventReceive<FMLInitializationEvent>() {
			public void receive(FMLInitializationEvent item) {
				forgeInitialize();
			}
		});

		// load
		this.mod.eventLoad.add(new EventReceive<FMLInitializationEvent>() {
			public void receive(FMLInitializationEvent item) {
				forgeLoad();
			}
		});
	}

	public void forgePreinitalize(String configurationBaseFolderPath) {
		forgeApi.modRegister(mod);

		configurationStorage = new ConfigurationStorage(configurationBaseFolderPath);
	}

	public void forgeInitialize() {
		if (!configurationCopyFromStorage())
			return;

		guiHandlerCreate();
		render2d = new Render2d(forgeApi);
		bookTextureStorage = new BookTextureStorage(forgeApi.textureManagerGet());

		if (!loadFromConfiguration())
			return;
	}

	public void forgeLoad() {
		log.Add("Initalized");
	}

	private boolean configurationCopyFromStorage() {
		// create file with defaults
		try {
			configurationStorage.CreateIfNecessary();
		} catch (ConfigurationException e) {
			log.AddError("Unable to create default configuration file.  " + e.getMessage());
			return false;
		}

		// copy from file
		try {
			configuration = configurationStorage.CopyFrom();
		} catch (ConfigurationException e) {
			log.AddError("Unable to load configuration.  " + e.getMessage());
			return false;
		}
		
		// create any textures etc. for configuration items that are missing in the file 
		try {
			 configuration.setImpliedDefault();
		} catch (ConfigurationException e) {
			log.AddError("Invalid configuration.  " + e.getMessage());
			return false;
		}

		// validate
		try {
			 configuration.validate();
		} catch (ConfigurationException e) {
			log.AddError("Invalid configuration.  " + e.getMessage());
			return false;
		}
		
		return true;
	}

	private boolean loadFromConfiguration() {
		if (!resourcePackAdd(configuration.resourcePackListGet()))
			return false;

		if (!bookCreate(configuration.bookListGet()))
			return false;
		
		return true;
	}

	private boolean resourcePackAdd(ConfigurationResourcePack[] list) {
		for (ConfigurationResourcePack item : list) {
			if (!resourcePackAdd(item))
				return false;
		}
		
		return true;
	}

	private boolean resourcePackAdd(ConfigurationResourcePack item) {
		
		try {
			forgeApi.resourcePackAdd(item.filenameGet());
		} catch (StorageException e) {
			isLoadError = true;
			log.AddError("Unable to add resource pack \""
					+ item.filenameGet() + "\"  ."
					+ e.getMessage());
			return false;
		}
		
		return true;
	}

	private boolean bookCreate(ConfigurationBook[] configurationBookList) {
		// create texture
		for (ConfigurationBook configurationBook : configurationBookList) {
			if (!bookTextureCreate(configurationBook))
				return false;
		}

		// create book item
		for (ConfigurationBook configurationBook : configurationBookList) {
			if (!bookItemCreate(configurationBook))
				return false;
		}
		
		return true;
	}

	private boolean bookTextureCreate(ConfigurationBook configurationBook) {
		try {
			bookTextureStorage.textureCreate(configurationBook, forgeApi.minecraftDirectoryGet(), NAME_INTERNAL);
		} catch (StorageException e) {
			isLoadError = true;
			log.AddError("Unable to create book texture.  " + e.getMessage());
			return false;
		}
		
		return true;
	}

	private boolean bookItemCreate(ConfigurationBook configurationBook) {
		String nameInternal = configurationBook.nameInternalGet();
		String nameDisplay = configurationBook.nameDisplayGet();

		// get icon for book
		BookTexture bookTexture = bookTextureStorage.bookListGet().getByBookName(nameInternal);
		TextureResourceLocation iconTexture = bookTexture.itemIconGet();
		String textureName = iconTexture.locationGet().toString();

		// create item
		BookItem item = new BookItem(nameInternal);

		item.eventBookItemUse.add(new EventReceive<BookItemUseParameters>() {
			public void receive(BookItemUseParameters item) {
				if (isLoadError) {
					log.AddConsoleErrorGeneral();
					return;
				}

				openBookNameSet(item.bookNameInternal);

				forgeApi.guiOpen(CentralController.GuiId.BOOK, item.world, item.player);
			}
		});

		// use custom item renderer so external textures can be used for book item
		ItemRenderer itemRenderer = new ItemRenderer(iconTexture, render2d);

		forgeApi.itemRegister(item, nameInternal, nameDisplay, itemRenderer);
		
		return true;
	}

	public GuiScreen guiCreateClient(int id) {
		switch (id) {
		case CentralController.GuiId.BOOK: {
			return new BookGuiScreen(render2d, openBookName,
					bookTextureStorage.bookListGet().getByBookName(openBookName),
					configuration.bookGetByName(openBookName), forgeApi);
		}
		default:
			return null;
		}
	}

	public Object guiCreateServer(int id) {
		switch (id) {
		case CentralController.GuiId.BOOK:
			return null;
		default:
			return null;
		}
	}

	public void guiHandlerCreate() {
		mod.guiHandler.guiFactorySet(this);
		forgeApi.guiHandlerRegister(mod.guiHandler, mod);
	}
}
