package mattjohns.common.storage;

import java.io.File;
import java.io.IOException;

public class Path {
	public static String combine(String... paths)
			throws StorageException {
		File file = new File(paths[0]);
		
		for (int i = 1; i < paths.length ; i++) {
			file = new File(file, paths[i]);
		}

		try {
			return file.getPath();
			
		}
		catch (Exception e) {
			throw new StorageException("Unable to combine paths \""
					+ paths.toString() + "\""
					+ " ." + e.getMessage());
		}
	}

	public static void directoryCreate(String path) throws IOException {
		File directory = new File(path);

		directory.mkdir();
	}

	public static boolean checkIfExist(String item) {
		return new File(item).exists();
	}

	public static boolean checkIfSubDirectory(String base, String child)
			throws IOException {
		return checkIfSubDirectory(new File(base),  new File(child));
	}

	public static boolean checkIfSubDirectory(File base, File child)
			throws IOException {
		base = base.getCanonicalFile();
		child = child.getCanonicalFile();

		File parentFile = child;

		while (parentFile != null) {
			if (base.equals(parentFile))
				return true;

			parentFile = parentFile.getParentFile();
		}

		return false;
	}
}