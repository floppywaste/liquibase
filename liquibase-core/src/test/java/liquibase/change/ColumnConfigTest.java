package liquibase.change;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;

/**
 * Tests for {@link ColumnConfig}
 */
public class ColumnConfigTest {

	private static final String SOME_VALUE = "some value";
	private static final String FILE_CONTENT = "some text";

	@Test
	public void setValue() throws Exception {
		ColumnConfig column = new ColumnConfig();

		column.setValue(null);
		assertNull(column.getValue());

		column.setValue("abc");
		assertEquals("abc", column.getValue());

		column.setValue(null);
		assertEquals("passed null should override the value", null, column.getValue());

		column.setValue("");
		assertEquals("passed empty strings should override the value", "", column.getValue());

	}

	@Test
	public void getValueObject_fileValueSet_returnsFileContent() throws Exception {
		ColumnConfig column = new ColumnConfig();
		FileValueConfig fileValueConfig = createMock(FileValueConfig.class);
		expect(fileValueConfig.getFileContent()).andReturn(FILE_CONTENT);
		replay(fileValueConfig);
		
		column.setFileValue(fileValueConfig);
	
		assertEquals(FILE_CONTENT, column.getValueObject());
	}
	
	@Test
	public void getValueObject_valueAndFileValueSet_valueHidesFileValue() throws Exception {
		ColumnConfig column = new ColumnConfig();
		FileValueConfig fileValueConfig = createMock(FileValueConfig.class);
		expect(fileValueConfig.getFileContent()).andReturn(FILE_CONTENT);
		replay(fileValueConfig);
		
		column.setFileValue(fileValueConfig);
		column.setValue(SOME_VALUE);
	
		assertEquals(SOME_VALUE, column.getValueObject());
	}
}
