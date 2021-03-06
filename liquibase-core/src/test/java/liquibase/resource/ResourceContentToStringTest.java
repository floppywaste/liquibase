package liquibase.resource;

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

import org.junit.Before;
import org.junit.Test;

public class ResourceContentToStringTest {
	
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

		new ResourceContentToString(resourceAccessor).getFileContent(FILE_PATH, UTF_8);
	}

	@Test
	public void getFileContent_fileContentExists_returnsFileContent()
			throws Exception {
		mockResourceAccessor(TEXT.getBytes());

		String content = new ResourceContentToString(resourceAccessor).getFileContent(FILE_PATH, UTF_8);

		assertEquals(TEXT, content);
		verify(resourceAccessor);
	}
	
	@Test
	public void getFileContent_specialEncoding_returnsFileContentEncodedCorrectly()
			throws Exception {
		Charset charset = Charset.forName(ISO_8859_1);
		ByteBuffer germanUmlauts = charset.encode(GERMAN_UMLAUTS);
		mockResourceAccessor(germanUmlauts.array());

		String content = new ResourceContentToString(resourceAccessor).getFileContent(FILE_PATH, ISO_8859_1);

		assertEquals(GERMAN_UMLAUTS, content);
		verify(resourceAccessor);
	}
	
	@Test
	public void getFileContent_wrongEncoding_returnsFileContentEncodedIncorrectly()
			throws Exception {
		Charset charset = Charset.forName(ISO_8859_1);
		ByteBuffer germanUmlauts = charset.encode(GERMAN_UMLAUTS);
		mockResourceAccessor(germanUmlauts.array());

		String content =  new ResourceContentToString(resourceAccessor).getFileContent(FILE_PATH, UTF_8);

		assertFalse(GERMAN_UMLAUTS.equals(content));
		verify(resourceAccessor);
	}
	
	@Test
	public void getFileContent_noCharsetSpecified_usesDefaultCharset()
			throws Exception {
		
		Charset charset = Charset.defaultCharset();
		ByteBuffer germanUmlauts = charset.encode(GERMAN_UMLAUTS);
		mockResourceAccessor(germanUmlauts.array());

		String content = new ResourceContentToString(resourceAccessor).getFileContent(FILE_PATH,null);

		assertTrue(((String)content).contains(GERMAN_UMLAUTS));
		verify(resourceAccessor);
	}

	private void mockResourceAccessor(byte[] characterBytes) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(characterBytes);
		expect(resourceAccessor.getResourceAsStream(FILE_PATH)).andReturn(
				inputStream);
		replay(resourceAccessor);
	}

}
