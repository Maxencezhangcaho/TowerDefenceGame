/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import mygame.Main;
import mygame.entity.amunitions.AmoCharge;
import mygame.tower.controls.ExtendedTowerUserData;

/**
 *
 * @author kruno2
 */
public class TowerDefenceInputsState extends AbstractAppState {
    
    private static final Trigger BY_AMO_CHARGE_TRIGGER = new KeyTrigger(KeyInput.KEY_SPACE);
    private static final Trigger PICK__TRIGGER = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private static final String MAPPING_FOR_PICK_TOWER = "pick tower";
    private static final String MAPPING_FOR_BY_AMO = "byamo";
    
    private Main app;
    private Node rootNode;
    private Camera cam;
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        
       super.initialize(stateManager,app);
       this.app = (Main) app;
       this.app.getFlyByCamera().setDragToRotate(true);
       this.app.getInputManager().setCursorVisible(true);
       this.cam = this.app.getCamera();
       this.rootNode = this.app.getRootNode();
       
        this.app.getInputManager().addMapping(MAPPING_FOR_BY_AMO, BY_AMO_CHARGE_TRIGGER);
        this.app.getInputManager().addMapping(MAPPING_FOR_PICK_TOWER, PICK__TRIGGER);
        this.app.getInputManager().addListener(analogListener, new String[] {MAPPING_FOR_PICK_TOWER});
        this.app.getInputManager().addListener(actionListener, new String[] {MAPPING_FOR_BY_AMO});
        
    }
    
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(MAPPING_FOR_BY_AMO) && !isPressed){
                app.getSelectedTower().getChild("tower").getControl(ExtendedTowerUserData.class).getCharges().add(new AmoCharge(true,15));
                System.out.println("Added Charges (15U) to tower:"+ app.getSelectedTower().getName());
                
            }
        }
    };
            
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            if (name.equals(MAPPING_FOR_PICK_TOWER)){
                
                Vector2f vec = app.getInputManager().getCursorPosition();
                Vector3f clickPoss = cam.getWorldCoordinates(new Vector2f(vec), 0f);
                Vector3f direction = cam.getWorldCoordinates(new Vector2f(vec), 1f).subtract(clickPoss);
                
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(clickPoss, direction);
                rootNode.collideWith(ray, results);
                Geometry target = null;
                if (results != null && results.getClosestCollision() != null){
                    target = results.getClosestCollision().getGeometry();
                    System.out.println("selected:"+target.getName()+", parent:"+target.getParent().getName());
                    if ("tower".equals(target.getParent().getName())){
                        app.setSelectedTower(target.getParent().getParent());
                    } else {
                        app.setSelectedTower(target.getParent());
                    }
                    System.out.println("Selected tower: "+ app.getSelectedTower().getName());
                }
                
            }
        }
    };
}
