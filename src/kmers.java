
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * K-mer frequency analyzer.
 *
 * Usage: java KmersTool <input_file> <kmer_size> <output_file>
 *
 * Example: java -jar KmersTool.jar sequence.fasta 5 result.tsv
 *
 * Input : FASTA or plain-text file with a DNA sequence (ACGT). Output : TSV
 * file with columns: kmer \t count sorted by count descending.
 */
public class kmers {

    public static void main(String[] args) {

        // ---------- parse arguments ----------
        if (args.length < 3) {
            System.out.println("Usage: java -jar KmersTool.jar <input_file> <kmer_size> <output_file>");
            System.out.println("  input_file  - FASTA or plain text file with DNA sequence");
            System.out.println("  kmer_size   - length of k-mer (e.g. 3, 5, 11, 21)");
            System.out.println("  output_file - path for the result TSV file");
            return;
        }

        String inputPath = args[0];
        int kmerSize = Integer.parseInt(args[1]);
        String outputPath = args[2];

        if (kmerSize < 4) {
            System.err.println("Error: kmer size must be > 3");
            return;
        }
        long startTime = System.nanoTime();

        // ---------- read sequence ----------
        String sequence;
        try {
            sequence = readSequence(inputPath);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }

        System.out.println("Sequence length : " + sequence.length());
        System.out.println("K-mer size      : " + kmerSize);

        // ---------- count k-mers ----------
        KmeringSequence ms = new KmeringSequence();
        HashMap<String, Integer> kmerMap = ms.Mask(sequence, kmerSize);

        System.out.println("Unique k-mers   : " + kmerMap.size());

        // ---------- sort by count descending ----------
        List<Map.Entry<String, Integer>> sorted = kmerMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
        long duration = (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.println("\nTotal duration: " + duration + " seconds\n");

        // ---------- write output ----------
        System.out.println("File saving started...");
        startTime = System.nanoTime();
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputPath)))) {
            pw.println("kmer\tcount\tLC%\tYR%");
            for (Map.Entry<String, Integer> entry : sorted) {
                pw.println(entry.getKey() + "\t" + entry.getValue() + "\t" + dna.LC(entry.getKey()) + "\t" + dna.iLC(entry.getKey()));
            }
            System.out.println("Results saved to: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
        duration = (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.println("Duration for saving file: " + duration + " seconds\n");
    }

    /**
     * Reads a DNA sequence from a FASTA or plain-text file. - Lines starting
     * with '>' are treated as headers and skipped. - All whitespace / newlines
     * are stripped. - The sequence is converted to LowerCase.
     */
    private static String readSequence(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith(">")) {
                    continue;
                }
                sb.append(line);
            }
        }
        return sb.toString().toLowerCase();
    }
}
