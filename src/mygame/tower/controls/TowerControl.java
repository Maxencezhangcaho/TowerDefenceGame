/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.tower.controls;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Line;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import mygame.appstates.GamePlaySceneState;
import mygame.entity.amunitions.AmoCharge;

/**
 *
 * @author kruno2
 */
public class TowerControl extends AbstractControl {
    
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.

    public static float shootInterval = 1;
    
    private float elapsedTime = 0f;
    private GamePlaySceneState game;
    private List<Spatial> reachable = new ArrayList<Spatial>();
    private ExtendedTowerUserData extendedUserData;
    private Node beamNode = null;

    public TowerControl(){}
    
    public TowerControl(GamePlaySceneState game){
        this.game = game;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        
        extendedUserData = spatial.getControl(ExtendedTowerUserData.class);
        beamNode = (Node) spatial.getParent().getChild(game.TOWER_BEAM_NODE);
        
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
        elapsedTime += tpf;
        if (elapsedTime > 0){
            if (elapsedTime > shootInterval){
                elapsedTime = 0f;
            } if (elapsedTime > shootInterval/2){
                beamNode.detachAllChildren();
            }
        }
        if (elapsedTime == 0){
            reachable.clear();
            
            if (!extendedUserData.getCharges().isEmpty()){
                if (extendedUserData.getCharges().get(0).isEmpty()){
                    ((LinkedList)extendedUserData.getCharges()).removeFirst();
             //       List<AmoCharge> rest = new extendedUserData.getCharges().subList(1, extendedUserData.getCharges().size());
             //       extendedUserData.getCharges().clear();
             //       extendedUserData.getCharges().addAll(rest);
                    System.out.println("AmoCharge change");
                }
                if (!extendedUserData.getCharges().isEmpty() && !extendedUserData.getCharges().get(0).isEmpty()){
                    int bulletsLeft = extendedUserData.getCharges().get(0).getCurrentAmoUnits();
                    for (Spatial creep : game.getCreeps()){
                        if (beamNode.getWorldTranslation().distance(creep.getWorldTranslation()) < 12){
                            reachable.add(creep);
                            if (bulletsLeft > 0){
                                Line shootLine = new Line(new Vector3f().ZERO, creep.getLocalTranslation().subtract(beamNode.getLocalTranslation()).subtract(beamNode.getParent().getLocalTranslation()));
                                Geometry line_geo = new Geometry("shootLine", shootLine);
                                Material line_mat = new Material(game.getAssetManager(),  "Common/MatDefs/Misc/Unshaded.j3md");
                                line_mat.setColor("Color", ColorRGBA.White); 
                                line_geo.setMaterial(line_mat);
                                beamNode.attachChild(line_geo);
                                bulletsLeft--;
                                creep.setUserData(game.HEALTH_KEY,((Integer) creep.getUserData(game.HEALTH_KEY)) - extendedUserData.getCharges().get(0).getBulletDamage());
                            }
                        }
                    }
                    extendedUserData.getCharges().get(0).setCurrentAmoUnits(bulletsLeft);
                }
            }
        } 
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        TowerControl control = new TowerControl();
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }

    public int getSpatialIndex(){
        return spatial.getUserData(game.INDEX_KEY);
    }
    
    public List<AmoCharge> getSpatialAmoCharges(){
        return spatial.getControl(ExtendedTowerUserData.class).getCharges();
    }
}
