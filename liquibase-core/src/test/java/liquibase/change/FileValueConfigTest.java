package liquibase.change;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;

import org.junit.Before;
import org.junit.Test;

public class FileValueConfigTest {



	private static final String UTF_8 = "UTF-8";
	private static final String ISO_8859_1 = "ISO-8859-1";

	private static final String FILE_PATH = "some/file.txt";

	private static final String TEXT = "some text content";
	private static final String GERMAN_UMLAUTS = "äöü";

	private ResourceAccessor resourceAccessor;

	@Before
	public void setUp() {
		resourceAccessor = createMock(ResourceAccessor.class);
	}

	@Test(expected = UnexpectedLiquibaseException.class)
	public void getFileContent_fileNotFound_throwsException() throws Exception {
		expect(resourceAccessor.getResourceAsStream(FILE_PATH)).andReturn(null);
		replay(resourceAccessor);
		FileValueConfig fileValueConfig = createFileValueConfig(FILE_PATH,
				UTF_8);

		fileValueConfig.getFileContent();
	}

	@Test
	public void getFileContent_fileContentExists_returnsFileContent()
			throws Exception {
		mockResourceAccessor(TEXT.getBytes());

		FileValueConfig fileValueConfig = createFileValueConfig(FILE_PATH,
				UTF_8);

		String content = fileValueConfig.getFileContent();

		assertEquals(TEXT, content);
		verify(resourceAccessor);
	}
	
	@Test
	public void getFileContent_specialEncoding_returnsFileContentEncodedCorrectly()
			throws Exception {
		
		Charset charset = Charset.forName(ISO_8859_1);
		ByteBuffer germanUmlauts = charset.encode(GERMAN_UMLAUTS);
		mockResourceAccessor(germanUmlauts.array());

		FileValueConfig fileValueConfig = createFileValueConfig(FILE_PATH,
				ISO_8859_1);

		String content = fileValueConfig.getFileContent();

		assertEquals(GERMAN_UMLAUTS, content);
		verify(resourceAccessor);
	}
	
	@Test
	public void getFileContent_wrongEncoding_returnsFileContentEncodedIncorrectly()
			throws Exception {
		
		Charset charset = Charset.forName(ISO_8859_1);
		ByteBuffer germanUmlauts = charset.encode(GERMAN_UMLAUTS);
		mockResourceAccessor(germanUmlauts.array());

		FileValueConfig fileValueConfig = createFileValueConfig(FILE_PATH,
				UTF_8);

		String content = fileValueConfig.getFileContent();

		assertFalse(GERMAN_UMLAUTS.equals(content));
		verify(resourceAccessor);
	}
	
	@Test
	public void getFileContent_noCharsetSpecified_usesDefaultCharset()
			throws Exception {
		
		Charset charset = Charset.defaultCharset();
		ByteBuffer germanUmlauts = charset.encode(GERMAN_UMLAUTS);
		mockResourceAccessor(germanUmlauts.array());

		FileValueConfig fileValueConfig = createFileValueConfig(FILE_PATH,
				null);

		String content = fileValueConfig.getFileContent();

		assertTrue(((String)content).contains(GERMAN_UMLAUTS));
		verify(resourceAccessor);
	}

	private void mockResourceAccessor(byte[] characterBytes) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(characterBytes);
		expect(resourceAccessor.getResourceAsStream(FILE_PATH)).andReturn(
				inputStream);
		replay(resourceAccessor);
	}

	private FileValueConfig createFileValueConfig(String filePath,
			String encoding) {
		FileValueConfig fileValueConfig = new FileValueConfig(resourceAccessor);
		fileValueConfig.setPath(filePath);
		fileValueConfig.setEncoding(encoding);
		return fileValueConfig;
	}

}
