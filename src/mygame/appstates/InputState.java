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
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 *
 * @author kruno2
 */
public class InputState extends AbstractAppState {
    
    private static final Trigger COLOR_TRIGGER = new KeyTrigger(KeyInput.KEY_SPACE);
    private static final Trigger COLOR_TRIGGER2 = new KeyTrigger(KeyInput.KEY_C);
    private static final Trigger ROTATE__TRIGGER = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private static final String MAPPING_FOR_COLOR = "toggle color";
    private static final String MAPPING_FOR_ROTATE = "rotate";
    
    private SimpleApplication app;
    private Node rootNode;
    private Camera cam;
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager,app);
       
        this.app = (SimpleApplication) app;
        this.app.getInputManager().addMapping(MAPPING_FOR_COLOR, COLOR_TRIGGER,COLOR_TRIGGER2);
        this.app.getInputManager().addMapping(MAPPING_FOR_ROTATE, ROTATE__TRIGGER);
        this.app.getInputManager().addListener(analogListener, new String[] {MAPPING_FOR_ROTATE});
        this.app.getInputManager().addListener(actionListener, new String[] {MAPPING_FOR_COLOR});
        
    }
    
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(MAPPING_FOR_COLOR) && !isPressed){
                 CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                rootNode.collideWith(ray, results);
                Geometry target = results.getClosestCollision().getGeometry(); 
                target.getMaterial().setColor("Color", ColorRGBA.randomColor());
            }
        }
    };
            
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            if (name.equals(MAPPING_FOR_ROTATE)){
                
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                rootNode.collideWith(ray, results);
                Geometry target = results.getClosestCollision().getGeometry();
                System.out.println("we: "+target.getName());
                if (target.getName().equals("box1")){
                    target.rotate(0, value, 0);
                } else if (target.getName().equals("box2")){
                    target.rotate(0,-value, 0);
                } 
            }
        }
    };
}
