package liquibase.change;

import liquibase.resource.ResourceAccessor;
import liquibase.resource.ResourceContentToString;

/**
 * This class is the representation of the fileValue tag in the XMl file It is
 * used to provide content from a file as column content.
 */
public class FileValueConfig {

	private String path;

	private String encoding;

	private final ResourceAccessor resourceAccessor;

	public FileValueConfig(ResourceAccessor resourceAccessor) {
		this.resourceAccessor = resourceAccessor;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Reads the content from a resource specified by {@link #setPath(String)}.
	 * Uses the charset specified by {@link #setEncoding(String)} or the system
	 * default if none was specified.
	 * 
	 * @return
	 */
	public String getFileContent() {
		return new ResourceContentToString(resourceAccessor).getFileContent(path, encoding); 
	}

}
