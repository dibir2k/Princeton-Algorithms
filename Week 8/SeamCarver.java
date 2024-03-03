import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

// import java.awt.Color;


public class SeamCarver {
    private Picture pic;
    private double[][] energy;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        pic = new Picture(picture);
        energy = new double[pic.height()][pic.width()];
        // int v = pic.height() * pic.width();
        // graph = new EdgeWeightedDigraph(v);
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) throw new IllegalArgumentException("Out of bounds.");

        if (x == 0 || x == width()-1 || y == 0 || y == height()-1) return 1000;

        Color left = pic.get(x-1, y);
        Color right = pic.get(x+1, y);
        Color up = pic.get(x, y-1);
        Color down = pic.get(x, y+1);

        double deltaX = (right.getRed() - left.getRed()) * (right.getRed() - left.getRed()) + (right.getGreen() - left.getGreen()) * (right.getGreen() - left.getGreen()) + (right.getBlue() - left.getBlue()) * (right.getBlue() - left.getBlue());
        double deltaY = (down.getRed() - up.getRed()) * (down.getRed() - up.getRed()) + (down.getGreen() - up.getGreen()) * (down.getGreen() - up.getGreen()) + (down.getBlue() - up.getBlue()) * (down.getBlue() - up.getBlue());

        return Math.sqrt(deltaX + deltaY);

    }

    private void fillEnergy() {
        for (int j = 0; j < pic.height(); j++)
            for (int i = 0; i < pic.width(); i++) {
                energy[j][i] = this.energy(i, j);
        }
    }

    private void dfsMinPath(int x, int y, double[][] sumEnergy, double[][] energyMat, int[][] steps, boolean horizontal) {
        if ((horizontal && x == pic.width() - 1) || (!horizontal && y == pic.height() - 1)) {
            sumEnergy[y][x] = energyMat[y][x];
            steps[y][x] = -1;
            return;
        }

        double minPath = Double.POSITIVE_INFINITY;
        int bestMv = 0;
        for (int mv = -1; mv <= 1; mv++) {
            if (horizontal) {
                int py = y + mv;
                if (py >= pic.height() || py < 0) continue;
                if (steps[py][x + 1] == 0) dfsMinPath(x + 1, py, sumEnergy, energyMat, steps, horizontal);
                if (sumEnergy[py][x + 1] < minPath) {
                    minPath = sumEnergy[py][x + 1];
                    bestMv = py;
                }
            } 
            else {
                int px = x + mv;
                if (px >= pic.width() || px < 0) continue;
                if (steps[y + 1][px] == 0) dfsMinPath(px, y + 1, sumEnergy, energyMat, steps, horizontal);
                if (sumEnergy[y + 1][px] < minPath) {
                    minPath = sumEnergy[y + 1][px];
                    bestMv = px;
                }
            }
        }
        steps[y][x] = bestMv;
        sumEnergy[y][x] = energyMat[y][x] + minPath;
    }

    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        int[][] steps = new int[pic.height()][pic.width()];
        double[][] sumEnergy = new double[pic.height()][pic.width()];
        this.fillEnergy();
        for (int y = 0; y < this.height(); y++) dfsMinPath(0, y, sumEnergy, energy, steps, true);
        int[] ht = new int[pic.width()];
        double bestEnergy = Double.POSITIVE_INFINITY;
        for (int y = 0; y < this.height(); y++) {
            if (sumEnergy[y][0] < bestEnergy) {
                bestEnergy = sumEnergy[y][0];
                ht[0] = y;
            }
        }
        for (int x = 1; x < this.width(); x++) {
            ht[x] = steps[ht[x - 1]][x - 1];
        }
        
        return ht;
    }

    public int[] findVerticalSeam() {
        // sequence of indices for horizontal seam
        int[][] steps = new int[pic.height()][pic.width()];
        double[][] sumEnergy = new double[pic.height()][pic.width()];
        this.fillEnergy();
        for (int x = 0; x < this.width(); x++) dfsMinPath(x, 0, sumEnergy, energy, steps, false);
        int[] ht = new int[pic.height()];
        double bestEnergy = Double.POSITIVE_INFINITY;
        for (int x = 0; x < this.width(); x++) {
            if (sumEnergy[0][x] < bestEnergy) {
                bestEnergy = sumEnergy[0][x];
                ht[0] = x;
            }
        }
        for (int y = 1; y < this.height(); y++) {
            ht[y] = steps[y-1][ht[y - 1]];
        }
        
        return ht;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != pic.width() || pic.height() <= 1) throw new IllegalArgumentException();

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= pic.height()) throw new IllegalArgumentException();
            if (i < seam.length -1 && Math.abs(seam[i] - seam[i+1]) > 1) throw new IllegalArgumentException();
        }
        
        Picture newPic = new Picture(pic.width(), pic.height()-1);
        
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                if (seam[i] == j) continue;
                int row = j;
                if (row > seam[i]) row--;

                newPic.set(i, row, pic.get(i, j));
            }
        }
        this.pic = newPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != pic.height() || pic.width() <= 1) throw new IllegalArgumentException();

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= pic.width()) throw new IllegalArgumentException();
            if (i < seam.length -1 && Math.abs(seam[i] - seam[i+1]) > 1) throw new IllegalArgumentException();
        }
        
        Picture newPic = new Picture(pic.width()-1, pic.height());
        
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                if (seam[j] == i) continue;
                int col = i;
                if (col > seam[j]) col--;

                newPic.set(col, j, pic.get(i, j));
            }
        }
        this.pic = newPic;
    }

    //  unit testing (optional)
    
    // public static void main(String[] args) {}
}

