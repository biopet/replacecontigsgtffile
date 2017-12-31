/*
 * Copyright (c) 2017 Sequencing Analysis Support Core - Leiden University Medical Center
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

import java.io.File

import nl.biopet.utils.ngs.fasta
import nl.biopet.utils.tool.{AbstractOptParser, ToolCommand}

class ArgsParser(toolCommand: ToolCommand[Args])
    extends AbstractOptParser[Args](toolCommand) {
  opt[File]('I', "input") required () unbounded () valueName "<file>" action {
    (x, c) =>
      c.copy(input = x)
  } text "Input GTF file"
  opt[File]('o', "output") required () unbounded () valueName "<file>" action {
    (x, c) =>
      c.copy(output = x)
  } text "Output GTF file"
  opt[File]('R', "referenceFile") required () unbounded () valueName "<file>" action {
    (x, c) =>
      c.copy(referenceFile = x)
  } text "Reference fasta file"
  opt[Map[String, String]]("contig") unbounded () action { (x, c) =>
    c.copy(contigs = c.contigs ++ x)
  } text
    """Specify contig mappings on the command line. Example '1=chr1' will convert
      |contig '1' to 'chr1'
    """.stripMargin
  opt[Unit]("writeAsGff") unbounded () action { (_, c) =>
    c.copy(writeAsGff = true)
  } text "Write as GFF file instead of GTF file."
  opt[File]("contigMappingFile") unbounded () action { (x, c) =>
    c.copy(contigMapFile = Some(x))
  } text "File how to map contig names, first column is the new name, second column is semicolon separated list of alternative names"
  opt[Unit]("caseSensitive") unbounded () action { (_, c) =>
    c.copy(caseSensitive = true)
  } text "If set the tool does not try to match case differences, example: chr1_gl000191_random will not match to chr1_GL000191_random"
}
