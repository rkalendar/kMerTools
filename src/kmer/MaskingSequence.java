package kmers;

import java.util.HashMap;

public class MaskingSequence {

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
        HashMap<String, Integer> map = new HashMap<>();
        
        seq = dna.DNA(seq);
        
        int l = seq.length();
        
        if (l < kmer) {
            return map;
        }

        int[] ax = new int[kmer];
        int[] bx = new int[5];
        
        byte[] b = seq.getBytes();

        for (int i = 0; i < l; i++) {
            b[i] = tables.dx2[b[i]];
        }

        // --- forward strand ---
        for (int i = 0; i < kmer - 1; i++) {
            ax[i] = b[i];
            bx[ax[i]]++;
        }
        for (int i = kmer - 1; i < l; i++) {
            ax[kmer - 1] = b[i];
            bx[ax[kmer - 1]]++;
            if (bx[4] == 0) { // no unknown bases in window
                String s = seq.substring(i - kmer + 1, i + 1);
                if (map.containsKey(s)) {
                    map.replace(s, map.get(s) + 1);
                } else {
                    map.put(s, 1);
                }
            }
            bx[b[i + 1 - kmer]]--;
            for (int j = 0; j < kmer - 1; j++) {
                ax[j] = ax[j + 1];
            }
        }

        seq = dna.ComplementDNA(seq);
        b = seq.getBytes();
        for (int i = 0; i < l; i++) {
            b[i] = tables.dx2[b[i]];
        }

        // --- reverse complement strand ---
        bx = new int[5];
        for (int i = 0; i < kmer - 1; i++) {
            ax[i] = b[i];
            bx[ax[i]]++;
        }
        for (int i = kmer - 1; i < l; i++) {
            ax[kmer - 1] = b[i];
            bx[ax[kmer - 1]]++;
            if (bx[4] == 0) {
                String s = seq.substring(i - kmer + 1, i + 1);
                if (map.containsKey(s)) {
                    map.replace(s, map.get(s) + 1);
                }
                // only count reverse-complement k-mers already seen on forward
            }
            bx[b[i + 1 - kmer]]--;
            for (int j = 0; j < kmer - 1; j++) {
                ax[j] = ax[j + 1];
            }
        }

        return map;
    }
}
