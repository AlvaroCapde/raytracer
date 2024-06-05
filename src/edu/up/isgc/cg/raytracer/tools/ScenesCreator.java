package edu.up.isgc.cg.raytracer.tools;

import edu.up.isgc.cg.raytracer.Scene;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.lights.DirectionalLight;
import edu.up.isgc.cg.raytracer.lights.PointLight;
import edu.up.isgc.cg.raytracer.lights.SpotLight;
import edu.up.isgc.cg.raytracer.objects.*;

import java.awt.*;

public class ScenesCreator {

    public static Scene scene1(){
        Scene scene01 = new Scene();
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 60, 60, 800, 800, 0.6, 50.0));
//        scene01.addLight(new PointLight(new Vector3D(0, 5, 0), Color.WHITE, 1));
        scene01.addLight(new SpotLight(new Vector3D(0, 10, 0), Color.WHITE, 1,new Vector3D(0,-1,0),10));
        Sphere sphere = new Sphere(new Vector3D(-1, -2, 0), 1, Color.WHITE);
        Model3D apple = OBJReader.getModel3D("Apple.obj", new Vector3D(-1, -2.5, 4), Color.WHITE);
        apple.setRefractionCoefficient(1.0);
        apple.setRefractiveIndex(1.4);
        sphere.setReflectivity(0);
        sphere.setRefractiveIndex(1.0001);
        sphere.setRefractionCoefficient(1.0);
        scene01.addObject(apple);
//        Sphere sphere2 = new Sphere(new Vector3D(4.0, -1.5, 1), 1, Color.RED);
//        sphere2.setReflectivity(0);
//        sphere2.setRefractiveIndex(1);
//        sphere2.setRefractionCoefficient(0.6);
//        scene01.addObject(sphere2);

        Plane plane = new Plane(-2.5, Color.GRAY);
        plane.setShininess(1.0);
        plane.setSpecularCoefficient(0);
        plane.setDiffuseCoefficient(0.5);
        plane.setReflectivity(0.5);
        scene01.addObject(plane);
        Model3D teapot = OBJReader.getModel3D("car3.obj", new Vector3D(-1, -2.5, 4), Color.RED);
//        teapot.setReflectivity(1);
//        teapot.setRefractiveIndex(0);
//        teapot.setRefractionCoefficient(1);
        scene01.addObject(teapot);

        return scene01;
    }
    public static Scene render1(){
        Scene scene = new Scene();
        scene.setCamera(new Camera(new Vector3D(0, 0, -4), 60, 60, 4096, 2160, 0.6, 50.0));
        scene.addLight(new PointLight(new Vector3D(3, 4, -3), Color.WHITE, 1));
//        Model3D apple = OBJReader.getModel3D("Apple.obj", new Vector3D(-3, -2.5, 0), Color.RED);
//        Model3D apple2 = OBJReader.getModel3D("Apple.obj", new Vector3D(3, -2.5, 0), Color.RED);
        Model3D fork1 = OBJReader.getModel3D("fork.obj", new Vector3D(-3, 0, 4), Color.GRAY);
//        Model3D fork2 = OBJReader.getModel3D("fork.obj", new Vector3D(0, 0, 4), Color.GRAY);
//        Model3D fork3 = OBJReader.getModel3D("fork.obj", new Vector3D(3, 0, 4), Color.GRAY);
//        apple.setReflectivity(1);
//        apple2.setRefractiveIndex(1.2);
//        apple2.setRefractionCoefficient(1.0);
//        scene.addObject(apple2);
//        scene.addObject(apple);
//
//        fork1.setReflectivity(1);
//        fork2.setReflectivity(1);
//        fork3.setReflectivity(1);

        Plane plane = new Plane(-2.5, Color.GRAY);
//        plane.setShininess(1.0);
//        plane.setSpecularCoefficient(0);
//        plane.setDiffuseCoefficient(0.5);
//        plane.setReflectivity(0.5);
        scene.addObject(plane);

        scene.addObject(fork1);
//        scene.addObject(fork2);
//        scene.addObject(fork3);

        return scene;
    }

    public static Scene scene2(){
        Scene scene02 = new Scene();
        scene02.setCamera(new Camera(new Vector3D(0, 0, -4), 60, 60, 800, 800, 0.6, 50.0));
        scene02.addLight(new PointLight(new Vector3D(0.0, 1.0, 0.0),Color.WHITE, 0.8));
        scene02.addObject(new Plane(-2.5,Color.CYAN));
        /*scene02.addLight(new DirectionalLight(new Vector3D(0.0, 0.0, 1.0), Color.WHITE, 0.8));
        scene02.addLight(new DirectionalLight(new Vector3D(0.0, -0.1, 0.1), Color.WHITE, 0.2));
        scene02.addLight(new DirectionalLight(new Vector3D(-0.2, -0.1, 0.0), Color.WHITE, 0.2));*/
        scene02.addObject(new Sphere(new Vector3D(0.0, 1.0, 5.0), 0.5, Color.RED));
        scene02.addObject(new Sphere(new Vector3D(0.5, 1.0, 4.5), 0.25, new Color(200, 255, 0)));
        scene02.addObject(new Sphere(new Vector3D(0.35, 1.0, 4.5), 0.3, Color.BLUE));
        scene02.addObject(new Sphere(new Vector3D(4.85, 1.0, 4.5), 0.3, Color.PINK));
        scene02.addObject(new Sphere(new Vector3D(2.85, 1.0, 304.5), 0.5, Color.BLUE));
        scene02.addObject(OBJReader.getModel3D("Cube.obj", new Vector3D(0f, -2.5, 1.0), Color.WHITE));
        scene02.addObject(OBJReader.getModel3D("CubeQuad.obj", new Vector3D(-3.0, -2.5, 3.0), Color.GREEN));
        scene02.addObject(OBJReader.getModel3D("SmallTeapot.obj", new Vector3D(2.0, -1.0, 1.5), Color.BLUE));
        scene02.addObject(OBJReader.getModel3D("Ring.obj", new Vector3D(2.0, -1.0, 1.5), Color.BLUE));
        return scene02;
    }

    public static Scene scene03(){
        Scene scene = new Scene();
        scene.setCamera(new Camera(new Vector3D(0, 0, -4), 60, 60,800, 800, 0.6, 50.0));
//        scene.addLight(new PointLight(new Vector3D(-2, 3, 0), Color.YELLOW, 1));
        scene.addLight(new PointLight(new Vector3D(1, 0, -4), Color.WHITE, 1));


        Plane plane = new Plane(-2.5, Color.WHITE);
        plane.setShininess(1.0);
        plane.setSpecularCoefficient(0);
        plane.setDiffuseCoefficient(0.5);
//        plane.setReflectivity(0.5);
        scene.addObject(plane);
        Model3D lamp1 = OBJReader.getModel3D("lamp_scaled.obj", new Vector3D(0, -1.5, 0), Color.yellow);
//        Model3D lamp2 = OBJReader.getModel3D("new_skyscraper.obj", new Vector3D(2, -1.5, 1), Color.WHITE);
//        lamp1.setRefractionCoefficient(0.9);
//        lamp2.setReflectivity(1.0);
//        lamp1.setRefractiveIndex(1);
//        scene.addObject(lamp1);
        scene.addObject(lamp1);
//        Model3D ring = OBJReader.getModel3D("Ring.obj", new Vector3D(0, -1, 0), Color.WHITE);
//        ring.setRefractiveIndex(1);
//        ring.setRefractionCoefficient(0.5);

//        Sphere sphere = new Sphere(new Vector3D(0, -2, -1), 0.5, Color.GRAY);
//        sphere.setReflectivity(0);

        Model3D cube = OBJReader.getModel3D("SmallTeapot.obj", new Vector3D(1, -2.5, 2), Color.RED);
        cube.setReflectivity(1.0);
        scene.addObject(cube);


        return scene;
    }
}
