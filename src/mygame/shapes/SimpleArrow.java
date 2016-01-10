/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.shapes;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author kruno
 */
public class SimpleArrow extends Node {
    
    private Geometry body;
    
    public SimpleArrow(Vector3f location, Vector3f velocity){
        setName("arrow");
        Box arrowBody = new Box(0.3f, 4f, 0.3f);
        body = new Geometry("bullet", arrowBody);
        this.attachChild(body);
        body.setLocalTranslation(0f, -4f, 0f);
        this.setLocalTranslation(location);
        SphereCollisionShape arrowHeadCollision = new SphereCollisionShape(0.5f);
        RigidBodyControl rigidBody = new RigidBodyControl(arrowHeadCollision, 1f);
        rigidBody.setLinearVelocity(velocity);
        addControl(rigidBody);
        addControl(new ArrowFacingControl());
        
    }

    /**
     * @return the body
     */
    public Geometry getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(Geometry body) {
        this.body = body;
    }
}
