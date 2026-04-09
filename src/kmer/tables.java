package kmers;

public class tables {
    // Maps ASCII codes to: A=0, C=1, G=2, T=3, everything else=4
    public static final byte[] dx2 = new byte[256];

    static {
        for (int i = 0; i < 256; i++) {
            dx2[i] = 4; // default: unknown
        }
        dx2['A'] = 0; dx2['a'] = 0;
        dx2['C'] = 1; dx2['c'] = 1;
        dx2['G'] = 2; dx2['g'] = 2;
        dx2['T'] = 3; dx2['t'] = 3;
    }
}
