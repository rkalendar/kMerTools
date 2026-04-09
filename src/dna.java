public class dna {

    public static String DNA(String source) {
        if (source == null || source.isEmpty()) {
            return "";
        }
        byte[] cdn = new byte[128];
        cdn[65] = 97;   // A
        cdn[66] = 98;   // B
        cdn[67] = 99;   // C
        cdn[68] = 100;  // D
        cdn[69] = 97;   // dA=E
        cdn[70] = 99;   // dC=F
        cdn[71] = 103;  // G
        cdn[72] = 104;  // H
        cdn[73] = 103;  // I 
        cdn[74] = 103;  // dG=J
        cdn[75] = 107;  // K
        cdn[76] = 116;  // dT=L   
        cdn[77] = 109;  // M
        cdn[78] = 110;  // N
        cdn[82] = 114;  // R
        cdn[83] = 115;  // S
        cdn[84] = 116;  // T
        cdn[85] = 116;  // U
        cdn[86] = 118;  // V
        cdn[87] = 119;  // W
        cdn[89] = 121;  // Y 

        cdn[97] = 97;    // a
        cdn[98] = 98;    // b
        cdn[99] = 99;    // c
        cdn[100] = 100;  // d         
        cdn[101] = 97;   // dA=E
        cdn[102] = 99;   // dC=F
        cdn[103] = 103;  // g
        cdn[104] = 104;  // h  
        cdn[105] = 103;  // i
        cdn[106] = 103;  // dG=J
        cdn[107] = 107;  // k
        cdn[108] = 116;  // dT=L             
        cdn[109] = 109;  // m
        cdn[110] = 110;  // n
        cdn[114] = 114;  // r
        cdn[115] = 115;  // s
        cdn[116] = 116;  // t 
        cdn[117] = 116;  // u
        cdn[118] = 118;  // v
        cdn[119] = 119;  // w
        cdn[121] = 121;  // y  

        int l = source.length();
        byte[] b = source.getBytes();
        int n = -1;
        for (int i = 0; i < l; i++) {
            if (b[i] > 64 && b[i] < 128) {
                if (cdn[b[i]] > 0) {
                    b[++n] = cdn[b[i]];
                }
            }
        }
        return (n > -1) ? (new String(b, 0, n + 1)) : "";
    }

    /**
     * Returns the reverse complement of a DNA sequence. A <-> T, C <-> G
     *
     * @param seq
     * @return
     */
    public static String ComplementDNA(String source) {
        byte[] cdna = new byte[128];
        cdna[65] = 116;  //A->t
        cdna[66] = 118;  //B->v
        cdna[67] = 103;  //C->g
        cdna[68] = 104;  //D->h
        cdna[71] = 99;   //G->c
        cdna[72] = 100;  //H->d
        cdna[73] = 99;   //I->c
        cdna[75] = 109;  //K->m
        cdna[77] = 107;  //M->k
        cdna[78] = 110;  //N->n
        cdna[82] = 121;  //R->y
        cdna[83] = 115;  //S->s
        cdna[84] = 97;   //T->a
        cdna[85] = 97;   //U->a
        cdna[86] = 98;   //V->b
        cdna[87] = 119;  //W->w
        cdna[89] = 114;  //Y->r

        cdna[97] = 116;//  t <- a
        cdna[98] = 118;//  v <- b
        cdna[99] = 103;//  g <- c
        cdna[100] = 104;// h <- d
        cdna[103] = 99; // c <- g
        cdna[104] = 100;// d <- h
        cdna[105] = 99; // c <- i
        cdna[107] = 109;// m <- k
        cdna[109] = 107;// k <- m
        cdna[110] = 110;// n <- n
        cdna[114] = 121;// y <- r
        cdna[115] = 115;// s <- s
        cdna[116] = 97; // a <- t
        cdna[117] = 97; // a <- u
        cdna[118] = 98; // b <- v
        cdna[119] = 119;// w <- w
        cdna[121] = 114;// r <- y

        byte[] b = source.getBytes();
        int l = source.length();
        int n = l / 2;
        for (int i = 0; i < n; i++) {
            if (cdna[b[i]] > 0) {
                byte t = cdna[b[l - i - 1]];
                b[l - i - 1] = cdna[b[i]];
                b[i] = t;
            }
        }
        if ((l % 2) == 1) {
            if (cdna[b[n]] > 0) {
                b[n] = cdna[b[n]];
            }
        }
        return new String(b);
    }

    public static int LC(String source) {
        byte[] dx = new byte[128];
        dx[97] = 0;   //a =0
        dx[119] = 0;  //at                   
        dx[101] = 0;  //a  
        dx[108] = 1;  //t = 1            
        dx[116] = 1;  //t 
        dx[117] = 1;  //u  
        dx[98] = 2;   //tgc         
        dx[99] = 2;   //c = 2
        dx[102] = 2;  //c        
        dx[104] = 2;  //atc   
        dx[121] = 2;  //tc        
        dx[109] = 2;  //ac
        dx[115] = 2;  //gc        
        dx[103] = 3;  //g = 3
        dx[105] = 3;  //g 
        dx[106] = 3;  //g      
        dx[107] = 3;  //gt   
        dx[114] = 3;  //ag         
        dx[118] = 3;  //agc   
        dx[100] = 3;  //atg 
        dx[110] = 4;  //atgc 

        int r = 0;
        int k = 2;
        int n1 = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;

        int l = source.length();
        if (l < 6) {
            return 100;
        }

        byte[] y1 = new byte[5];
        byte[][] y2 = new byte[5][5];
        byte[][][] y3 = new byte[5][5][5];
        byte[][][][] y4 = new byte[5][5][5][5];

        int t = 4 + (l > 16 ? 16 : l - 1) + (l > 65 ? 64 : l - 2) + (l > 258 ? 256 : l - 3);

        for (int i = 0; i < l - 3; i++) {
            n1 = dx[source.charAt(i)];
            n2 = dx[source.charAt(i + 1)];
            n3 = dx[source.charAt(i + 2)];
            n4 = dx[source.charAt(i + 3)];
            if (y1[n1] == 0) {
                r++;
                y1[n1] = 1;
            }
            if (y2[n1][n2] == 0) {
                r++;
                y2[n1][n2] = 1;
            }
            if (y3[n1][n2][n3] == 0) {
                r++;
                y3[n1][n2][n3] = 1;
            }
            if (y4[n1][n2][n3][n4] == 0) {
                r++;
                y4[n1][n2][n3][n4] = 1;
            }
        }
        if (y3[n2][n3][n4] == 0) {
            r++;
            y3[n2][n3][n4] = 1;
        }
        if (y2[n3][n4] == 0) {
            r++;
            y2[n3][n4] = 1;
        }
        if (y2[n2][n3] == 0) {
            r++;
            y2[n2][n3] = 1;
        }
        if (y1[n2] == 0) {
            r++;
            y1[n2] = 1;
        }
        if (y1[n3] == 0) {
            r++;
            y1[n3] = 1;
        }
        if (y1[n4] == 0) {
            r++;
            y1[n4] = 1;
        }
        return ((100 * r) / t);
    }

    public static int iLC(String source) {
        int i = 0;
        int r = 0;
        int n1 = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;

        byte[] z = new byte[128];
        z[97] = 0; //a
        z[98] = 1; //b
        z[99] = 1; //c
        z[100] = 0; //d
        z[103] = 0; //g
        z[104] = 1; //h
        z[105] = 0; //i
        z[107] = 0; //k
        z[109] = 0; //m
        z[110] = 0; //n
        z[114] = 0; //r
        z[115] = 0; //s
        z[116] = 1; //t
        z[117] = 1; //u
        z[118] = 0; //v
        z[119] = 0; //w
        z[121] = 1; //y

        int l = source.length();
        if (l < 8) {
            return 100;
        }

        int[] y1 = new int[2];
        int[][] y2 = new int[2][2];
        int[][][] y3 = new int[2][2][2];
        int[][][][] y4 = new int[2][2][2][2];
        int[][][][][] y5 = new int[2][2][2][2][2];
        int[][][][][][] y6 = new int[2][2][2][2][2][2];
        int[][][][][][][] y7 = new int[2][2][2][2][2][2][2];

        int t = 6 + (l > 9 ? 8 : l - 2) + (l > 18 ? 16 : l - 3) + (l > 35 ? 32 : l - 4) + (l > 68 ? 64 : l - 5) + (l > 133 ? 128 : l - 6);

        for (i = 0; i < l - 6; i++) {
            n1 = z[source.charAt(i)];
            n2 = z[source.charAt(i + 1)];
            n3 = z[source.charAt(i + 2)];
            n4 = z[source.charAt(i + 3)];
            n5 = z[source.charAt(i + 4)];
            n6 = z[source.charAt(i + 5)];
            n7 = z[source.charAt(i + 6)];
            if (y1[n1] == 0) {
                r++;
                y1[n1] = 1;
            }
            if (y2[n1][n2] == 0) {
                r++;
                y2[n1][n2] = 1;
            }
            if (y3[n1][n2][n3] == 0) {
                r++;
                y3[n1][n2][n3] = 1;
            }
            if (y4[n1][n2][n3][n4] == 0) {
                r++;
                y4[n1][n2][n3][n4] = 1;
            }
            if (y5[n1][n2][n3][n4][n5] == 0) {
                r++;
                y5[n1][n2][n3][n4][n5] = 1;
            }
            if (y6[n1][n2][n3][n4][n5][n6] == 0) {
                r++;
                y6[n1][n2][n3][n4][n5][n6] = 1;
            }
            if (y7[n1][n2][n3][n4][n5][n6][n7] == 0) {
                r++;
                y7[n1][n2][n3][n4][n5][n6][n7] = 1;
            }
        }

        for (i = 1; i < l - 5; i++) {
            n1 = z[source.charAt(i)];
            n2 = z[source.charAt(i + 1)];
            n3 = z[source.charAt(i + 2)];
            n4 = z[source.charAt(i + 3)];
            n5 = z[source.charAt(i + 4)];
            n6 = z[source.charAt(i + 5)];
            if (y1[n1] == 0) {
                r++;
                y1[n1] = 1;
            }
            if (y2[n1][n2] == 0) {
                r++;
                y2[n1][n2] = 1;
            }
            if (y3[n1][n2][n3] == 0) {
                r++;
                y3[n1][n2][n3] = 1;
            }
            if (y4[n1][n2][n3][n4] == 0) {
                r++;
                y4[n1][n2][n3][n4] = 1;
            }
            if (y5[n1][n2][n3][n4][n5] == 0) {
                r++;
                y5[n1][n2][n3][n4][n5] = 1;
            }
            if (y6[n1][n2][n3][n4][n5][n6] == 0) {
                r++;
                y6[n1][n2][n3][n4][n5][n6] = 1;
            }
        }

        for (i = 2; i < l - 4; i++) {
            n1 = z[source.charAt(i)];
            n2 = z[source.charAt(i + 1)];
            n3 = z[source.charAt(i + 2)];
            n4 = z[source.charAt(i + 3)];
            n5 = z[source.charAt(i + 4)];
            if (y1[n1] == 0) {
                r++;
                y1[n1] = 1;
            }
            if (y2[n1][n2] == 0) {
                r++;
                y2[n1][n2] = 1;
            }
            if (y3[n1][n2][n3] == 0) {
                r++;
                y3[n1][n2][n3] = 1;
            }
            if (y4[n1][n2][n3][n4] == 0) {
                r++;
                y4[n1][n2][n3][n4] = 1;
            }
            if (y5[n1][n2][n3][n4][n5] == 0) {
                r++;
                y5[n1][n2][n3][n4][n5] = 1;
            }
        }

        for (i = 3; i < l - 3; i++) {
            n1 = z[source.charAt(i)];
            n2 = z[source.charAt(i + 1)];
            n3 = z[source.charAt(i + 2)];
            n4 = z[source.charAt(i + 3)];
            if (y1[n1] == 0) {
                r++;
                y1[n1] = 1;
            }
            if (y2[n1][n2] == 0) {
                r++;
                y2[n1][n2] = 1;
            }
            if (y3[n1][n2][n3] == 0) {
                r++;
                y3[n1][n2][n3] = 1;
            }
            if (y4[n1][n2][n3][n4] == 0) {
                r++;
                y4[n1][n2][n3][n4] = 1;
            }
        }

        for (i = 4; i < l - 2; i++) {
            n1 = z[source.charAt(i)];
            n2 = z[source.charAt(i + 1)];
            n3 = z[source.charAt(i + 2)];
            if (y1[n1] == 0) {
                r++;
                y1[n1] = 1;
            }
            if (y2[n1][n2] == 0) {
                r++;
                y2[n1][n2] = 1;
            }
            if (y3[n1][n2][n3] == 0) {
                r++;
                y3[n1][n2][n3] = 1;
            }
        }

        for (i = 5; i < l - 1; i++) {
            n1 = z[source.charAt(i)];
            n2 = z[source.charAt(i + 1)];
            if (y1[n1] == 0) {
                r++;
                y1[n1] = 1;
            }
            if (y2[n1][n2] == 0) {
                r++;
                y2[n1][n2] = 1;
            }
        }

        if (y1[z[source.charAt(l - 1)]] == 0) {
            r++;
        }
        if (r > t) {
            r = t;
        }
        return (100 * r / t);
    }
}
