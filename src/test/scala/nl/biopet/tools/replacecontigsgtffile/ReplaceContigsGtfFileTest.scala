/*
 * Copyright (c) 2017 Biopet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package nl.biopet.tools.replacecontigsgtffile

import java.io.{File, PrintWriter}

import nl.biopet.utils.test.tools.ToolTest
import org.testng.annotations.Test

import scala.io.Source

class ReplaceContigsGtfFileTest extends ToolTest[Args] {
  def toolCommand: ReplaceContigsGtfFile.type = ReplaceContigsGtfFile
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

    ReplaceContigsGtfFile.main(
      Array("-I",
            input.getAbsolutePath,
            "-o",
            output.getAbsolutePath,
            "-R",
            resourcePath("/fake_chrQ.fa")))

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
      Array("-I",
            input.getAbsolutePath,
            "-o",
            output.getAbsolutePath,
            "--contig",
            "1=chr1",
            "-R",
            resourcePath("/fake_chrQ.fa")))

    val reader = Source.fromFile(output)
    val line = reader.getLines().next()
    line shouldBe "chr1\tbla\tbla\t1\t2\t.\t.\t.\t"
    reader.close()
  }

  @Test
  def testReplaceCaps(): Unit = {
    val input = File.createTempFile("input.", ".gtf")
    input.deleteOnExit()
    val output = File.createTempFile("output.", ".gtf")
    output.deleteOnExit()

    val inputWriter = new PrintWriter(input)
    inputWriter.println("CHRQ\tbla\tbla\t1\t2\t.\t.\t.")
    inputWriter.close()

    ReplaceContigsGtfFile.main(
      Array("-I",
            input.getAbsolutePath,
            "-o",
            output.getAbsolutePath,
            "-R",
            resourcePath("/fake_chrQ.fa")))

    val reader = Source.fromFile(output)
    val line = reader.getLines().next()
    line shouldBe "chrQ\tbla\tbla\t1\t2\t.\t.\t.\t"
    reader.close()
  }

  @Test
  def testNoInput(): Unit = {
    val input = File.createTempFile("input.", ".gtf")
    input.delete()
    val output = File.createTempFile("output.", ".gtf")
    output.deleteOnExit()

    intercept[IllegalArgumentException] {
      ReplaceContigsGtfFile.main(
        Array("-I",
              input.getAbsolutePath,
              "-o",
              output.getAbsolutePath,
              "-R",
              resourcePath("/fake_chrQ.fa")))
    }.getMessage shouldBe s"requirement failed: Input file not found, file: ${input.getAbsolutePath}"
  }
}
