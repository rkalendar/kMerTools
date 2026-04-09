# K-mer Frequency Analyzer

A Java command-line tool for counting k-mer frequencies in DNA sequences with reverse complement support and linguistic complexity analysis.

## Overview

The tool extracts all k-mers of a given length from a DNA sequence, counts their occurrences on the forward strand, and additionally checks the reverse complement strand — if a k-mer from the reverse complement was already observed on the forward strand, its count is incremented. K-mers containing ambiguous bases (non-ACGT characters) are automatically skipped.

For each unique k-mer the tool calculates two linguistic complexity metrics:

- **LC%** — linguistic complexity based on a 4-letter DNA alphabet (A, C, G, T), measuring the proportion of distinct 1- through 4-mers relative to the theoretical maximum for the given sequence length.
- **YR%** — linguistic complexity based on a binary purine/pyrimidine alphabet (R = A/G, Y = C/T), measuring the proportion of distinct 1- through 7-mers in the binary representation.

Results are saved as a tab-separated file sorted by frequency in descending order.

## Author & Contact

**Ruslan Kalendar**  
📧 ruslan.kalendar@helsinki.fi

---

## Features

- **FASTA and plain-text input** — supports standard `.fasta` / `.fa` files (header lines starting with `>` are skipped) as well as raw text files containing only the sequence
- **IUPAC nucleotide support** — the input sequence is normalized through `dna.DNA()`, which maps extended IUPAC codes (B, D, H, K, M, R, S, V, W, Y) and modified-base aliases (dA, dC, dG, dT coded as E, F, J, L) to their standard lowercase equivalents; U is treated as T
- **Reverse complement counting** — k-mers found on the complementary strand contribute to the frequency of already observed forward-strand k-mers; complement mapping covers the full IUPAC alphabet (e.g. R↔Y, K↔M, B↔V, D↔H)
- **Ambiguity filtering** — windows containing any non-ACGT character (e.g. N) are excluded from the k-mer count
- **Linguistic complexity (LC%)** — 4-letter alphabet complexity using distinct n-grams of orders 1–4
- **Binary complexity (YR%)** — purine/pyrimidine alphabet complexity using distinct n-grams of orders 1–7
- **Sorted TSV output** — results are written as `kmer → count → LC% → YR%`, sorted by count descending

## Project Structure

```
kmers/
├── kmers.java              # Entry point — CLI argument parsing, file I/O
├── MaskingSequence.java    # Sliding-window k-mer extraction and counting
├── dna.java                # Sequence normalization, reverse complement, LC%, YR%
└── tables.java             # Byte lookup table (A=0, C=1, G=2, T=3, other=4)
```

### Key classes

| Class | Method | Description |
|-------|--------|-------------|
| `dna` | `DNA(String)` | Normalizes a raw sequence — maps IUPAC and modified-base codes to standard lowercase DNA letters, strips non-alphabetic characters |
| `dna` | `ComplementDNA(String)` | Returns the reverse complement using the full IUPAC alphabet |
| `dna` | `LC(String)` | Computes linguistic complexity (%) over a 4-symbol alphabet (A, C, G, T) using n-gram orders 1–4 |
| `dna` | `iLC(String)` | Computes binary linguistic complexity (%) over a 2-symbol purine/pyrimidine alphabet using n-gram orders 1–7 |
| `MaskingSequence` | `Mask(String, int)` | Counts k-mers on the forward strand and increments matches from the reverse complement |
| `tables` | `dx2[]` | Static byte lookup: maps ASCII codes to nucleotide indices (A=0, C=1, G=2, T=3, other=4) |

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
| `kmer_size`   | Length of k-mer (≥ 3)                             |
| `output_file` | Path for the output TSV file                     |

### Example

```bash
java -jar -Xms8g -Xmx32g  kmers.kmers genome.fasta 11 result.tsv

```

Output (`result.tsv`):

```
kmer	count	LC%	YR%
ATCGATCGATC	8	61	82
TCGATCGATCG	8	61	82
CGATCGATCGA	8	61	82
GATCGATCGAT	8	61	82
...
```

Console:

```
Sequence length : 80
K-mer size      : 11
Unique k-mers   : 24
Results saved to: result.tsv
```

## How It Works

1. The DNA sequence is read from the input file and converted to uppercase.
2. The sequence is normalized via `dna.DNA()` — IUPAC ambiguity codes and modified-base aliases are mapped to standard lowercase nucleotides; non-alphabetic characters are removed.
3. Each nucleotide is mapped to a numeric index via the `tables.dx2` lookup table (`A=0, C=1, G=2, T=3, other=4`).
4. A sliding window of size *k* moves across the forward strand. If the window contains no ambiguous bases (`index = 4`), the k-mer is added to a `HashMap` with its count.
5. The same sliding window runs over the reverse complement strand. If a reverse-complement k-mer already exists in the map (i.e. it was seen on the forward strand), its count is incremented.
6. For every unique k-mer two complexity scores are computed:
   - **LC%** counts distinct 1-, 2-, 3-, and 4-grams over {a, c, g, t} and divides by the theoretical maximum for the k-mer length.
   - **YR%** converts each base to a binary class (purine = 0, pyrimidine = 1), counts distinct 1- through 7-grams, and divides by the theoretical maximum.
7. The final table is sorted by frequency (descending) and written to a TSV file.

## Linguistic Complexity — Background

Linguistic complexity measures how "random" or "information-rich" a sequence is by counting the number of distinct n-grams it contains relative to the maximum possible. A homopolymer (e.g. `AAAA…`) scores close to 0 %, while a maximally complex sequence approaches 100 %.

- **LC% (4-letter)** is sensitive to base composition and short tandem repeats.
- **YR% (binary)** captures purine/pyrimidine patterns and can reveal strand asymmetry or transition/transversion bias at a structural level.

Both metrics are useful for filtering low-complexity k-mers that may represent repetitive or uninformative regions of a genome.

## License

This project is distributed under the terms of the [GNU General Public License v3.0](LICENSE.txt).
