package nl.biopet.tools.replacecontigsgtffile

import java.io.PrintWriter

import nl.biopet.utils.ngs.annotation.Feature
import nl.biopet.utils.ngs.fasta
import nl.biopet.utils.tool.ToolCommand

import scala.io.Source

import scala.collection.JavaConversions._

object ReplaceContigsGtfFile extends ToolCommand[Args] {
  def emptyArgs: Args = Args()
  def argsParser = new ArgsParser(toolName)
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
}
