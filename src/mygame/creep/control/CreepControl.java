/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.creep.control;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.Iterator;
import mygame.appstates.GamePlaySceneState;

/**
 *
 * @author kruno2
 */
public class CreepControl extends AbstractControl {

    private GamePlaySceneState game;
    
    public CreepControl(GamePlaySceneState game){
        this.game = game;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        //discreteMotionVersion();
        floatMotionVersion(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    private void discreteMotionVersion(){
        // move creeps
        if (game.getCreepElapsedPause() == 0){
                Spatial s = spatial;
                if ((Integer)s.getUserData(game.HEALTH_KEY) > 0){
                    s.move(Vector3f.UNIT_Z.mult(0.5f));
                     // end line check
                    if (s.getLocalTranslation().getZ() >= 0f){
             //           it.remove();
                        game.getCreeps().remove(s);
                        game.getCreepPivot().detachChild(s);
                        game.setHealth(game.getHealth()- game.getCreepAttackDamage());
                        System.out.println("health:"+game.getHealth());
                        if (game.getHealth() <= 0){
                            System.out.println("Creep Won!");
                        }
                     }
                } else {
                    game.getCreeps().remove(s);
                    game.getCreepPivot().detachChild(s);
                    game.setBudget(game.getBudget() + 50);
                    
                }
     //       }
           
        }
    }
    
    private void floatMotionVersion(float tpf){
        // move creeps
        Spatial s = spatial;
        if ((Integer)s.getUserData(game.HEALTH_KEY) > 0){
            if (s.getLocalTranslation().getZ() < 0f){
                s.move(Vector3f.UNIT_Z.mult(0.5f*tpf));
            }
             // end line check
            if (s.getLocalTranslation().getZ() >= 0f){
                AnimChannel channel = s.getControl(AnimControl.class).getChannel(0);
                if (GamePlaySceneState.ANIM_CREEP_RUN.equals(channel.getAnimationName())){
                    channel.setAnim(GamePlaySceneState.ANIM_CREEP_ATTACK);
                } else if (GamePlaySceneState.ANIM_CREEP_ATTACK.equals(channel.getAnimationName())){
                    if ((channel.getAnimMaxTime()-channel.getTime()) < channel.getAnimMaxTime() * 0.08 ){
                        game.getCreeps().remove(s);
                        game.getCreepPivot().detachChild(s);
                        game.setHealth(game.getHealth()- game.getCreepAttackDamage());
                        System.out.println("health:"+game.getHealth());
                        if (game.getHealth() <= 0){
                            System.out.println("Creep Won!");
                        }
                    }
                }
            }
        } else {
            game.getCreeps().remove(s);
            game.getCreepPivot().detachChild(s);
            game.setBudget(game.getBudget() + 50);
        }
    }
    
}
