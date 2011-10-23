package liquibase.change;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;

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

	public String getFileContent() {
		InputStream stream = null;
		final String fileContent;
		try {
			stream = resourceAccessor.getResourceAsStream(path);
			validatePath(stream);
			fileContent = IOUtils.toString(stream, encoding);
		} catch (IOException e) {
			throw new UnexpectedLiquibaseException(String.format(
					"Could not read file %s", path), e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
		return fileContent;
	}

	private void validatePath(InputStream stream) {
		if (stream == null) {
			throw new UnexpectedLiquibaseException(String.format(
					"Path %s could not be resolved", path));
		}
	}

}
