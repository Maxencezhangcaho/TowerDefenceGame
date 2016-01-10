/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.amunitions;

/**
 *
 * @author kruno2
 */
public class AmoCharge {
    
    public static final int MAX_AMO_UNITS = 16;
    private int currentAmoUnits;
    private int bulletDamage;

    public AmoCharge(boolean fullOfAmo,int damage){
        currentAmoUnits = MAX_AMO_UNITS;
        this.bulletDamage = damage;
    }
    
    
    
    /**
     * @return the currentAmoUnits
     */
    public int getCurrentAmoUnits() {
        return currentAmoUnits;
    }

    /**
     * @param currentAmoUnits the currentAmoUnits to set
     */
    public boolean fillWithAmo(int amoUnits) {
        while (amoUnits > 0){
            addAmoUnit();
            amoUnits--;
        }
        if (amoUnits == 0){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 
     * @return true if added, false if NOT added 
     */
    public boolean addAmoUnit(){
        if (currentAmoUnits < MAX_AMO_UNITS){
            currentAmoUnits++;
            return true;
        }
        return false;
    }
    
    public boolean isFull(){
        return currentAmoUnits == MAX_AMO_UNITS;
    }
    
    public boolean isEmpty(){
        return currentAmoUnits == 0;
    }

    /**
     * @return the bulletDamage
     */
    public int getBulletDamage() {
        return bulletDamage;
    }

    /**
     * @param bulletDamage the bulletDamage to set
     */
    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    /**
     * @param currentAmoUnits the currentAmoUnits to set
     */
    public void setCurrentAmoUnits(int currentAmoUnits) {
        this.currentAmoUnits = currentAmoUnits;
    }
    
    
}
