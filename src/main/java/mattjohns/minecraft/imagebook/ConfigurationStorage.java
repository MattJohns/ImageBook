package mattjohns.minecraft.imagebook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import org.apache.commons.io.IOUtils;
import java.util.Arrays;
import cpw.mods.fml.common.FMLLog;
import mattjohns.common.storage.Path;
import mattjohns.common.storage.StorageException;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileOutputStream;
import com.google.gson.JsonSyntaxException;

public class ConfigurationStorage {
	private static final String FILENAME = CentralController.NAME_INTERNAL + ".json";

	// independent of mod version
	public static final String CONFIGURATION_VERSION = "0.1";
	private static final String CONFIGURATION_VERSION_LOWEST_COMPATIBLE = "0.1";

	private static final String FOLDER = "config";

	private String folderPath;

	public ConfigurationStorage(String folderPath) {
		this.folderPath = folderPath;
	}

	public Configuration CopyFrom() throws ConfigurationException
	{
		String filePathJson;
		
		try {
			filePathJson = filePathGetJson();
		}
		catch (StorageException e) {
			throw new ConfigurationException("Invalid JSON path.  " + e.getMessage());
		}
		
		File configurationFile = new File(filePathJson);

		//// need to find a way to check configuration version before reading whole file
		
		String streamText = null;
		try {
			InputStream inputStream = new FileInputStream(filePathJson);
			streamText = IOUtils.toString(inputStream);
		}
		catch (Exception e) {
			throw new ConfigurationException("Unable to read file at at \""
					+ filePathJson + "\".  "
					+ e.getMessage());
		}

		Gson gson = new Gson();

		try {
			return gson.fromJson(streamText, Configuration.class);
		}
		catch (JsonSyntaxException e) {
			throw new ConfigurationException("Error in configuration file \""
					+ filePathJson + "\".  "
					+ e.getMessage());
		}
	}

	public void CreateIfNecessary() throws ConfigurationException {
		try {
			folderCreateIfNecessary();
		}
		catch (IOException e) {
			throw new ConfigurationException("Unable to create folder.  File system error: " + e.getMessage());
		}

		try {
			fileCreateIfNecessary();
		}
		catch (ConfigurationException e) {
			throw new ConfigurationException("Unable to create file.  " + e.getMessage());
		}
		catch (IOException e) {
			throw new ConfigurationException("Unable to create file.  File system error: " + e.getMessage());
		}
	}

	private void folderCreateIfNecessary() throws IOException {
		if (!Path.checkIfExist(folderPath)) {
			Path.directoryCreate(folderPath);
		}
	}

	public String filePathGetJson() throws StorageException
	{
		return Path.combine(folderPath, FILENAME);
	}

	private void fileCreateIfNecessary() throws ConfigurationException, IOException {
		
		String filePathJson;
		
		try {
			filePathJson = filePathGetJson();
		}
		catch (StorageException e) {
			throw new ConfigurationException("Invalid JSON file path.  " + e.getMessage());
		}
		
		File configurationFile = new File(filePathJson);

		if (!configurationFile.exists())
			configurationFileCreateDefault();
	}

	private void configurationFileCreateDefault() throws ConfigurationException {
		Configuration defaultConfiguration = createDefault();

		String filePathJson;
		
		try {
			filePathJson = filePathGetJson();
		}
		catch (StorageException e) {
			throw new ConfigurationException("Invalid JSON path.  " + e.getMessage());
		}

		// unfortunately there is no way to remove the double slashes from paths in JSON so the configuration
		// files therefore require them
		Gson gson = new GsonBuilder()
				.disableHtmlEscaping()
				.setPrettyPrinting()
				.create();
		String text = gson.toJson(defaultConfiguration);

		try
		{
			PrintWriter out = new PrintWriter(filePathJson);
			out.println(text);
			out.close();
		}
		catch (Exception e) {
			throw new ConfigurationException(
					"Unable to create file at \"" + filePathJson + "\".  " + e.getMessage());
		}
	}

	private Configuration createDefault() throws ConfigurationException {
		Configuration returnValue = new Configuration();

		returnValue.descriptionSet("Configuration file for ImageBook mod.");
		returnValue.helpUrlSet("https://github.com/MattJohns/Imagebook");
		returnValue.configurationVersionSet(ConfigurationStorage.CONFIGURATION_VERSION);

		ConfigurationBook book = new ConfigurationBook();
		returnValue.bookListSet(new ConfigurationBook[1]);
		returnValue.bookListGet()[0] = book;
		
		book.nameInternalSet("ExampleBook");
		book.nameDisplaySet("Example Book");

		book.bookContainerMarginLeftSet(0.1f);
		book.bookContainerMarginRightSet(0.1f);
		book.bookContainerMarginTopSet(0.1f);
		book.bookContainerMarginBottomSet(0.1f);
		book.pageMarginOutsideSet(0.1f);
		book.pageMarginInsideSet(0.02f);
		book.pageMarginTopSet(0.1f);
		book.pageMarginBottomSet(0.1f);

		book.isBookContainerKeepAspectRatioSet(true);
		book.isPageKeepAspectRatioSet(true);

		String filePathJson;
		
		try {
			ConfigurationTexture texture;
			String externalTextureFolder = Path.combine(FOLDER, book.nameInternalGet());   

			texture = new ConfigurationTexture();
			texture.externalPathRelativeSet(Path.combine(externalTextureFolder, "Background.png"));
			book.bookContainerImageSet(texture);

			book.pageImageListSet(new ConfigurationTexture[2]);

			texture = new ConfigurationTexture();
			texture.externalPathRelativeSet(Path.combine(externalTextureFolder, "Page1.png"));
			book.pageImageListGet()[0] = texture;

			texture = new ConfigurationTexture();
			texture.externalPathRelativeSet(Path.combine(externalTextureFolder, "Page2.png"));
			book.pageImageListGet()[1] = texture;
		}
		catch (StorageException e) {
			throw new ConfigurationException("Invalid JSON path.  " + e.getMessage());
		}


		return returnValue;
	}
};