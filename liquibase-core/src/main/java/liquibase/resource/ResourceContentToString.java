package liquibase.resource;

import java.io.IOException;
import java.io.InputStream;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.util.StreamUtil;

/**
 * Reads the content of a resource into a {@link String}.
 *
 */
public class ResourceContentToString {

	private final ResourceAccessor resourceAccessor;

	public ResourceContentToString(ResourceAccessor resourceAccessor) {
		this.resourceAccessor = resourceAccessor;
	}


	/**
	 * Reads the content from a resource specified by path.
	 * Uses the charset specified by encoding or the system
	 * default if none was specified.
	 * 
	 * <p>
	 * {@link IOException} is handled if occurring and will be wrapped in an {@link UnexpectedLiquibaseException}.
	 * 
	 * @param path path to the resource
	 * @param encoding the resource content charset encoding. May be <code>null</code>
	 * 
	 * @return
	 */
	public String getFileContent(String path, String encoding) {
		InputStream stream = null;
		final String fileContent;
		try {
			stream = resourceAccessor.getResourceAsStream(path);
			validatePathResolves(stream, path);
			fileContent = StreamUtil.getStreamContents(stream, encoding);
		} catch (IOException e) {
			throw new UnexpectedLiquibaseException(String.format("Could not read file %s", path), e);
		} finally {
			tryClosingStream(stream);
		}
		return fileContent;
	}

	private void validatePathResolves(InputStream stream, String path) {
		if (stream == null) {
			throw new UnexpectedLiquibaseException(String.format("Path %s could not be resolved", path));
		}
	}

	private void tryClosingStream(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				throw new UnexpectedLiquibaseException(String.format("Could not close stream"), e);
			}
		}
	}

}
