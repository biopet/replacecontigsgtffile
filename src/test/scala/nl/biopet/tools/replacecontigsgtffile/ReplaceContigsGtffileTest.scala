package nl.biopet.tools.replacecontigsgtffile

import nl.biopet.test.BiopetTest
import org.testng.annotations.Test

class ReplaceContigsGtffileTest extends BiopetTest {
  @Test
  def testNoArgs(): Unit = {
    intercept[IllegalArgumentException] {
      ReplaceContigsGtffile.main(Array())
    }
  }
}
