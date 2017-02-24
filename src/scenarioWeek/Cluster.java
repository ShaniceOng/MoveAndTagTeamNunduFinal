package scenarioWeek;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaniceong on 23/02/2017.
 */
public class Cluster {
    public ArrayList<Point> points;
    public Point centroid;
    public int id;
    public int numPoints = 0;

    //Creates a new Cluster
    public Cluster(int id) {
        this.id = id;
        this.points = new ArrayList();
        this.centroid = null;
        updateNumPoints();
    }

    public void updateNumPoints () {
        this.numPoints = points.size();
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        points.add(point);
        updateNumPoints();
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
        updateNumPoints();
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public int getId() {
        return id;
    }

    public void clear() {
        points.clear();
    }
}
