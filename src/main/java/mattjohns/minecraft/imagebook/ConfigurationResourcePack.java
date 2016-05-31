package mattjohns.minecraft.imagebook;

import mattjohns.minecraft.imagebook.ConfigurationTexture.enumImageType;

public class ConfigurationResourcePack {
	private String filename;

	public String filenameGet() {
		return filename;
	}

	public void filenameSet(String item) {
		filename = item;
	}
	
	public void setImpliedDefault() {
	}

	public void validate() throws ConfigurationException {
		
		if (filename.trim().length() <= 0)
			throw new ConfigurationException("Filename is empty.");
	}
}
