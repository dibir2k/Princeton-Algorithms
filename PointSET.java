// package KDTree.src;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private SET<Point2D> tree;

    public PointSET() {
        tree = new SET<>();
    }
    // construct an empty set of points 
    public boolean isEmpty() {
        return tree.size() == 0;
    }  
    // is the set empty? 
    public int size() {
        return tree.size();
    }     
    // number of points in the set 
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument of insert() is null.");
        
        if (!tree.contains(p)) tree.add(p);
    }   
    // add the point to the set (if it is not already in the set)
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument of contains() is null.");

        return tree.contains(p);
    }       
    // does the set contain point p? 

    public void draw() {
        // StdDraw.setPenRadius(.008);
        // StdDraw.setXscale(tree.min().x()-1., tree.max().x()+1.);
        // StdDraw.setYscale(tree.min().y()-1., tree.max().y()+1.);
        for (Point2D p : tree) p.draw();
    }         
    // draw all points to standard draw 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Argument of range() is null.");
        List<Point2D> insideRect = new ArrayList<>();
        if (!isEmpty()) {
            
            for (Point2D p : tree) {
                if (rect.contains(p)) insideRect.add(p);
            }
        }
        return insideRect;
    }     
    // all points that are inside the rectangle (or on the boundary) 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument provided.");

        if (!isEmpty()) {
            Point2D closestPoint = tree. min();
            double closestDistance = p.distanceSquaredTo(closestPoint);
            for (Point2D q : tree) {
                double distancePQ = p.distanceSquaredTo(q);
                if (distancePQ < closestDistance) {
                    closestDistance = distancePQ;
                    closestPoint = q;
                }
            }
            return closestPoint;
        }
        return null;
    }       
    // a nearest neighbor in the set to point p; null if the set is empty 

    public static void main(String[] args) {

        PointSET set = new PointSET();

        // Test if set is empty
        StdOut.printf("The set is empty: %b\n", set.isEmpty());

        // Add elements to the set
        for (int i = 0; i < 100; i++) set.insert(new Point2D(StdRandom.uniformDouble(0., 5.), StdRandom.uniformDouble(0., 5.)));
        
        // Test if set is empty
        StdOut.printf("The set is empty: %b\n", set.isEmpty());

        // Get size of the set
        StdOut.printf("The set has %d points\n", set.size());

        // Check if set contains a point
        Point2D p = new Point2D(1.5, 0.8);
        StdOut.printf("The set contains point %s: %b\n", p.toString(), set.contains(p));

        // Add p to the set
        set.insert(p);
        StdOut.printf("The set contains point %s: %b\n", p.toString(), set.contains(p));

        // Draw Points
        set.draw();

        // Points inside rectangle 
        RectHV rectangle = new RectHV(0.5, 0.3, 1.5, 1.8);

        for (Point2D q : set.range(rectangle)) StdOut.printf(q.toString() + "\n");

        // Find closest neighbour
        Point2D query = new Point2D(0.31, 2.23);
        Point2D neighbour = set.nearest(query);
        StdOut.printf("Nearest point to %s is %s\n", query.toString(), neighbour.toString());
    }
}
