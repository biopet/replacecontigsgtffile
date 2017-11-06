package nl.biopet.tools.replacecontigsgtffile

import java.io.File

import nl.biopet.utils.ngs.fasta
import nl.biopet.utils.tool.AbstractOptParser

class ArgsParser(cmdName: String) extends AbstractOptParser[Args](cmdName) {
  opt[File]('I', "input") required () unbounded () valueName "<file>" action { (x, c) =>
    c.copy(input = x)
  } text "Input GTF file"
  opt[File]('o', "output") required () unbounded () valueName "<file>" action { (x, c) =>
    c.copy(output = x)
  } text "Output GTF file"
  opt[File]('R', "referenceFile") required () unbounded () valueName "<file>" action { (x, c) =>
    c.copy(referenceFile = x)
  } text "Reference fasta file"
  opt[Map[String, String]]("contig") unbounded () action { (x, c) =>
    c.copy(contigs = c.contigs ++ x)
  } text
    """Only include these contigs in the output file. Can be specified multiple times for multiple contigs.
      |When not specified, all contigs will be included in the output file.
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
