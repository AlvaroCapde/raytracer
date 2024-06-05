package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

public abstract class Object3D implements IIntersectable{
    private Color color;
    private Vector3D position;
    private double ambientCoefficient = 0.1;
    private double diffuseCoefficient = 0.6;
    private double specularCoefficient = 0.5;
    private double shininess = 60;

    private double reflectivity = 0;
    private double refractiveIndex = 0.0;  // Add refractive index
    private double refractionCoefficient = 0.0;  // Add refraction coefficient

    public double getRefractiveIndex() {
        return refractiveIndex;
    }

    public void setRefractiveIndex(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }

    public double getRefractionCoefficient() {
        return refractionCoefficient;
    }

    public void setRefractionCoefficient(double refractionCoefficient) {
        this.refractionCoefficient = refractionCoefficient;
    }

    public double getAmbientCoefficient() {
        return ambientCoefficient;
    }

    public void setAmbientCoefficient(double ambientCoefficient) {
        this.ambientCoefficient = ambientCoefficient;
    }

    public double getDiffuseCoefficient() {
        return this.diffuseCoefficient;
    }

    public void setDiffuseCoefficient(double diffuseCoefficient) {
        this.diffuseCoefficient = diffuseCoefficient;
    }

    public double getSpecularCoefficient() {
        return this.specularCoefficient;
    }

    public void setSpecularCoefficient(double specularCoefficient) {
        this.specularCoefficient = specularCoefficient;
    }

    public double getShininess() {
        return this.shininess;
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    public Object3D(Vector3D position, Color color) {
        setPosition(position);
        setColor(color);
    }
    public Object3D(Vector3D position, Color color, double reflectivity, double refractiveIndex, double refractionCoefficient) {
        setPosition(position);
        setColor(color);
        setReflectivity(reflectivity);
        setRefractiveIndex(refractiveIndex);
        setRefractionCoefficient(refractionCoefficient);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(double reflectivity) {
        this.reflectivity = reflectivity;
    }
}
