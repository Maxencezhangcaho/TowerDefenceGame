/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author kruno2
 */
public class CubeChaserControl extends AbstractControl {

    private static float JUMP_LIMIT = 4f;
    private float jumpRich = 0f;
    private boolean contactOn = false;
    private Vector3f direction;

    
    @Override
    protected void controlUpdate(float tpf) {
    //    spatial.rotate(tpf,tpf,tpf);
        if (contactOn) {
            jumpRich += tpf*10;
            spatial.move(direction.mult(tpf*10));
            System.out.println("jump="+tpf*10+", jumpSum="+jumpRich+",length="+direction.length());
            if (jumpRich > JUMP_LIMIT){
                jumpRich = 0f;
                contactOn = false;
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }


    public float getJumpRich(){
        return jumpRich;
    }
    
    public void setJumpRich(float jump){
        this.jumpRich = jump;
    }
    
    public boolean getContactOn(){
        return contactOn;
    }
    
    public void setContactOn(boolean contactOn){
        this.contactOn = contactOn;
    }

    /**
     * @return the direction
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}
