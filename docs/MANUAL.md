# Manual

## Introduction
This tool renames the contigs in a gtf file.

## Example
To run this tool:
```bash
java -jar ReplaceContigsGtfFile-version.jar -I input.gtf -o output.gtf --contig "1=chr1"
```

to get help:
```bash
java -jar ReplaceContigsGtfFile-version.jar --help
General Biopet options


Options for ReplaceContigsGtfFile

Usage: ReplaceContigsGtfFile [options]

  -l, --log_level <value>  Level of log information printed. Possible levels: 'debug', 'info', 'warn', 'error'
  -h, --help               Print usage
  -v, --version            Print version
  -I, --input <file>       Input gtf file
  -o, --output <file>      Output gtf file
  --contig <value>
  --writeAsGff
  --contigMappingFile <value>
                           File how to map contig names, first column is the new name, second column is semicolon separated list of alternative names
```

## Output
A new GTF file.
