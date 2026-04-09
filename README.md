# K-mer Frequency Analyzer

A Java command-line tool for counting k-mer frequencies in DNA sequences with Linguistic complexity (LC% and YR%) calcultion.




## Overview

The tool extracts all k-mers of a given length from a DNA sequence, counts their occurrences on the forward strand, and additionally checks the reverse complement strand — if a k-mer from the reverse complement was already observed on the forward strand, its count is incremented. K-mers containing ambiguous bases (non-ACGT characters) are automatically skipped.

Results are saved as a tab-separated file sorted by frequency in descending order.

## Author & Contact

**Ruslan Kalendar**
📧 ruslan.kalendar@helsinki.fi

---

## Features

- **FASTA and plain-text input** — supports standard `.fasta` / `.fa` files (header lines starting with `>` are skipped) as well as raw text files containing only the sequence
- **Reverse complement counting** — k-mers found on the complementary strand contribute to the frequency of already observed forward-strand k-mers
- **Ambiguity filtering** — windows containing any non-ACGT character (e.g. `N`) are excluded from the count
- **Sorted TSV output** — results are written as `kmer\tcount`, sorted by count descending

## Project Structure

```
kmers/
├── kmers.java              # Entry point — CLI argument parsing, file I/O
├── MaskingSequence.java    # Sliding-window k-mer extraction and counting
├── dna.java                # Reverse complement generation
└── tables.java             # Byte lookup table (A=0, C=1, G=2, T=3, other=4)
```

## Requirements

- **Java 25+** (tested with OpenJDK 25)

## Build

```bash
javac kmers/*.java
```

## Usage

```bash
java -cp . kmers.kmers <input_file> <kmer_size> <output_file>
```

| Argument      | Description                                      |
|---------------|--------------------------------------------------|
| `input_file`  | Path to a FASTA or plain-text DNA sequence file  |
| `kmer_size`   | Length of k-mer (e.g. `3`, `5`, `11`, `21`)      |
| `output_file` | Path for the output TSV file                     |

### Example

```bash
java -cp . kmers.kmers genome.fasta 5 result.tsv
```

Output (`result.tsv`):

```
kmer	count
ATCGA	17
TCGAT	17
GATCG	16
CGATC	16
TACGT	9
...
```

Console:

```
Sequence length : 80
K-mer size      : 5
Unique k-mers   : 33
Results saved to: result.tsv
```

## How It Works

1. The DNA sequence is read from the input file and converted to uppercase.
2. Each nucleotide is mapped to a numeric index via a lookup table (`A=0, C=1, G=2, T=3, other=4`).
3. A sliding window of size `k` moves across the forward strand. If the window contains no ambiguous bases (`index=4`), the k-mer string is added to a `HashMap` with its count.
4. The same sliding window runs over the reverse complement. If a reverse-complement k-mer already exists in the map (i.e. it was seen on the forward strand), its count is incremented.
5. The final map is sorted by frequency and written to a TSV file.


## License

This project is distributed under the terms of the [GNU General Public License v3.0](LICENSE.txt).
