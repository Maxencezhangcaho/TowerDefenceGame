/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.test;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.TechniqueDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author kruno2
 */
public class MaterialTest extends SimpleApplication {
    
    DirectionalLight sun ;
    
    public static void main(String[] args){
        MaterialTest app = new MaterialTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        flyCam.setMoveSpeed(20);
       //unshadedSphere();
        setDisplayFps(false);
        setDisplayStatView(false);
        
        
        //   addSunToScene(); // add static sun to scene
        addChaseSunToScene(); // sun behind camera
                 
     //   viewPort.setBackgroundColor(ColorRGBA.LightGray);
       //lightingSphere();
       //shinySphere();
     //  transparentLightingSphere();
     //  semiTransparentWindow();
       hoverTank();
    }
    
    public void unshadedSphere(){
        Sphere ball = new Sphere(16, 16, 1f);
        Geometry ball_geo = new Geometry("ball", ball);
        ball_geo.move(-2f, 0f, 0f);
        ball_geo.rotate(FastMath.DEG_TO_RAD *-90, FastMath.DEG_TO_RAD *120, 0f);
        Material ball_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        ball_mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Monkey.png"));
        ball_mat.setTexture("LightMap", assetManager.loadTexture("Interface/Monkey_light.png"));

        ball_geo.setMaterial(ball_mat);
        rootNode.attachChild(ball_geo);
    }
    
    public void lightingSphere(){
        Sphere ball = new Sphere(16, 16, 1f);
        Geometry ball_geo = new Geometry("ball", ball);
        ball_geo.move(0f, 0f, -3f);
        ball_geo.rotate(FastMath.DEG_TO_RAD *-90, FastMath.DEG_TO_RAD *120, 0f);
        Material ball_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        ball_mat.setBoolean("UseMaterialColors", true);
        ball_mat.setColor("Diffuse", ColorRGBA.Blue);
      //  ball_mat.setColor("Ambient", ColorRGBA.Gray); // default value
        ball_geo.setMaterial(ball_mat);
        rootNode.attachChild(ball_geo);
    }
    
    public void shinySphere(){
        Sphere ball = new Sphere(16, 16, 1f);
        Geometry ball_geo = new Geometry("ball", ball);
        ball_geo.move(-2f, 0f, 0f);
        ball_geo.rotate(FastMath.DEG_TO_RAD *-90, FastMath.DEG_TO_RAD *120, 0f);
        Material ball_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        ball_mat.setBoolean("UseMaterialColors", true);
        ball_mat.setColor("Diffuse", ColorRGBA.Gray);
        ball_mat.setColor("Ambient", ColorRGBA.Cyan);
        ball_mat.setColor("Specular", ColorRGBA.White);
        ball_mat.setFloat("Shininess", 2f);
        ball_geo.setMaterial(ball_mat);
        rootNode.attachChild(ball_geo);
    }
    
     public void transparentLightingSphere(){
         Sphere ball = new Sphere(16, 16, 1f);
        Geometry ball_geo = new Geometry("ball", ball);
        ball_geo.move(0f, 0f, -3f);
        ball_geo.rotate(FastMath.DEG_TO_RAD *-90, FastMath.DEG_TO_RAD *120, 0f);
        Material ball_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        ball_mat.setTexture("DiffuseMap", assetManager.loadTexture("Interface/Monkey.png"));
        ball_mat.setTexture("AlphaMap", assetManager.loadTexture("Interface/Monkey_alpha.png"));
        ball_mat.setBoolean("UseMaterialColors", true);
        ball_mat.setColor("Diffuse", ColorRGBA.White);
        ball_mat.setColor("Ambient", ColorRGBA.Red);
        
          //      ball_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        ball_mat.getAdditionalRenderState().setAlphaTest(true);
        ball_mat.getAdditionalRenderState().setAlphaFallOff(0.9f);
        ball_geo.setQueueBucket(RenderQueue.Bucket.Transparent);
        ball_geo.setMaterial(ball_mat);
        rootNode.attachChild(ball_geo);
    }
    
    public void semiTransparentWindow(){
        Box window = new Box(Vector3f.ZERO, 1f, 1.4f, 0.01f);
        Geometry windowGeo = new Geometry("ball", window);
        windowGeo.move(1f, 0f, 0f);
      //  windowGeo.rotate(FastMath.DEG_TO_RAD *-90, FastMath.DEG_TO_RAD *120, 0f);
        Material windowMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        windowMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/mucha-window.png"));
        windowMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        windowGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
        windowGeo.setMaterial(windowMat);
        rootNode.attachChild(windowGeo);
    }
    
    public void hoverTank(){
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
        
        /* Drop shadows */
        final int SHADOWMAP_SIZE=1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bloom);
        
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true); // try true or false
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
        
        Node tank = (Node) assetManager.loadModel("Models/HoverTank/Tank.j3o");
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        
        TextureKey tankDiffuse = new TextureKey("Models/HoverTank/tank_diffuse.jpg", false);
        mat.setTexture("DiffuseMap", assetManager.loadAsset(tankDiffuse));
       
        TangentBinormalGenerator.generate(tank);
        
        TextureKey tankNormal = new TextureKey("Models/HoverTank/tank_normals.png", false);
        mat.setTexture("NormalMap", assetManager.loadAsset(tankNormal));
        
         TextureKey tankSpecular = new TextureKey("Models/HoverTank/tank_specular.jpg", false);
        mat.setTexture("SpecularMap", assetManager.loadAsset(tankSpecular));
        
        TextureKey tankGlow = new TextureKey("Models/HoverTank/tank_glow_map.jpg", false);
        mat.setTexture("GlowMap", assetManager.loadAsset(tankGlow));
        
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Gray);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setColor("GlowColor", ColorRGBA.White);
        mat.setFloat("Shininess", 100f);
        
        tank.setMaterial(mat);
        rootNode.attachChild(tank);
        tank.setShadowMode(ShadowMode.CastAndReceive);
        
        Box floorMesh = new Box(new Vector3f(-20, -3, -20), new Vector3f(20, -2, 20));
        floorMesh.scaleTextureCoordinates(new Vector2f(8, 8));
        Geometry floorGeo = new Geometry("floor", floorMesh);
        Material floorMat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
        Texture diff = assetManager.loadTexture("Textures/BrickWall/BrickWall_diffuse.jpg");
        diff.setWrap(Texture.WrapMode.Repeat);
        floorMat.setTexture("DiffuseMap", diff);
        TangentBinormalGenerator.generate(floorMesh);
        Texture normal = assetManager.loadTexture("Textures/BrickWall/BrickWall_normal.jpg");
        normal.setWrap(Texture.WrapMode.Repeat);
        floorMat.setTexture("NormalMap", normal);
        
//        floorMat.getTextureParam("NormalMap").getTextureValue().setWrap(Texture.WrapMode.Repeat);
//        floorMat.getTextureParam("DiffuseMap").getTextureValue().setWrap(Texture.WrapMode.Repeat);
        floorGeo.setMaterial(floorMat);
        floorGeo.setShadowMode(ShadowMode.Receive);
        rootNode.attachChild(floorGeo);
    }
    
    public void addSunToScene(){
         sun = new DirectionalLight();
        sun.setDirection((new Vector3f(1f,0f,-2f)));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
    }
    
    public void addChaseSunToScene(){
        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(cam.getDirection());
        rootNode.addLight(sun);
    }
    
    @Override
    public void simpleUpdate(float tpf) {    
  //  sun.setDirection(cam.getDirection());
    }
}
