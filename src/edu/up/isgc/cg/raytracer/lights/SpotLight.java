package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;
import java.awt.Color;

public class SpotLight extends Light {
    private Vector3D direction;
    private double cutoffAngle;  // In degrees

    public SpotLight(Vector3D position, Color color, double intensity, Vector3D direction, double cutoffAngle) {
        super(position, color, intensity);
        this.direction =  Vector3D.normalize(direction);
        this.cutoffAngle = cutoffAngle;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public double getCutoffAngle() {
        return cutoffAngle;
    }

    @Override
    public double getNDotL(Intersection intersection) {
        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }
}