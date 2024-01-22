import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Argument Points is null");

        int len = points.length;

        for (int p = 0; p < len; p++)
            if (points[p] == null) throw new IllegalArgumentException("Argument Points has null items");

        
        points = points.clone();
        Arrays.sort(points);
        checkRepeated(points);
        List<LineSegment> list = new ArrayList<>();
        // lineSegments = new LineSegment[len];
        // int idx = 0;

        for (int i = 0; i < len - 3; i++) {
            for (int j = i+1; j < len - 2; j++) {
                double slopeOne = points[i].slopeTo(points[j]);
                for (int k = j+1; k < len - 1; k++) {
                    double slopeTwo = points[i].slopeTo(points[k]);
                    int firstCompare = Double.compare(slopeOne, slopeTwo);
                    for (int s = k+1; s < len; s++) {
                        double slopeThree = points[i].slopeTo(points[s]);
                        if (firstCompare == 0 && Double.compare(slopeOne, slopeThree) == 0) {
                            list.add(new LineSegment(points[i], points[s]));
                            // if (idx == lineSegments.length) resize(lineSegments.length+1);
                            // lineSegments[idx++] = new LineSegment(points[i], points[l]);
                        }
                        
                    }
                }
            }
        }
        lineSegments = list.toArray(new LineSegment[list.size()]);
    }

    private void checkRepeated(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i-1]) == 0) throw new IllegalArgumentException("Repeated Points were provided.");
        }
    }

    public int numberOfSegments() {
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
    
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
    
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}