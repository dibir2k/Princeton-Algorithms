import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private static final int MIN_POINTS = 4;
    private final LineSegment[] lineSegments;
    
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Argument Points is null");

        // int len = points.length;
        // points = points.clone();

        for (int p = 0; p < points.length; p++)
            if (points[p] == null) throw new IllegalArgumentException("Argument Points has null items");

        Point[] pointsCopy = points.clone();
        Arrays.sort(pointsCopy);
        checkRepeated(pointsCopy);
        List<LineSegment> lines = new ArrayList<LineSegment>();

        
        for (Point p : pointsCopy) {
            Point[] copy = pointsCopy.clone();
            Arrays.sort(copy, p.slopeOrder());
            findSegments(copy, p, lines);
        }

        lineSegments = lines.toArray(new LineSegment[lines.size()]);
    }
    

    public int numberOfSegments() {
        return lineSegments.length;
    }

    private void checkRepeated(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i-1]) == 0) throw new IllegalArgumentException("Repeated Points were provided.");
        }
    }

    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    private void findSegments(Point[] sortedPoints, Point p, List<LineSegment>listOfSegments) {
        int j;
        int i = 1;
        List<Point> collinearPoints = new ArrayList<Point>();
        while (i < sortedPoints.length) {
            collinearPoints.clear();
            collinearPoints.add(p);
            j = i + 1;
            double slope = p.slopeTo(sortedPoints[i]);
            collinearPoints.add(sortedPoints[i]);
            while (j < sortedPoints.length && Double.compare(slope, p.slopeTo(sortedPoints[j])) == 0) {
                collinearPoints.add(sortedPoints[j]);
                j++;
            }
            if (collinearPoints.size() >= MIN_POINTS) {
                Collections.sort(collinearPoints);
                Point startPoint = collinearPoints.get(0);
                Point endPoint = collinearPoints.get(collinearPoints.size()-1);
                if (p.compareTo(startPoint) == 0) listOfSegments.add(new LineSegment(startPoint, endPoint));  
            }
            i = j;
        } 
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}