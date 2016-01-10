/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appstates;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mygame.creep.control.CreepControl;
import mygame.creep.control.CreepPositionControl;
import mygame.tower.controls.ExtendedTowerUserData;
import mygame.tower.controls.TowerControl;

/**
 *
 * @author kruno2
 */
public class GamePlaySceneState extends AbstractAppState {
    
    private SimpleApplication app;
    private Node rootNode;
    private AssetManager assetManager;
    
    public static final String PIVOT_PLAYER_NODE = "playerNode";
    public static final String PIVOT_TOWER_NODE = "towerNode";
    public static final String PIVOT_CREEP_NODE = "creepNode";
    public static final String PIVOT_GROUND_NODE = "groundNode";
    public static final String TOWER_BEAM_NODE = "beamNode";
    public static int INITIAL_AMO_CHARGES = 20;
    public static final int INITIAL_PLAYER_HEALTH = 200;
    
    public static final String INDEX_KEY = "index";
    public static final String HEALTH_KEY = "health";
    
    public static final String ANIM_CREEP_RUN = "Run";
    public static final String ANIM_CREEP_ATTACK = "Strike";
    
    private float creepCreationPauseTime = 1;
    private float creepElapsedPause = 0;
    private int creepLastIndex = 0;
    private Node creepPivot;
    private int creepInitialMaxHealth = 200;

    //player variables
    private int level;
    private int score;
    private int health = INITIAL_PLAYER_HEALTH;
    private int budget;
    
    //creeps
    private List<Spatial> creeps = new ArrayList<Spatial>();
    private int creepAttackDamage = 5;
    
    //effects
    private int smokeEmmitersCount = 0;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.app = (SimpleApplication) app;
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        
        Node groundNode = new Node(PIVOT_GROUND_NODE);
        
        Box floorMesh = new Box(new Vector3f(-20f,-0.5f,-20f), new Vector3f(20f, 0.5f, 20f));
        Geometry ground = new Geometry("Colored Box", floorMesh); 
        floorMesh.scaleTextureCoordinates(new Vector2f(8f, 8f));
        Material floorMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        floorMat.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall_normal.jpg"));
        floorMat.getTextureParam("DiffuseMap").getTextureValue().setWrap(Texture.WrapMode.Repeat);
        floorMat.getTextureParam("NormalMap").getTextureValue().setWrap(Texture.WrapMode.Repeat);
        ground.setMaterial(floorMat); 
        ground.move(0f,-0.5f, -17f);
        groundNode.attachChild(ground);

         rootNode.attachChild(groundNode);
         
         Node playerBase = new Node(PIVOT_PLAYER_NODE);
         Node gBase = creatBase("Ground");
         gBase.move(-3f,1f, 3.5f);
         gBase.getLocalRotation().fromAngleAxis(FastMath.DEG_TO_RAD * 90, Vector3f.UNIT_Y);
         playerBase.attachChild(gBase);

         rootNode.attachChild(playerBase);
         
         Node towerPivot1 = new Node(PIVOT_TOWER_NODE+"_1");
         Node towerPivot2 = new Node(PIVOT_TOWER_NODE+"_2");
         Node towerPivot3 = new Node(PIVOT_TOWER_NODE+"_3");
         Node towerPivot4 = new Node(PIVOT_TOWER_NODE+"_4");
         
       // Geometry tower1_geo = createTower("tower", ColorRGBA.Green);
         Node tower1_geo = creatDonjoneTower("tower");
         tower1_geo.setName("tower");
         towerPivot1.move(-8f,2.5f, -5f);
         towerPivot1.attachChild(tower1_geo);
         initializeTowerStats(1, tower1_geo);
         tower1_geo.addControl(new TowerControl(this));
         Node beamNode1 = new Node(TOWER_BEAM_NODE);
         towerPivot1.attachChild(beamNode1);
         beamNode1.move(Vector3f.UNIT_Y.mult(3));
         
        //Geometry tower2_geo = createTower("tower", ColorRGBA.Blue);
        Node tower2_geo = creatDonjoneTower("tower");
        towerPivot2.move(8f,2.5f, -5f);
        towerPivot2.attachChild(tower2_geo);
        initializeTowerStats(2, tower2_geo);
        tower2_geo.addControl(new TowerControl(this));
        Node beamNode2 = new Node(TOWER_BEAM_NODE);
        towerPivot2.attachChild(beamNode2);
         beamNode2.move(Vector3f.UNIT_Y.mult(3));
         
       // Geometry tower3_geo = createTower("tower", ColorRGBA.Green);
        Node tower3_geo = creatDonjoneTower("tower");
         towerPivot3.move(-8f,2.5f, -15f);
         towerPivot3.attachChild(tower3_geo);
         initializeTowerStats(3, tower3_geo);
         tower3_geo.addControl(new TowerControl(this));
         Node beamNode3 = new Node(TOWER_BEAM_NODE);
         towerPivot3.attachChild(beamNode3);
         beamNode3.move(Vector3f.UNIT_Y.mult(3));
         
        //Geometry tower4_geo = createTower("tower", ColorRGBA.Blue);
        Node tower4_geo = creatDonjoneTower("tower");
         towerPivot4.move(8f,2.5f, -15f);
        towerPivot4.attachChild(tower4_geo);
        initializeTowerStats(4, tower4_geo);
        tower4_geo.addControl(new TowerControl(this));
        Node beamNode4 = new Node(TOWER_BEAM_NODE);
        towerPivot4.attachChild(beamNode4);
        beamNode4.move(Vector3f.UNIT_Y.mult(3));
        System.out.println("beamNode4.y="+beamNode4.getLocalTranslation().getY());
        
         Box spotBox = new Box(0.1f,0.1f,0.1f); 
        Geometry spot_geo = new Geometry("spot", spotBox); 
        Material spot_mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"); 
        spot_mat.setColor("Color", ColorRGBA.White); 
        spot_geo.setMaterial(spot_mat); 
        towerPivot4.attachChild(spot_geo);
         spot_geo.move(Vector3f.UNIT_Y.mult(3));
         System.out.println("spot_geo.y="+spot_geo.getLocalTranslation().getY());
         setCreepPivot(new Node(PIVOT_CREEP_NODE));
         rootNode.attachChild(getCreepPivot());

        creepDistributionMaker(7); // initialize creep list
        creeps.get(0).addControl(new CreepPositionControl());
       
        rootNode.attachChild(towerPivot1);
        rootNode.attachChild(towerPivot2);
        rootNode.attachChild(towerPivot3);
        rootNode.attachChild(towerPivot4);
    }
    
    @Override 
    public void update(float tpf){
        setCreepElapsedPause(getCreepElapsedPause() + tpf);
        if (getCreepElapsedPause() > creepCreationPauseTime){
            setCreepElapsedPause(0); //reset
            creepDistributionMaker(1);
        }
        
        int wound = INITIAL_PLAYER_HEALTH - health;
        int newCount = new Float(wound/10).intValue();
        
        if (newCount > smokeEmmitersCount){
            smokeEmmitersCount = newCount;
            addParticleEmmiter();
        }
    }
    
    private void creepDistributionMaker(int countOfCreeps){
        for (int k=0;k<countOfCreeps;k++){
           // basicShapeCreep();
          moldedShapeCreep();
        }
    }
    
    private void basicShapeCreep(){
            Spatial creep = createBasicShapeCreep("creep_1", ColorRGBA.Red);
            randomCreepStartXPosition(creep);
    }
    
    private void moldedShapeCreep(){
            Spatial creep = loadMoldedCreep("creep_1");
            randomCreepStartXPosition(creep);
    }
    
    private void randomCreepStartXPosition(Spatial creep){
        boolean validPosition = false;
        Random r = new Random();

        while(validPosition == false){
            int newPostionX = r.nextInt(28);
            float xPosition = (float) (-7 + newPostionX*0.5);
            for (Spatial s : getCreeps()){
                if (s.getLocalTranslation().getY() == 0f && s.getLocalTranslation().getX() == xPosition){
                    continue; // used initial position, repeat
                }
            }
            creep.move(xPosition, 0.25f,-25f);
            validPosition = true;
        }
    }
    
    
    private Geometry createBasicShapeCreep(String name,ColorRGBA color){
        Box creepBox = new Box(0.25f,0.25f,0.25f); 
        Geometry model = new Geometry(name, creepBox); 
        Material creepMat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"); 
        creepMat.setColor("Color", color); 
        model.setMaterial(creepMat); 
        model.addControl(new CreepControl(this));
        initializeCreepStats(++creepLastIndex, model, creepInitialMaxHealth);
        getCreepPivot().attachChild(model);
        getCreeps().add(model);
        return model;
    }
    
    private Node loadMoldedCreep(String name){
        Node model = (Node) assetManager.loadModel("Models/spider.j3o");
        model.setName(name);
        model.addControl(new CreepControl(this));
        AnimChannel channel = model.getControl(AnimControl.class).createChannel();
        channel.setAnim(ANIM_CREEP_RUN);
        initializeCreepStats(++creepLastIndex, model, creepInitialMaxHealth);
        getCreepPivot().attachChild(model);
        getCreeps().add(model);
        model.scale(0.3f);
        return model;
    }
    
    private Geometry createTower(String name,ColorRGBA color){
         Box tower2box = new Box(0.5f,2.5f,0.5f); 
        Geometry tower = new Geometry(name, tower2box); 
        Material tower2mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"); 
        tower2mat.setColor("Color", color); 
        tower.setMaterial(tower2mat); 
        tower.addControl(new ExtendedTowerUserData());
        return tower;
    }
    
    private Node creatDonjoneTower(String name){
        Node tower1_geo = (Node) assetManager.loadModel("Models/cg_donjon.j3o");
        tower1_geo.setName("tower");
        tower1_geo.addControl(new ExtendedTowerUserData());
        tower1_geo.scale(0.1f);
        return tower1_geo;
    }
    
    private Node creatBase(String name){
        Node node = (Node) assetManager.loadModel("Models/cg_gateway.j3o");
        node.setName(name);
        node.scale(0.15f);
        return node;
    }
    
    private void initializeTowerStats(int index,Spatial tower){
        tower.setUserData("index", index);
//         for (int k=0;k<INITIAL_AMO_CHARGES;k++){
//            tower.getControl(ExtendedTowerUserData.class).getCharges().add(new AmoCharge(true,15));
//         }
    }
    
    private void initializeCreepStats(int index,Spatial creep, int health){
        creep.setUserData("index", index);
         creep.setUserData("health", health);
    }
    
    private void addParticleEmmiter(){
                 ParticleEmitter particleEm = new ParticleEmitter("dust", ParticleMesh.Type.Triangle, 100);
                 Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                 mat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/Smoke.png"));
                 particleEm.setImagesX(15);
                 particleEm.setImagesY(1);
                 particleEm.setNumParticles(20);
                 particleEm.setParticlesPerSec(1f);
                 particleEm.setLocalTranslation(0f, 4.5f, 2.5f);
                 particleEm.setSelectRandomImage(true);
                 particleEm.setRandomAngle(true);
                 particleEm.setGravity(0f, -1f, 0f);
                 particleEm.setLowLife(1f); // start 1
                 particleEm.setHighLife(5f); // start 5
                 particleEm.setStartSize(1f);
                 particleEm.setEndSize(4f);
                 particleEm.getParticleInfluencer().setInitialVelocity(new Vector3f(0f, 5f, 0f));
                 particleEm.getParticleInfluencer().setVelocityVariation(0.2f);
                 particleEm.setMaterial(mat);
                 rootNode.attachChild(particleEm);
    }
    
    @Override
     public void cleanup() {
         rootNode.detachChildNamed(PIVOT_CREEP_NODE);
         rootNode.detachChildNamed(PIVOT_PLAYER_NODE);
         rootNode.detachChildNamed(PIVOT_GROUND_NODE);
     }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @param health the health to set
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * @return the budget
     */
    public int getBudget() {
        return budget;
    }

    /**
     * @param budget the budget to set
     */
    public void setBudget(int budget) {
        this.budget = budget;
    }

    /**
     * @return the creeps
     */
    public List<Spatial> getCreeps() {
        return creeps;
    }

    /**
     * @param creeps the creeps to set
     */
    public void setCreeps(List<Spatial> creeps) {
        this.creeps = creeps;
    }

    /**
     * @return the creepElapsedPause
     */
    public float getCreepElapsedPause() {
        return creepElapsedPause;
    }

    /**
     * @param creepElapsedPause the creepElapsedPause to set
     */
    public void setCreepElapsedPause(float creepElapsedPause) {
        this.creepElapsedPause = creepElapsedPause;
    }

    /**
     * @return the creepPivot
     */
    public Node getCreepPivot() {
        return creepPivot;
    }

    /**
     * @param creepPivot the creepPivot to set
     */
    public void setCreepPivot(Node creepPivot) {
        this.creepPivot = creepPivot;
    }

    /**
     * @return the creepAttackDamage
     */
    public int getCreepAttackDamage() {
        return creepAttackDamage;
    }

    /**
     * @param creepAttackDamage the creepAttackDamage to set
     */
    public void setCreepAttackDamage(int creepAttackDamage) {
        this.creepAttackDamage = creepAttackDamage;
    }

    /**
     * @return the assetManager
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }
}
