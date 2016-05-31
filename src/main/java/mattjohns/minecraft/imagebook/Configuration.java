package mattjohns.minecraft.imagebook;

public class Configuration {
	private String description;
	private String helpUrl;
	private String configurationVersion;
	private ConfigurationResourcePack [] resourcePackList = new ConfigurationResourcePack[0];
	private ConfigurationBook [] bookList = new ConfigurationBook[0];

	public Configuration() {
	}

	public String descriptionGet() {
		return description;
	}

	public void descriptionSet(String item) {
		description = item;
	}

	public String helpUrlGet() {
		return helpUrl;
	}

	public void helpUrlSet(String item) {
		helpUrl = item;
	}

	public String configurationVersionGet() {
		return configurationVersion;
	}

	public void configurationVersionSet(String item) {
		configurationVersion = item;
	}

	public ConfigurationBook[] bookListGet() {
		return bookList;
	}

	public void bookListSet(ConfigurationBook[] item) {
		bookList = item;
	}

	public ConfigurationBook bookGetByName(String item) {
		for (int i = 0; i < bookList.length; i++) {
			if (bookList[i].nameInternalGet().equals(item))
				return bookList[i];
		}

		return null;
	}

	public ConfigurationResourcePack[] resourcePackListGet() {
		return resourcePackList;
	}

	public void resourcePackListSet(ConfigurationResourcePack[] item) {
		resourcePackList = item;
	}

	// Fields that were left blank in the configuration file should be replaced in some
	// cases.  For example if the book icon is missing the user implies they want some
	// default like resource location "minecraft:book" .
	public void setImpliedDefault() throws ConfigurationException {
		for (ConfigurationResourcePack resourcePack : resourcePackList) {
			resourcePack.setImpliedDefault();
		}

		for (ConfigurationBook book : bookList) {
			book.setImpliedDefault();
		}
	}
	
	public void validate() throws ConfigurationException {
		for (ConfigurationResourcePack resourcePack : resourcePackList) {
			resourcePack.validate();
		}

		for (ConfigurationBook book : bookList) {
			book.validate();
		}
	}
}