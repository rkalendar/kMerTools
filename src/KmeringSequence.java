

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
        dx2[97] = 0;   //a =0
        dx2[101] = 0;  //a  
        dx2[108] = 1;  //t = 1            
        dx2[116] = 1;  //t 
        dx2[117] = 1;  //u  
        dx2[99] = 2;   //c = 2
        dx2[102] = 2;  //c  
        dx2[103] = 3;  //g = 3
        dx2[105] = 3;  //g 
        dx2[106] = 3;  //g    
        dx2[119] = 4;  //at  
        dx2[98] = 4;   //tgc   
        dx2[104] = 4;  //atc   
        dx2[121] = 4;  //tc        
        dx2[109] = 4;  //ac
        dx2[115] = 4;  //gc       
        dx2[107] = 4;  //gt   
        dx2[114] = 4;  //ag         
        dx2[118] = 4;  //agc   
        dx2[100] = 4;  //atg         
        dx2[110] = 4;  //atgc 

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
            b[i] = dx2[b[i]];
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
            b[i] = dx2[b[i]];
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
