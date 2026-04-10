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
     * @param source
     * @return String
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
        dx[97] = 0;
        dx[119] = 0;
        dx[101] = 0;
        dx[108] = 1;
        dx[116] = 1;
        dx[117] = 1;
        dx[98] = 2;
        dx[99] = 2;
        dx[102] = 2;
        dx[104] = 2;
        dx[121] = 2;
        dx[109] = 2;
        dx[115] = 2;
        dx[103] = 3;
        dx[105] = 3;
        dx[106] = 3;
        dx[107] = 3;
        dx[114] = 3;
        dx[118] = 3;
        dx[100] = 3;
        dx[110] = 4;

        int l = source.length();
        if (l < 6) {
            return 100;
        }

        int r = 0;
        int y1 = 0;                   // битмаска, 5 бит
        int y2 = 0;                   // битмаска, 25 бит
        long y3a = 0, y3b = 0;        // битмаска, 125 бит (64+61)
        byte[] y4 = new byte[625];    // плоский массив вместо [5][5][5][5]

        int t = 4 + Math.min(l - 1, 16) + Math.min(l - 2, 64) + Math.min(l - 3, 256);

        // инициализация окна
        int n1 = dx[source.charAt(0)];
        int n2 = dx[source.charAt(1)];
        int n3 = dx[source.charAt(2)];

        for (int i = 3; i < l; i++) {
            int n4 = dx[source.charAt(i)];

            // y1 — битмаска
            int bit1 = 1 << n1;
            if ((y1 & bit1) == 0) {
                r++;
                y1 |= bit1;
            }

            // y2 — битмаска
            int bit2 = 1 << (n1 * 5 + n2);
            if ((y2 & bit2) == 0) {
                r++;
                y2 |= bit2;
            }

            // y3 — две long-маски
            int idx3 = n1 * 25 + n2 * 5 + n3;
            if (idx3 < 64) {
                long m = 1L << idx3;
                if ((y3a & m) == 0) {
                    r++;
                    y3a |= m;
                }
            } else {
                long m = 1L << (idx3 - 64);
                if ((y3b & m) == 0) {
                    r++;
                    y3b |= m;
                }
            }

            // y4 — плоский массив
            int idx4 = n1 * 125 + n2 * 25 + n3 * 5 + n4;
            if (y4[idx4] == 0) {
                r++;
                y4[idx4] = 1;
            }

            // сдвиг окна — одна операция чтения вместо четырёх
            n1 = n2;
            n2 = n3;
            n3 = n4;
        }

        // хвост: n1, n2, n3 = последние три символа (позиции l-3, l-2, l-1)
        // y3[n1][n2][n3]
        int idx3 = n1 * 25 + n2 * 5 + n3;
        if (idx3 < 64) {
            long m = 1L << idx3;
            if ((y3a & m) == 0) {
                r++;
                y3a |= m;
            }
        } else {
            long m = 1L << (idx3 - 64);
            if ((y3b & m) == 0) {
                r++;
                y3b |= m;
            }
        }

        // y2[n2][n3]
        int bit2 = 1 << (n2 * 5 + n3);
        if ((y2 & bit2) == 0) {
            r++;
            y2 |= bit2;
        }

        // y2[n1][n2]
        bit2 = 1 << (n1 * 5 + n2);
        if ((y2 & bit2) == 0) {
            r++;
            y2 |= bit2;
        }

        // y1[n1], y1[n2], y1[n3]
        int bit1 = 1 << n1;
        if ((y1 & bit1) == 0) {
            r++;
            y1 |= bit1;
        }
        bit1 = 1 << n2;
        if ((y1 & bit1) == 0) {
            r++;
            y1 |= bit1;
        }
        bit1 = 1 << n3;
        if ((y1 & bit1) == 0) {
            r++;
            y1 |= bit1;
        }

        return (100 * r) / t;
    }

    public static int iLC(String source) {
        byte[] z = new byte[128];
        z[97] = 0;
        z[98] = 1;
        z[99] = 1;
        z[100] = 0;
        z[103] = 0;
        z[104] = 1;
        z[105] = 0;
        z[107] = 0;
        z[109] = 0;
        z[110] = 0;
        z[114] = 0;
        z[115] = 0;
        z[116] = 1;
        z[117] = 1;
        z[118] = 0;
        z[119] = 0;
        z[121] = 1;

        int l = source.length();
        if (l < 8) {
            return 100;
        }

        int r = 0;

        // битмаски вместо 7 многомерных массивов
        // y1: 2 бита, y2: 4, y3: 8, y4: 16, y5: 32 — всё в int
        int y1 = 0, y2 = 0, y3 = 0, y4 = 0, y5 = 0;
        // y6: 64 бита, y7: 128 бит (два long)
        long y6 = 0, y7a = 0, y7b = 0;

        int t = 6 + Math.min(l - 2, 8) + Math.min(l - 3, 16)
                + Math.min(l - 4, 32) + Math.min(l - 5, 64)
                + Math.min(l - 6, 128);

        int win = 0; // скользящее окно из последних 7 бит

        for (int i = 0; i < l; i++) {
            win = ((win << 1) | z[source.charAt(i)]) & 0x7F;

            // 1
            int m = 1 << (win & 1);
            if ((y1 & m) == 0) {
                r++;
                y1 |= m;
            }

            // 2
            if (i >= 1) {
                m = 1 << (win & 3);
                if ((y2 & m) == 0) {
                    r++;
                    y2 |= m;
                }
            }
            // 3
            if (i >= 2) {
                m = 1 << (win & 7);
                if ((y3 & m) == 0) {
                    r++;
                    y3 |= m;
                }
            }
            // 4
            if (i >= 3) {
                m = 1 << (win & 15);
                if ((y4 & m) == 0) {
                    r++;
                    y4 |= m;
                }
            }
            // 5
            if (i >= 4) {
                m = 1 << (win & 31);
                if ((y5 & m) == 0) {
                    r++;
                    y5 |= m;
                }
            }
            // 6
            if (i >= 5) {
                long m6 = 1L << (win & 63);
                if ((y6 & m6) == 0) {
                    r++;
                    y6 |= m6;
                }
            }
            // 7
            if (i >= 6) {
                int idx = win; // win уже & 0x7F
                if (idx < 64) {
                    long m7 = 1L << idx;
                    if ((y7a & m7) == 0) {
                        r++;
                        y7a |= m7;
                    }
                } else {
                    long m7 = 1L << (idx - 64);
                    if ((y7b & m7) == 0) {
                        r++;
                        y7b |= m7;
                    }
                }
            }
        }

        if (r > t) {
            r = t;
        }
        return 100 * r / t;
    }
}
