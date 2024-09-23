package com.example.apptransport.Entity;

import java.util.List;

public class PointToPointMaster {

    List<PointToPointItem> points;

    public PointToPointMaster() {
    }

    public PointToPointMaster(List<PointToPointItem> points) {
        this.points = points;
    }

    public List<PointToPointItem> getPoints() {
        return points;
    }

    public void setPoints(List<PointToPointItem> points) {
        this.points = points;
    }
}
