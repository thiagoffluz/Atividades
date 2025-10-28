package processamentoimagens;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ProcessamentoImagensAtividade2 {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\nEscolha uma das tarefas abaixo (1 a 15) ou 0 para sair:");
            System.out.println(" 1 - Exibir valores RGB de cada pixel de uma imagem carregada.");
            System.out.println(" 2 - Converter imagem colorida em tons de cinza pela media dos canais RGB.");
            System.out.println(" 3 - Converter imagem para cinza usando ponderacao perceptual (0.3R + 0.59G + 0.11B).");
            System.out.println(" 4 - Aplicar limiarizacao em imagem em tons de cinza, gerando imagem binaria (P/B).");
            System.out.println(" 5 - Gerar e exibir histograma com contagem de intensidades em imagem em tons de cinza.");
            System.out.println(" 6 - Reduzir niveis de cinza (quantizacao) com numero de niveis definido pelo usuario (2,4,8...)");
            System.out.println(" 7 - Reduzir resolucao de imagem em tons de cinza por subamostragem proporcional.");
            System.out.println(" 8 - Calcular tamanho total da imagem em bits e bytes a partir de suas dimensoes e profundidade.");
            System.out.println(" 9 - Criar imagem artificial com faixas em tons de cinza simulando bandas de Mach.");
            System.out.println("10 - Gerar padrao xadrez binario com blocos brancos e pretos de tamanho e quantidade configuravel.");
            System.out.println("11 - Converter imagem RGB para o modelo de cor CMY (Ciano, Magenta, Amarelo).");
            System.out.println("12 - Converter imagem RGB para o modelo HSI (Matiz, Saturacao e Intensidade), salvando cada canal.");
            System.out.println("13 - Criar imagem artificial com gradiente continuo de cinza horizontal e vertical.");
            System.out.println("14 - Gerar imagem a partir de valores manuais de pixels inseridos pelo usuario (tons de cinza).");
            System.out.println("15 - Detectar regioes conectadas em imagem binaria, destacando ou contando essas regioes.");

            System.out.print("Tarefa numero: ");
            opcao = entrada.nextInt();

            switch (opcao) {
                case 1:
                    metodoUm();
                    break;
                case 2:
                    metodoDois();
                    break;
                case 3:
                    metodoTres();
                    break;
                case 4:
                    metodoQuatro(entrada);
                    break;
                case 5:
                    metodoCinco();
                    break;
                case 6:
                    metodoSeis(entrada);
                    break;
                case 7:
                    metodoSete(entrada);
                    break;
                case 8:
                    metodoOito(entrada);
                    break;
                case 9:
                    metodoNove();
                    break;
                case 10:
                    metodoDez(entrada);
                    break;
                case 11:
                    metodoOnze();
                    break;
                case 12:
                    metodoDoze();
                    break;
                case 13:
                    metodoTreze();
                    break;
                case 14:
                    metodoQuatorze(entrada);
                    break;
                case 15:
                    metodoQuinze();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }

        entrada.close();
    }

    private static void metodoUm() {
        try {
            BufferedImage img = ImageIO.read(new File("entrada.jpg"));

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color c = new Color(img.getRGB(x, y));
                    System.out.printf("Posição (%d,%d): R=%d, G=%d, B=%d\n",
                            x, y, c.getRed(), c.getGreen(), c.getBlue());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo.");
        }
    }

    private static void metodoDois() {
        try {
            BufferedImage img = ImageIO.read(new File("entrada.jpg"));
            BufferedImage nova = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color c = new Color(img.getRGB(x, y));
                    int v = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                    Color nc = new Color(v, v, v);
                    nova.setRGB(x, y, nc.getRGB());
                }
            }

            ImageIO.write(nova, "png", new File("metodo2.png"));
            System.out.println("Arquivo salvo como metodo2.png");
        } catch (IOException e) {
            System.out.println("Erro ao processar.");
        }
    }

    private static void metodoTres() {
        try {
            BufferedImage img = ImageIO.read(new File("entrada.jpg"));
            BufferedImage nova = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color c = new Color(img.getRGB(x, y));
                    int v = (int) (0.3 * c.getRed() + 0.59 * c.getGreen() + 0.11 * c.getBlue());
                    Color nc = new Color(v, v, v);
                    nova.setRGB(x, y, nc.getRGB());
                }
            }

            ImageIO.write(nova, "png", new File("cinza_ponderada.png"));
            System.out.println("Arquivo salvo como cinza_ponderada.png");
        } catch (IOException e) {
            System.out.println("Erro ao processar.");
        }
    }

    private static void metodoQuatro(Scanner sc) {
        try {
            System.out.println("Informe o limiar:");
            int l = sc.nextInt();

            BufferedImage img = ImageIO.read(new File("metodo2.png"));
            BufferedImage nova = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color c = new Color(img.getRGB(x, y));
                    int v = c.getRed() < l ? 0 : 255;
                    Color nc = new Color(v, v, v);
                    nova.setRGB(x, y, nc.getRGB());
                }
            }

            ImageIO.write(nova, "png", new File("metodo4.png"));
            System.out.println("Arquivo salvo como metodo4.png");
        } catch (IOException e) {
            System.out.println("Erro ao processar.");
        }
    }

    private static void metodoCinco() {
        try {
            BufferedImage img = ImageIO.read(new File("metodo2.png"));
            int[] h = new int[256];

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color c = new Color(img.getRGB(x, y));
                    h[c.getRed()]++;
                }
            }

            System.out.println("Distribuição:");
            for (int i = 0; i < h.length; i++) {
                System.out.printf("%d: %d\n", i, h[i]);
            }
        } catch (IOException e) {
            System.out.println("Erro ao processar.");
        }
    }

    private static void metodoSeis(Scanner sc) {
        try {
            System.out.println("Informe níveis:");
            int n = sc.nextInt();

            BufferedImage img = ImageIO.read(new File("metodo2.png"));
            BufferedImage nova = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

            int f = 256 / n;

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color c = new Color(img.getRGB(x, y));
                    int v = (c.getRed() / f) * f;
                    v = Math.min(v, 255);
                    Color nc = new Color(v, v, v);
                    nova.setRGB(x, y, nc.getRGB());
                }
            }

            ImageIO.write(nova, "png", new File("metodo6.png"));
            System.out.println("Arquivo salvo como metodo6.png");
        } catch (IOException e) {
            System.out.println("Erro ao processar.");
        }
    }

    private static void metodoSete(Scanner sc) {
        try {
            System.out.println("Informe largura:");
            int w = sc.nextInt();
            System.out.println("Informe altura:");
            int h = sc.nextInt();

            BufferedImage img = ImageIO.read(new File("metodo2.png"));
            BufferedImage nova = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            double rx = (double) img.getWidth() / w;
            double ry = (double) img.getHeight() / h;

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int ox = (int) (x * rx);
                    int oy = (int) (y * ry);
                    Color c = new Color(img.getRGB(ox, oy));
                    nova.setRGB(x, y, c.getRGB());
                }
            }

            ImageIO.write(nova, "png", new File("metodo7.png"));
            System.out.println("Arquivo salvo como metodo7.png");
        } catch (IOException e) {
            System.out.println("Erro ao processar.");
        }
    }

    private static void metodoOito(Scanner sc) {
        System.out.println("Informe largura:");
        int w = sc.nextInt();
        System.out.println("Informe altura:");
        int h = sc.nextInt();
        System.out.println("Informe bits por pixel:");
        int b = sc.nextInt();

        long tb = (long) w * h * b;
        long tB = tb / 8;

        System.out.printf("Tamanho: %d bits (%d bytes)\n", tb, tB);
    }

    private static void metodoNove() {
        int w = 512;
        int h = 100;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        int n = 8;
        int s = w / n;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p = x / s;
                int v = p * (256 / n);
                Color c = new Color(v, v, v);
                img.setRGB(x, y, c.getRGB());
            }
        }

        try {
            ImageIO.write(img, "png", new File("metodo9.png"));
            System.out.println("Arquivo salvo como metodo9.png");
        } catch (IOException e) {
            System.out.println("Erro ao salvar.");
        }
    }

    private static void metodoDez(Scanner sc) {
        System.out.println("Informe blocos:");
        int b = sc.nextInt();
        System.out.println("Informe tamanho do bloco:");
        int t = sc.nextInt();

        int s = b * t;
        BufferedImage img = new BufferedImage(s, s, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < s; y++) {
            for (int x = 0; x < s; x++) {
                int bx = x / t;
                int by = y / t;
                int v = (bx + by) % 2 == 0 ? 255 : 0;
                Color c = new Color(v, v, v);
                img.setRGB(x, y, c.getRGB());
            }
        }

        try {
            ImageIO.write(img, "png", new File("metodo10.png"));
            System.out.println("Arquivo salvo como metodo10.png");
        } catch (IOException e) {
            System.out.println("Erro ao salvar.");
        }
    }

    private static void metodoOnze() {
        try {
            BufferedImage img = ImageIO.read(new File("entrada.jpg"));
            BufferedImage nova = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color c = new Color(img.getRGB(x, y));
                    int cn = (int) ((1 - c.getRed() / 255.0) * 255);
                    int mn = (int) ((1 - c.getGreen() / 255.0) * 255);
                    int yn = (int) ((1 - c.getBlue() / 255.0) * 255);
                    Color nc = new Color(cn, mn, yn);
                    nova.setRGB(x, y, nc.getRGB());
                }
            }

            ImageIO.write(nova, "png", new File("metodo11.png"));
            System.out.println("Arquivo salvo como metodo11.png");
        } catch (IOException e) {
            System.out.println("Erro ao processar.");
        }
    }

    private static void metodoDoze() {
        try {
            BufferedImage img = ImageIO.read(new File("entrada.jpg"));
            BufferedImage h = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            BufferedImage s = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            BufferedImage i = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color c = new Color(img.getRGB(x, y));
                    double r = c.getRed() / 255.0;
                    double g = c.getGreen() / 255.0;
                    double b = c.getBlue() / 255.0;

                    double num = 0.5 * ((r - g) + (r - b));
                    double den = Math.sqrt(Math.pow(r - g, 2) + (r - b) * (g - b));
                    double ang = Math.acos(num / (den + 1e-6));
                    double hue = b <= g ? ang : 2 * Math.PI - ang;
                    hue = hue * 180 / Math.PI;

                    double min = Math.min(r, Math.min(g, b));
                    double sat = 1 - 3 * min / (r + g + b + 1e-6);

                    double intens = (r + g + b) / 3;

                    int hv = (int) (hue * 255 / 360);
                    int sv = (int) (sat * 255);
                    int iv = (int) (intens * 255);

                    h.setRGB(x, y, new Color(hv, hv, hv).getRGB());
                    s.setRGB(x, y, new Color(sv, sv, sv).getRGB());
                    i.setRGB(x, y, new Color(iv, iv, iv).getRGB());
                }
            }

            ImageIO.write(h, "png", new File("metodo12h.png"));
            ImageIO.write(s, "png", new File("metodo12s.png"));
            ImageIO.write(i, "png", new File("metodo12i.png"));
            System.out.println("Arquivos salvos como metodo12h.png, metodo12s.png e metodo12i.png");
        } catch (IOException e) {
            System.out.println("Erro ao processar.");
        }
    }

    private static void metodoTreze() {
        int w = 256;
        int h = 256;
        BufferedImage hor = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        BufferedImage ver = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color ch = new Color(x, x, x);
                hor.setRGB(x, y, ch.getRGB());

                Color cv = new Color(y, y, y);
                ver.setRGB(x, y, cv.getRGB());
            }
        }

        try {
            ImageIO.write(hor, "png", new File("metodo13h.png"));
            ImageIO.write(ver, "png", new File("metodo13v.png"));
            System.out.println("Arquivos salvos como metodo13h.png e metodo13v.png");
        } catch (IOException e) {
            System.out.println("Erro ao salvar.");
        }
    }

    private static void metodoQuatorze(Scanner sc) {
        System.out.println("Informe linhas:");
        int l = sc.nextInt();
        System.out.println("Informe colunas:");
        int c = sc.nextInt();

        int[][] m = new int[l][c];

        System.out.println("Informe valores (0-255):");
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < c; j++) {
                m[i][j] = sc.nextInt();
            }
        }

        BufferedImage img = new BufferedImage(c, l, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < l; y++) {
            for (int x = 0; x < c; x++) {
                int v = m[y][x];
                Color nc = new Color(v, v, v);
                img.setRGB(x, y, nc.getRGB());
            }
        }

        try {
            ImageIO.write(img, "png", new File("metodo14.png"));
            System.out.println("Arquivo salvo como metodo14.png");
        } catch (IOException e) {
            System.out.println("Erro ao salvar.");
        }
    }

    private static void metodoQuinze() {
        try {
            BufferedImage img = ImageIO.read(new File("metodo4.png"));
            int w = img.getWidth();
            int h = img.getHeight();

            int[][] r = new int[h][w];
            int atual = 1;

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    Color c = new Color(img.getRGB(x, y));
                    if (c.getRed() == 0) {
                        int a = y > 0 ? r[y - 1][x] : 0;
                        int e = x > 0 ? r[y][x - 1] : 0;

                        if (a == 0 && e == 0) {
                            r[y][x] = atual++;
                        } else if (a != 0 && e == 0) {
                            r[y][x] = a;
                        } else if (a == 0 && e != 0) {
                            r[y][x] = e;
                        } else {
                            r[y][x] = Math.min(a, e);
                        }
                    }
                }
            }

            int reg = atual - 1;
            System.out.println("Regiões encontradas: " + reg);

            BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (r[y][x] == 0) {
                        res.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        int hash = r[y][x] * 1000;
                        int red = (hash * 37) % 256;
                        int green = (hash * 73) % 256;
                        int blue = (hash * 109) % 256;
                        res.setRGB(x, y, new Color(red, green, blue).getRGB());
                    }
                }
            }

            ImageIO.write(res, "png", new File("metodo15.png"));
            System.out.println("Arquivo salvo como metodo15.png");
        } catch (IOException e) {
            System.out.println("Erro ao processar.");
        }
    }
}
