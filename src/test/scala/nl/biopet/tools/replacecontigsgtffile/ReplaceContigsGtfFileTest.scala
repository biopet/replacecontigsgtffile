package nl.biopet.tools.replacecontigsgtffile

import java.io.{File, PrintWriter}

import nl.biopet.test.BiopetTest
import org.testng.annotations.Test

import scala.io.Source

class ReplaceContigsGtfFileTest extends BiopetTest {
  @Test
  def testNoArgs(): Unit = {
    intercept[IllegalArgumentException] {
      ReplaceContigsGtfFile.main(Array())
    }
  }

  @Test
  def testNoReplace(): Unit = {
    val input = File.createTempFile("input.", ".gtf")
    input.deleteOnExit()
    val output = File.createTempFile("output.", ".gtf")
    output.deleteOnExit()

    val inputWriter = new PrintWriter(input)
    inputWriter.println("1\tbla\tbla\t1\t2\t.\t.\t.")
    inputWriter.close()

    ReplaceContigsGtfFile.main(Array("-I", input.getAbsolutePath, "-o", output.getAbsolutePath))

    val reader = Source.fromFile(output)
    reader.getLines().next() shouldBe "1\tbla\tbla\t1\t2\t.\t.\t.\t"
    reader.close()
  }

  @Test
  def testReplace(): Unit = {
    val input = File.createTempFile("input.", ".gtf")
    input.deleteOnExit()
    val output = File.createTempFile("output.", ".gtf")
    output.deleteOnExit()

    val inputWriter = new PrintWriter(input)
    inputWriter.println("1\tbla\tbla\t1\t2\t.\t.\t.")
    inputWriter.close()

    ReplaceContigsGtfFile.main(
      Array("-I", input.getAbsolutePath, "-o", output.getAbsolutePath, "--contig", "1=chr1"))

    val reader = Source.fromFile(output)
    val line = reader.getLines().next()
    line shouldBe "chr1\tbla\tbla\t1\t2\t.\t.\t.\t"
    reader.close()
  }

  @Test
  def testNoInput(): Unit = {
    val input = File.createTempFile("input.", ".gtf")
    input.delete()
    val output = File.createTempFile("output.", ".gtf")
    output.deleteOnExit()

    intercept[IllegalStateException] {
      ReplaceContigsGtfFile.main(Array("-I", input.getAbsolutePath, "-o", output.getAbsolutePath))
    }

  }
}
