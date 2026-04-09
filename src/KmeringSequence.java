import java.util.HashMap;

public class KmeringSequence {

    /**
     * Counts k-mers in seq (forward strand) and increments counts for k-mers
     * also found on the reverse complement strand. K-mers containing non-ACGT
     * characters are skipped.
     *
     * @param seq DNA sequence (uppercase ACGT expected)
     * @param kmer k-mer length
     * @return map of k-mer -> count
     */
    public HashMap<String, Integer> Mask(String seq, int kmer) {
        return Masking(seq, kmer);
    }

    private HashMap<String, Integer> Masking(String seq, int kmer) {
        byte[] dx2 = new byte[128];
        dx2[119] = 4;
        dx2[98] = 4;
        dx2[104] = 4;
        dx2[121] = 4;
        dx2[109] = 4;
        dx2[115] = 4;
        dx2[107] = 4;
        dx2[114] = 4;
        dx2[118] = 4;
        dx2[100] = 4;
        dx2[110] = 4;

        HashMap<String, Integer> map = new HashMap<>();
        seq = dna.DNA(seq);
        int l = seq.length();
        if (l < kmer) {
            return map;
        }

        byte[] b = seq.getBytes();
        for (int i = 0; i < l; i++) {
            b[i] = dx2[b[i]];
        }

        // --- forward strand ---
        int bad = 0;
        for (int i = 0; i < kmer - 1; i++) {
            if (b[i] == 4) {
                bad++;
            }
        }
        for (int i = kmer - 1; i < l; i++) {
            if (b[i] == 4) {
                bad++;
            }
            if (bad == 0) {
                map.merge(seq.substring(i - kmer + 1, i + 1), 1, Integer::sum);
            }
            if (b[i - kmer + 1] == 4) {
                bad--;
            }
        }

        // --- reverse complement strand ---
        seq = dna.ComplementDNA(seq);
        b = seq.getBytes();
        for (int i = 0; i < l; i++) {
            b[i] = dx2[b[i]];
        }

        bad = 0;
        for (int i = 0; i < kmer - 1; i++) {
            if (b[i] == 4) {
                bad++;
            }
        }
        for (int i = kmer - 1; i < l; i++) {
            if (b[i] == 4) {
                bad++;
            }
            if (bad == 0) {
                String s = seq.substring(i - kmer + 1, i + 1);
                map.computeIfPresent(s, (k, v) -> v + 1);
            }
            if (b[i - kmer + 1] == 4) {
                bad--;
            }
        }

        return map;
    }
}
