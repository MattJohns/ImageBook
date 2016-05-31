package mattjohns.common.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.io.InputStream;

public class ImageStorage {
	public static BufferedImage copyFromPath(String path) throws IOException {
		// let exceptions bubble up
		File file = new File(path);
		return copyFromFile(file);
	}

	public static BufferedImage copyFromFile(File item) throws IOException {
		return ImageIO.read(item);
	}

	public static BufferedImage copyFromFile(URL item) throws IOException {
		return ImageIO.read(item);
	}

	public static BufferedImage copyFromStream(InputStream item) throws IOException {
		return ImageIO.read(item);
	}
}
