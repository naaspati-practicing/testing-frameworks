package rules

import static org.junit.Assert.*

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * The TemporaryFolder rule allows the creation of fles and folders that are
 * guaranteed to be deleted when the test method fnishes (whether it passes or fails).
 */
class TemporaryFolderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder()	

	@Test
	public void testUsingTempFolder() {
		File file = folder.newFile('myfiles.txt')
		File folder2 = folder.newFolder('mysubfolder')
	}

}
