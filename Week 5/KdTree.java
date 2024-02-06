import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;
    private RectHV square;

    public KdTree() {
        root = null;
        size = 0;
        square = new RectHV(0., 0., 1., 1.);
    }
    // construct an empty tree

    private class Node {
        
        private Point2D point;
        private Node left;
        private Node right;
        private boolean vert;

        private Node(Point2D p, boolean vertical) {
            this.point = p;
            vert = vertical;
        }

        private void draw(double x1, double x2, double y1, double y2) {
            double xmin = x1;
            double xmax = x2;
            double ymin = y1;
            double ymax = y2;

            if (point != null) {
                StdDraw.setPenColor(StdDraw.BLACK);
                point.draw();
            }
            
            if (vert) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(point.x(), ymin, point.x(), ymax);
                if (left != null) left.draw(xmin, point.x(), ymin, ymax);
                if (right != null) right.draw(point.x(), xmax, ymin, ymax);
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(xmin, point.y(), xmax, point.y());
                if (left != null) left.draw(xmin, xmax, ymin, point.y());
                if (right != null) right.draw(xmin, xmax, point.y(), ymax);
            }
        }

        private List<Point2D> range(RectHV rect, RectHV thisRect) {
            RectHV rectLeft;
            RectHV rectRight;
            double xmin = thisRect.xmin();
            double xmax = thisRect.xmax();
            double ymin = thisRect.ymin();
            double ymax = thisRect.ymax();

            List<Point2D> inside = new ArrayList<>();

            if (rect.intersects(thisRect)) {

                if (rect.contains(point)) inside.add(point);

                if (left != null) {
                    if (this.vert) {
                        RectHV rectVert = new RectHV(xmin, ymin, point.x(), ymax);
                        rectLeft = rectVert;
                    }
                    else {
                        RectHV rectHor = new RectHV(xmin, ymin, xmax, point.y());
                        rectLeft = rectHor;
                    }
                    List<Point2D> insideLeft = left.range(rect, rectLeft);
                    inside.addAll(insideLeft);
                }

                if (right != null) {
                    if (this.vert) {
                        RectHV rectVert = new RectHV(point.x(), ymin, xmax, ymax);
                        rectRight = rectVert;
                    }
                    else {
                        RectHV rectHor = new RectHV(xmin, point.y(), xmax, ymax);
                        rectRight = rectHor;
                    }   
                    List<Point2D> insideRight = right.range(rect, rectRight);
                    inside.addAll(insideRight);
                }
            }
            return inside;
        }

        private Point2D nearest(Point2D p, Point2D champion, RectHV thisRect) {
            RectHV leftRect;
            RectHV rightRect;
            boolean isleft;
            double xmin = thisRect.xmin();
            double xmax = thisRect.xmax();
            double ymin = thisRect.ymin();
            double ymax = thisRect.ymax();

            double dist = p.distanceSquaredTo(champion);
            
            if (point.distanceSquaredTo(p) < dist) champion = point;

            if (vert) {
                leftRect = new RectHV(xmin, ymin, point.x(), ymax);
                rightRect = new RectHV(point.x(), ymin, xmax, ymax);
            }
            else {
                leftRect = new RectHV(xmin, ymin, xmax, point.y());
                rightRect = new RectHV(xmin, point.y(), xmax, ymax);
            }
            isleft = vert ? p.x() < point.x() : p.y() < point.y();
            if (isleft) {
                if (left != null) champion = left.nearest(p, champion, leftRect);
                if (champion.distanceSquaredTo(p) > rightRect.distanceSquaredTo(p) && right != null) champion = right.nearest(p, champion, rightRect);
            }
            else {
                if (right != null) champion = right.nearest(p, champion, rightRect);
                if (champion.distanceSquaredTo(p) > leftRect.distanceSquaredTo(p) && left != null) champion = left.nearest(p, champion, leftRect);
            }
            
            return champion;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }  
    // is the set empty? 
    public int size() {
        return size;
    }     

    // private int size(Node x) {
    //     if (x == null) return 0;
    //     return x.count;
    // }
    // number of points in the set 

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument of insert() is null.");
        
        if (!contains(p)) {
            root = insert(root, p, true);
            size++;   
        }
    }   
    // add the point to the set (if it is not already in the set)

    private Node insert(Node x, Point2D p, boolean vertical) {
        if (x == null) {
            return new Node(p, vertical);
        }

        int cmp = x.vert ? Double.compare(p.x(), x.point.x()) : Double.compare(p.y(), x.point.y());
        if (cmp < 0) x.left = insert(x.left, p, !vertical);
        else x.right = insert(x.right, p, !vertical);
        
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument provided.");
        if (isEmpty()) return false;
        else return contains(root, p);
    }    

    private boolean contains(Node x, Point2D p) {

        if (x == null) return false;

        if(p.compareTo(x.point) == 0) return true;

        int cmp = x.vert ? Double.compare(p.x(), x.point.x()) : Double.compare(p.y(), x.point.y());

        if (cmp < 0) return contains(x.left, p);
        else return contains(x.right, p);
    }
    // does the set contain point p? 
    public void draw() {

        // StdDraw.setPenRadius(.008);
        if(!isEmpty()) root.draw(0.0, 1.0, 0.0, 1.0);
    }      
    // draw all points to standard draw 

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Argument of range() is null.");

        List<Point2D> insideRect = new ArrayList<>();
        
        if (!isEmpty()) insideRect = root.range(rect, new RectHV(0., 0., 1., 1.));

        
        return insideRect;
    }     
    // all points that are inside the rectangle (or on the boundary) 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null argument provided.");

        if (!isEmpty()) {
            Point2D champion = root.point;
            champion = root.nearest(p, champion, square);

            return champion;
        }
        return null;
    }       
    // a nearest neighbor in the set to point p; null if the set is empty 

    public static void main(String[] args) {

    }
}
