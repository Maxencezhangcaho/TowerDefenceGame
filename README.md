# TowerDefenceShow
testing JME3

Demo tower deffence game.

Some spiders attacking player base. Four towers shoots on spiders.
Towers uses amo charges. 
Commands:
Move camera: W,A,S,D
Rotate Camera: drag with left mouse button
Chose tower: left mouse left click 
Add an amo charge (default 15U): space
If spider come close to base he attacks. Player health drops 5U for every attack. After first attack base start to 
generate a smoke signal that is proportional to health damage.

Code is runnable from jMonkeyEngine 3.0
Code is structured in AppStates and Control classes.
No physics in use. Pure ray collision detection.

