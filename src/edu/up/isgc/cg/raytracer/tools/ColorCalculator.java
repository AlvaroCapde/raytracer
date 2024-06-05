package edu.up.isgc.cg.raytracer.tools;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Raytracer;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.lights.Light;
import edu.up.isgc.cg.raytracer.lights.SpotLight;
import edu.up.isgc.cg.raytracer.objects.Object3D;

import java.awt.*;
import java.util.List;


public class ColorCalculator {
    public static Color getAmbientColor(Color objColor, double ambientCoefficient){
        return new Color(
                (float) (objColor.getRed() / 255.0 * ambientCoefficient),
                (float) (objColor.getGreen() / 255.0 * ambientCoefficient),
                (float) (objColor.getBlue() / 255.0 * ambientCoefficient)
        );
    }

    public static Color getDiffuseColor(Intersection closestIntersection, Color objColor, double diffuseCoefficient, Light light, Vector3D lightDirection, double[] lightColors, double falloff){

        double nDotL = light.getNDotL(closestIntersection);
        double diffuseIntensity = light.getIntensity() * diffuseCoefficient * nDotL / falloff;
        if (light instanceof SpotLight) {
            double spotEffect = getSpotEffect((SpotLight) light, lightDirection);
            diffuseIntensity *= spotEffect;
        }
        double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
            objColors[colorIndex] *= diffuseIntensity * lightColors[colorIndex];
        }
        return clampColors(objColors);
    }

    public static Color getSpecularColor(Vector3D normal, Vector3D viewDirection, Vector3D lightDirection, double specularCoefficient, Light light, double[] lightColors, double falloff, double shininess){
        Vector3D halfVector = Vector3D.normalize(Vector3D.add(lightDirection, viewDirection));
        double nDotH = Math.max(0, Vector3D.dotProduct(normal, halfVector));
        double specularIntensity = light.getIntensity() * specularCoefficient * Math.pow(nDotH, shininess) / falloff;
        if (light instanceof SpotLight) {
            double spotEffect = getSpotEffect((SpotLight) light, lightDirection);
            specularIntensity *= spotEffect;
        }
        double[] multipliedByLight = new double[]{lightColors[0] * specularIntensity,lightColors[1] * specularIntensity,lightColors[2] * specularIntensity};
        return clampColors(multipliedByLight);
    }

    public static Color getReflectionColor(Ray ray, Vector3D intersectionPosition, Vector3D normal, List<Object3D> objects, List<Light> lights, Vector3D cameraPosition, double[] nearFarPlanes, int depth, double reflectivity){
        Vector3D reflectionDirection = Vector3D.normalize(Vector3D.substract(ray.getDirection(), Vector3D.scalarMultiplication(normal, 2 * Vector3D.dotProduct(ray.getDirection(), normal))));
        Vector3D offsetIntersectionPosition = Vector3D.add(intersectionPosition, Vector3D.scalarMultiplication(normal, 1e-4));
        Ray reflectionRay = new Ray(offsetIntersectionPosition, reflectionDirection);
        Color reflectionColor = Raytracer.traceRay(reflectionRay, objects, lights, cameraPosition, nearFarPlanes, depth + 1);
        return clampColors(new double[]{
                reflectionColor.getRed() / 255.0 * reflectivity,
                reflectionColor.getGreen() / 255.0 * reflectivity,
                reflectionColor.getGreen() / 255.0 * reflectivity});
    }
    public static Color getRefractionColor(Ray ray, Vector3D intersectionPosition, Vector3D normal, List<Object3D> objects, List<Light> lights, Vector3D cameraPosition, double[] nearFarPlanes, int depth, double refractiveIndex, double refractionCoefficient){
        double eta = refractiveIndex;
        double cosi = -Math.max(-1.0, Math.min(1.0, Vector3D.dotProduct(normal, Vector3D.normalize(ray.getDirection()))));
        if (cosi < 0) {
            cosi = -cosi;
            normal = Vector3D.scalarMultiplication(normal, -1);
            eta = 1 / refractiveIndex;
        }
        double k = 1 - eta * eta * (1 - cosi * cosi);
        Ray refractionRay;
        Vector3D offsetIntersectionPosition = Vector3D.add(intersectionPosition, Vector3D.scalarMultiplication(normal, -1e-4));

        if (k >= 0) {
            Vector3D refractionDirection = Vector3D.normalize(Vector3D.add(Vector3D.scalarMultiplication(ray.getDirection(), eta), Vector3D.scalarMultiplication(normal, eta * cosi - Math.sqrt(k))));
            refractionRay = new Ray(offsetIntersectionPosition, refractionDirection);
        } else{
            refractionRay = new Ray(offsetIntersectionPosition, Vector3D.ZERO());
        }
        Color refractionColor = Raytracer.traceRay(refractionRay, objects, lights, cameraPosition, nearFarPlanes, depth);

        return clampColors(new double[]{
                refractionColor.getRed() / 255.0 * refractionCoefficient,
                refractionColor.getGreen() / 255.0 * refractionCoefficient,
                refractionColor.getGreen() / 255.0 * refractionCoefficient});
    }
    private static double getSpotEffect(SpotLight light, Vector3D lightDirection) {
        Vector3D spotDirection = light.getDirection();
        double spotAngle = Math.toRadians(light.getCutoffAngle());
        double spotCosine = Math.cos(spotAngle);
        double spotDot = Vector3D.dotProduct(lightDirection, spotDirection);
        if (spotDot > spotCosine) {
            return Math.pow(spotDot, light.getIntensity());
        } else {
            return 0;
        }
    }


    private static Color clampColors(double[] colors){
        return new Color(
                (float) Math.clamp(colors[0], 0.0, 1.0),
                (float) Math.clamp(colors[1], 0.0, 1.0),
                (float) Math.clamp(colors[2], 0.0, 1.0)
        );
    }
}
