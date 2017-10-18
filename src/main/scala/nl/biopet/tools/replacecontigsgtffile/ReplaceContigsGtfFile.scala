package nl.biopet.tools.replacecontigsgtffile

import java.io.PrintWriter

import nl.biopet.utils.ngs.annotation.Feature
import nl.biopet.utils.tool.ToolCommand

import scala.io.Source

object ReplaceContigsGtfFile extends ToolCommand {
  def main(args: Array[String]): Unit = {
    val parser = new ArgsParser(toolName)
    val cmdArgs =
      parser.parse(args, Args()).getOrElse(throw new IllegalArgumentException)

    logger.info("Start")

    if (!cmdArgs.input.exists)
      throw new IllegalStateException("Input file not found, file: " + cmdArgs.input)

    logger.info("Start")

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
        if (cmdArgs.contigs.contains(feature.contig))
          writeLine(feature.copy(contig = cmdArgs.contigs(feature.contig)))
        else writeLine(feature)
      }
    }
    reader.close()
    writer.close()

    logger.info("Done")
  }
}
