/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.tower.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.LinkedList;
import java.util.List;
import mygame.entity.amunitions.AmoCharge;

/**
 *
 * @author kruno2
 */
public class ExtendedTowerUserData extends AbstractControl {

    private List<AmoCharge> charges = new LinkedList<AmoCharge>();
    
    
    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * @return the charges
     */
    public List<AmoCharge> getCharges() {
        return charges;
    }

    /**
     * @param charges the charges to set
     */
    public void setCharges(List<AmoCharge> charges) {
        this.charges = charges;
    }
    
}
