package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.Light;
import edu.up.isgc.cg.raytracer.objects.*;
import edu.up.isgc.cg.raytracer.tools.ColorCalculator;
import edu.up.isgc.cg.raytracer.tools.ScenesCreator;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Raytracer {

    private static final double falloffExponent = 0.2;
    private static final int MAX_RECURSION_DEPTH = 4;


    public static void main(String[] args) {
        System.out.println(new Date());

        Scene scene = ScenesCreator.render1();

        BufferedImage image = raytrace(scene);
        File outputImage = new File("image.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new Date());
    }

    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        double[] nearFarPlanes = mainCamera.getNearFarPlanes();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        List<Object3D> objects = scene.getObjects();
        List<Light> lights = scene.getLights();
        Vector3D[][] posRaytrace = mainCamera.calculatePositionsToRay();
        Vector3D pos = mainCamera.getPosition();
        double cameraZ = pos.getZ();

        // Parallelize
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < posRaytrace.length; i++) {
            for (int j = 0; j < posRaytrace[i].length; j++) {
                Runnable runnable = parallelRayTrace(i, j, posRaytrace, pos, mainCamera, objects, lights, cameraZ, nearFarPlanes, image);
                executorService.execute(runnable);
            }
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!executorService.isTerminated()) {
                System.err.println("cancel non-finished");
            }
        }
        executorService.shutdownNow();

        return image;
    }

    public static Runnable parallelRayTrace(int i, int j, Vector3D[][] posRaytrace, Vector3D pos, Camera mainCamera, List<Object3D> objects, List<Light> lights, double cameraZ, double[] nearFarPlanes, BufferedImage image) {
        Runnable aRunnable = () -> {
            double x = posRaytrace[i][j].getX() + pos.getX();
            double y = posRaytrace[i][j].getY() + pos.getY();
            double z = posRaytrace[i][j].getZ() + pos.getZ();

            Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
            Color pixelColor = traceRay(ray,objects,lights,mainCamera.getPosition(),new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]}, 0);

            setOurRGB(image, i, j, pixelColor);
        };
        return aRunnable;
    }

    public static Color traceRay(Ray ray, List<Object3D> objects, List<Light> lights, Vector3D cameraPosition, double[] nearFarPlanes, int depth){
        if (depth > MAX_RECURSION_DEPTH) {
            return Color.BLACK;  // Base case: maximum recursion depth reached
        }
        Intersection closestIntersection = raycast(ray, objects, null,
                nearFarPlanes);
        if(closestIntersection == null){
            return Color.black;
        }

        Color pixelColor = Color.BLACK;
        if (closestIntersection != null) {
            Color objColor = closestIntersection.getObject().getColor();
            Vector3D intersectionPosition = closestIntersection.getPosition();
            Vector3D normal = closestIntersection.getNormal();
            Vector3D viewDirection = Vector3D.normalize(Vector3D.substract(cameraPosition, intersectionPosition));
            double ambientCoefficient = closestIntersection.getObject().getAmbientCoefficient();
            double diffuseCoefficient = closestIntersection.getObject().getDiffuseCoefficient();
            double specularCoefficient = closestIntersection.getObject().getSpecularCoefficient();
            double shininess = closestIntersection.getObject().getShininess();
            double reflectivity = closestIntersection.getObject().getReflectivity();
            double refractiveIndex = closestIntersection.getObject().getRefractiveIndex();
            double refractionCoefficient = closestIntersection.getObject().getRefractionCoefficient();


            pixelColor = ColorCalculator.getAmbientColor(objColor, ambientCoefficient);


            for (Light light : lights) {
                Vector3D lightDirection = Vector3D.normalize(Vector3D.substract(light.getPosition(), closestIntersection.getPosition()));
                Ray shadowRay = new Ray(closestIntersection.getPosition(), lightDirection);
                double lightDistance = Vector3D.magnitude(lightDirection);
                Intersection shadowIntersection = raycast(shadowRay, objects, closestIntersection.getObject(), new double[]{0, lightDistance});
                if (shadowIntersection == null) {
                    Color lightColor = light.getColor();
                    double falloff = Math.pow(lightDistance, falloffExponent);
                    double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};

                    Color diffuse = ColorCalculator.getDiffuseColor(closestIntersection,objColor,diffuseCoefficient,light, lightDirection, lightColors,falloff);
                    Color specularColor = ColorCalculator.getSpecularColor(normal,viewDirection,lightDirection,specularCoefficient,light,lightColors,falloff,shininess);

                    pixelColor = addColor(pixelColor, addColor(diffuse, specularColor));
                }
            }
            // Reflective component
            if (reflectivity > 0) {
                Color reflectionColor =  ColorCalculator.getReflectionColor(ray,intersectionPosition,normal,objects,lights,cameraPosition,nearFarPlanes,depth,reflectivity);
                pixelColor = addColor(pixelColor,reflectionColor);
            }
            // Refractive component
            if (refractionCoefficient > 0) {
                Color refractionColor = ColorCalculator.getRefractionColor(ray,intersectionPosition,normal,objects,lights,cameraPosition,nearFarPlanes,depth,refractiveIndex,refractionCoefficient);
                if(refractionColor != null){
                    pixelColor = addColor(pixelColor, refractionColor);
                }
            }
        }

        return pixelColor;
    }

    public static synchronized void setOurRGB(BufferedImage image, int i, int j, Color pixelColor) {
        image.setRGB(i, j, pixelColor.getRGB());
    }

    public static Color addColor(Color original, Color otherColor) {
        float red = (float) Math.clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) Math.clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) Math.clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }

    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster, double[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (int i = 0; i < objects.size(); i++) {
            Object3D currObj = objects.get(i);
            if (caster == null || !currObj.equals(caster)) {
                Intersection intersection = currObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    double intersectionZ = intersection.getPosition().getZ();

                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersectionZ >= clippingPlanes[0] && intersectionZ <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }
}
