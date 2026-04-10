import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * K-mer frequency analyzer.
 *
 * Usage: java KmersTool <input_file_or_dir> <kmer_size> [output_file]
 *
 * Examples: java -jar KmersTool.jar sequence.fasta 5 result.tsv java -jar
 * KmersTool.jar sequence.fasta 5 (output -> sequence.out) java -jar
 * KmersTool.jar /path/to/dir 5 (each file -> <name>.out)
 *
 * Input : FASTA or plain-text file with a DNA sequence (ACGT), or a directory
 * containing such files (each is processed independently). Output: TSV file(s)
 * with columns: kmer \t count \t LC% \t YR% sorted by count descending. When
 * output_file is omitted the output path is derived by replacing the input
 * file's extension (or appending) with ".out".
 */
public class kmers {

    public static void main(String[] args) {

        // ---------- parse arguments ----------
        if (args.length < 2) {
            System.out.println("Usage: java -jar KmersTool.jar <input_file_or_dir> <kmer_size> [output_file]");
            System.out.println("  input_file_or_dir - FASTA/plain-text file or directory of such files");
            System.out.println("  kmer_size         - length of k-mer (e.g. 3, 5, 11, 21)");
            System.out.println("  output_file       - (optional) path for the result TSV file");
            System.out.println("                      omit to auto-generate from the input file name (.out)");
            return;
        }

        String inputPath = args[0];
        int kmerSize = Integer.parseInt(args[1]);
        // output path is optional — derived automatically when omitted
        String outputPathArg = (args.length >= 3) ? args[2] : null;

        if (kmerSize < 3) {
            System.err.println("Error: kmer size must be > 2");
            return;
        }

        File inputFile = new File(inputPath);

        if (inputFile.isDirectory()) {
            // ---------- directory mode: process every file inside ----------
            File[] files = inputFile.listFiles(File::isFile);
            if (files == null || files.length == 0) {
                System.err.println("Error: directory is empty or cannot be read: " + inputPath);
                return;
            }
            Arrays.sort(files);  // deterministic order
            System.out.println("Directory mode — " + files.length + " file(s) found in: " + inputPath);
            for (File f : files) {
                // in directory mode the explicit output arg (if any) is ignored
                // and each file gets its own <name>.out next to the input file
                String outPath = deriveOutputPath(f.getAbsolutePath(), null);
                System.out.println("\n=== Processing: " + f.getName() + " -> " + new File(outPath).getName() + " ===");
                processFile(f.getAbsolutePath(), kmerSize, outPath);
            }
        } else {
            // ---------- single-file mode ----------
            String outPath = deriveOutputPath(inputPath, outputPathArg);
            processFile(inputPath, kmerSize, outPath);
        }
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------
    /**
     * Returns the output path to use: - explicit != null → use it as-is -
     * explicit == null → replace the last extension of inputPath with ".out"
     * (or append ".out" if there is no extension)
     */
    private static String deriveOutputPath(String inputPath, String explicit) {
        if (explicit != null && !explicit.isEmpty()) {
            return explicit;
        }
        int dotIdx = inputPath.lastIndexOf('.');
        int sepIdx = Math.max(inputPath.lastIndexOf('/'), inputPath.lastIndexOf('\\'));
        // only strip the extension if the dot belongs to the file name, not a
        // parent directory component (e.g. /my.data/file has no extension)
        if (dotIdx > sepIdx) {
            return inputPath.substring(0, dotIdx) + ".out";
        }
        return inputPath + ".out";
    }

    /**
     * Full pipeline for a single file: read → count → sort → write.
     */
    private static void processFile(String inputPath, int kmerSize, String outputPath) {

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
        System.out.println("File saving started and linguistic complexity calculation....");
        startTime = System.nanoTime();
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputPath)))) {
            pw.println("kmer\tcount\tLC%\tYR%");
            // pw.println("count\tLC%");
            for (Map.Entry<String, Integer> entry : sorted) {
                //  pw.println(entry.getValue() + "\t" + dna.LC(entry.getKey()));

                pw.println(entry.getKey() + "\t" + entry.getValue()
                        + "\t" + dna.LC(entry.getKey())
                        + "\t" + dna.iLC(entry.getKey()));

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
