package nl.biopet.tools.replacecontigsgtffile

import java.io.PrintWriter

import nl.biopet.utils.ngs.annotation.Feature
import nl.biopet.utils.ngs.fasta
import nl.biopet.utils.tool.ToolCommand

import scala.io.Source

import scala.collection.JavaConversions._

object ReplaceContigsGtfFile extends ToolCommand[Args] {
  def emptyArgs: Args = Args()
  def argsParser = new ArgsParser(this)
  def main(args: Array[String]): Unit = {
    val cmdArgs = cmdArrayToArgs(args)

    require(cmdArgs.input.exists,
            s"Input file not found, file: ${cmdArgs.input}")

    logger.info("Start")

    val dict = fasta.getDictFromFasta(cmdArgs.referenceFile)

    val contigMap = {
      val caseSensitive = cmdArgs.contigMapFile
        .map(fasta.readContigMapReverse)
        .getOrElse(Map()) ++ cmdArgs.contigs
      if (cmdArgs.caseSensitive) caseSensitive
      else {
        caseSensitive.map(x => x._1.toLowerCase -> x._2) ++ caseSensitive ++
          dict.getSequences
            .filter(x => x.getSequenceName.toLowerCase != x.getSequenceName)
            .map(x => x.getSequenceName.toLowerCase -> x.getSequenceName)
      }
    }

    val reader = Source.fromFile(cmdArgs.input)
    val writer = new PrintWriter(cmdArgs.output)

    def writeLine(feature: Feature): Unit = {
      if (cmdArgs.writeAsGff) writer.println(feature.asGff3Line)
      else writer.println(feature.asGtfLine)
    }

    reader.getLines().foreach { line =>
      if (line.startsWith("#")) writer.println(line)
      else {
        val feature = Feature.fromLine(line)

        if (contigMap.contains(feature.contig))
          writeLine(feature.copy(contig = contigMap(feature.contig)))
        else if (!cmdArgs.caseSensitive && contigMap.contains(
                   feature.contig.toLowerCase))
          writeLine(
            feature.copy(contig = contigMap(feature.contig.toLowerCase)))
        else writeLine(feature)
      }
    }
    reader.close()
    writer.close()

    logger.info("Done")
  }

  def descriptionText: String =
    """
      |This tool takes an input GTF file and outputs a GTF file with renamed contigs.
      |For example chr1 -> 1. This can be useful in a pipeline where tools have different
      |naming standards for contigs.
    """.stripMargin

  def manualText: String =
    s"""
       |$toolName needs a reference fasta file and an input GTF file.
       |The reference fasta is needed to validate the contigs. The renaming
       |of contigs can be specified in a contig mapping file.
       |The contig mapping file should be in the following format.
       |
       |    chr1    1;I;one
       |    chr2    2;II;two
       |
       |Any contigs found in the input VCF that have a contig name in the second column will be renamed
       |with the contig name in the corresponding first column.
       |
       |Alternatively, options can be specified on the command line. For example '1=chr1' will
       |convert all contigs named '1' to 'chr1'.
       |
       |Mappings are NOT case sensitive by default. If you need case sensitivity use the `--caseSensitive` flag.
       |
       |The output can also be a GFF file with the `--writeAsGff` flag.
       |
     """.stripMargin


  def exampleText: String =
    s"""
       |To convert the contig names in a gtf file with case sensitivity  and output as GFF run:
       |
       |${example("-I",
      "input.gtf",
      "-o",
      "output.gtf",
      "-R",
      "reference.fasta",
      "--contigMappingFile",
      "contignames.tsv",
      "--caseSensitive", "--writeAsGff")}
       |
       |To convert the contig names using command line options, similar
       |to the example contig mapping file given in the manual:
       |
       |${example(
      "-I",
      "input.gtf",
      "-o",
      "output.gtf",
      "-R",
      "reference.fasta",
      "--contig",
      "1=chr1",
      "--contig",
      "I=chr1",
      "--contig",
      "one=chr1",
      "--contig",
      "2=chr2",
      "--contig",
      "II=chr2",
      "--contig",
      "two=chr2"
    )}
       |
       | A contig mapping file and contigs can be used together:
       |${example("-I",
      "input.gtf",
      "-o",
      "output.gtf",
      "-R",
      "reference.fasta",
      "--contigMappingFile",
      "contignames.tsv",
      "--contig",
      "3=chr3",
      "--contig",
      "III=chr3")}
     """.stripMargin
}
