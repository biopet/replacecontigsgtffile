package nl.biopet.tools.replacecontigsgtffile

import java.io.File

case class Args(input: File = null,
                output: File = null,
                referenceFile: File = null,
                contigs: Map[String, String] = Map(),
                writeAsGff: Boolean = false,
                contigMapFile: Option[File] = None,
                caseSensitive: Boolean = false)
