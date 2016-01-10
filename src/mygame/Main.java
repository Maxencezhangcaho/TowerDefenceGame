package mygame;

import mygame.appstates.GamePlaySceneState;
import mygame.appstates.CubeChaseState;
import mygame.appstates.AxisAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import mygame.appstates.TowerDefenceInputsState;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    public static float WIDTH_OF_SCREEN ;
    public static float HEIGTH_OF_SCREEN;

    private Node selectedTower = null;
    
    public static void main(String[] args) {
        
        Main app = new Main();
        AppSettings sets = new AppSettings(true);
        sets.setTitle("Tower Defence");
        sets.setSettingsDialogImage("Interface/Forest.png");
        app.setSettings(sets);
        WIDTH_OF_SCREEN = sets.getWidth();
        HEIGTH_OF_SCREEN = sets.getHeight();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
            // isključuje dev HUD
//        setDisplayFps(false);
 //       setDisplayStatView(false);
   
        flyCam.setMoveSpeed(40);
        flyCam.setRotationSpeed(10);
        Vector3f camLoca = new Vector3f(0f, 20f, 20f);
        Vector3f lookAtPoint = new Vector3f(0f, 0f, -20f);
        cam.setLocation(camLoca);
        cam.lookAtDirection(lookAtPoint.subtract(camLoca), Vector3f.UNIT_Y);
        
           /** A white, directional light source */ 
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun); 
        
        /** A white ambient light source. */ 
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient); 
        
    //    stateManager.attach(new AxisAppState());
        stateManager.attach(new TowerDefenceInputsState());
    //    stateManager.attach(new InputState());
        stateManager.attach(new GamePlaySceneState());
    //    stateManager.attach(new CubeChaseState());
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    /**
     * @return the selectedTower
     */
    public Node getSelectedTower() {
        return selectedTower;
    }

    /**
     * @param selectedTower the selectedTower to set
     */
    public void setSelectedTower(Node selectedTower) {
        this.selectedTower = selectedTower;
    }
    
    
    
    
    
   
}
