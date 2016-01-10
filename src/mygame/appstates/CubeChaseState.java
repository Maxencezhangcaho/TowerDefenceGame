/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import mygame.CubeChaserControl;
import mygame.Main;

/**
 *
 * @author kruno2
 */
public class CubeChaseState extends AbstractAppState {
    
    private Main app;
    private Ray ray = new Ray();
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private Geometry geom;
    private static Box basicBoxMesh = new Box(1, 1, 1);
    
       @Override
       public void update(float tpf) {
          
            CollisionResults results = new CollisionResults();
            ray.setOrigin(cam.getLocation());
            ray.setDirection(cam.getDirection());
            rootNode.collideWith(ray, results);
            if (results.size() > 0){
                Geometry target = results.getClosestCollision().getGeometry();
                CubeChaserControl cubeControl = target.getControl(CubeChaserControl.class);
                if (cubeControl != null && cubeControl.getContactOn() == false){
                    if (cam.getLocation().distance(target.getLocalTranslation()) < 10 ){
                        cubeControl.setContactOn(true);
                        cubeControl.setDirection(new Vector3f(cam.getDirection()));
                        cubeControl.setJumpRich(cubeControl.getJumpRich()+ tpf*20);
                   //     target.move(cam.getDirection().multLocal(tpf*20));
                    //    target.move(cam.getDirection().add(2f, 0f, 0f)); // unit move in cam direction
                    }
                }
            }
    }

       @Override
    public void cleanup() {
    }
       @Override
       public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.app = (Main) app;
        this.assetManager = this.app.getAssetManager();
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        
        geom = newBox("box1", new Vector3f(0f, 1f, 3f), ColorRGBA.Blue);
        geom.addControl(new CubeChaserControl());
        rootNode.attachChild(geom);
        rootNode.attachChild(newBox("box2", new Vector3f(5f, 1f, 3f), ColorRGBA.Brown));
        
        attachCenterCross();
    }
       
       private Geometry newBox(String name,Vector3f location,ColorRGBA color){
        
        Geometry geom = new Geometry(name, basicBoxMesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.setLocalTranslation(location);
        //rootNode.attachChild(geom);
        return geom;
    }
       
        private void attachCenterCross(){
        
        Geometry cross = newBox("centerPoint", Vector3f.ZERO, ColorRGBA.White);
        cross.setLocalTranslation(app.WIDTH_OF_SCREEN/2, app.HEIGTH_OF_SCREEN/2, 0f);
        cross.scale(4);
        app.getGuiNode().attachChild(cross);
    }
}
