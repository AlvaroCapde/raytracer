package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.Color;

public class Plane extends Object3D implements IIntersectable {

    /**
     * Constructs a {@code Plane} object with the given properties.
     *
     * @param y     the y coordinate where the plane is positioned.
     * @param color the {@code Color}.
     */
    public Plane(double y, Color color) {
        super(new Vector3D(0, y, 0), color);
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        Vector3D rayOrigin = ray.getOrigin();
        Vector3D rayDirection = ray.getDirection();

        // Plane normal (y-plane)
        Vector3D planeNormal = new Vector3D(0, 1, 0);

        // Check if the ray is parallel to the plane
        double denom = Vector3D.dotProduct(planeNormal, rayDirection);
        if (Math.abs(denom) < 1e-6) {
            // Ray is parallel to the plane, no intersection
            return null;
        }

        // Compute the intersection point
        double t = (getPosition().getY() - rayOrigin.getY()) / rayDirection.getY();
        if (t < 0) {
            // Intersection is behind the ray's origin
            return null;
        }

        Vector3D intersectionPoint = Vector3D.add(rayOrigin, Vector3D.scalarMultiplication(rayDirection, t));

        // Return the intersection object
        return new Intersection(intersectionPoint, t, planeNormal, this);
    }
}