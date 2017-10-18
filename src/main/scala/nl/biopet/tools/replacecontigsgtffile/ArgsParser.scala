package nl.biopet.tools.replacecontigsgtffile

import java.io.File

import nl.biopet.utils.ngs.fasta
import nl.biopet.utils.tool.AbstractOptParser

class ArgsParser(cmdName: String) extends AbstractOptParser[Args](cmdName) {
  opt[File]('I', "input") required () unbounded () valueName "<file>" action { (x, c) =>
    c.copy(input = x)
  } text "Input gtf file"
  opt[File]('o', "output") required () unbounded () valueName "<file>" action { (x, c) =>
    c.copy(output = x)
  } text "Output gtf file"
  opt[Map[String, String]]("contig") unbounded () action { (x, c) =>
    c.copy(contigs = c.contigs ++ x)
  }
  opt[Unit]("writeAsGff") unbounded () action { (_, c) =>
    c.copy(writeAsGff = true)
  }
  opt[File]("contigMappingFile") unbounded () action { (x, c) =>
    c.copy(contigs = c.contigs ++ fasta.readContigMapReverse(x))
  } text "File how to map contig names, first column is the new name, second column is semicolon separated list of alternative names"
}
