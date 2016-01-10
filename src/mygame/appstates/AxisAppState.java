/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;

/**
 *
 * @author kruno2
 */
public class AxisAppState  extends AbstractAppState {
    
    public static final String PIVOT_AXES_ORIGIN = "axesOrigin";

        
    private SimpleApplication app;
    private Node rootNode;
    private AssetManager assetManager;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.app = (SimpleApplication) app;
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        
        Arrow arx = new Arrow(Vector3f.UNIT_X);
        Geometry garx = new Geometry("axe x", arx);
        garx.scale(20f);
        Material matx = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matx.setColor("Color", ColorRGBA.Blue);
        garx.setMaterial(matx);

        Arrow ary = new Arrow(Vector3f.UNIT_Y);
        Arrow arz = new Arrow(Vector3f.UNIT_Z);
        
        
        Geometry gary = new Geometry("axe y", ary);
        Geometry garz = new Geometry("axe z", arz);
        gary.scale(20f);
        garz.scale(20f);
        
        Material maty = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material matz = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        maty.setColor("Color", ColorRGBA.Red);
        matz.setColor("Color", ColorRGBA.Green);
        
        gary.setMaterial(maty);
        garz.setMaterial(matz);
        
        Node axesOrigin = new Node(PIVOT_AXES_ORIGIN);
        axesOrigin.attachChild(garx);
        axesOrigin.attachChild(gary);
        axesOrigin.attachChild(garz);
        rootNode.attachChild(axesOrigin);
    }
}
