package nl.biopet.tools.replacecontigsgtffile

import java.io.File

case class Args(input: File = null,
                output: File = null,
                contigs: Map[String, String] = Map(),
                writeAsGff: Boolean = false)
